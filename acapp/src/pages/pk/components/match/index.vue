<script setup lang="ts">
import defaultAvatar from '~/assets/default-avatar.png'

const userStore = useUserStore()
const { user } = storeToRefs(userStore)

const pkStore = usePkStore()
const { status, loser, gameMapObject, players } = storeToRefs(pkStore)
const { updateSocket, updateOpponent, updateGame, updateStatus, updateLoser } = pkStore

// 更新对手信息
updateOpponent()

const urlPrefix = import.meta.env.MODE === 'development'
  ? 'ws://127.0.0.1:3000'
  : 'wss://app3626.acapp.acwing.com.cn'
const socketUrl = `${urlPrefix}/websocket/${userStore.token}/`
const socket = new WebSocket(socketUrl)

let showFightAnimation = $ref(false)

onMounted(() => {
  socket.onopen = () => {
    updateSocket(socket)
  }

  socket.onmessage = (msg) => {
    const { message } = useGlobalNaiveApi()
    const data = JSON.parse(msg.data)
    // 匹配成功
    if (data.event === 'match-success') {
      updateOpponent({
        name: data?.opponentName || '-',
        avatar: data?.opponentAvatar ?? defaultAvatar,
      })
      updateGame(data.game)
      updateStatus('play')
      message.success('匹配成功')
      showFightAnimation = true
      useTimeoutFn(() => {
        showFightAnimation = false
      }, 5000)
    }
    else if (data.event === 'move') {
      const { snakes } = gameMapObject.value!
      const [snake0, snake1] = snakes
      snake0.setDirection(data.aDirection)
      snake1.setDirection(data.bDirection)
    }
    else if (data.event === 'result') {
      const { snakes } = gameMapObject.value!
      const [snake0, snake1] = snakes

      if (['all', 'A'].includes(data.loser))
        snake0.status = 'die'

      if (['all', 'B'].includes(data.loser))
        snake1.status = 'die'

      updateLoser(data.loser)
    }
  }

  socket.onclose = () => {}
})

function clear() {
  socket.close()
  updateLoser('none')
  updateOpponent()
  updateStatus('match')
}

onUnmounted(clear)

const showConfetti = computed(() => {
  return (loser.value === 'A' && players.value[1].id === user.value?.id)
  || (loser.value === 'B' && players.value[0].id === user.value?.id)
})
</script>

<template>
  <div w-full h-full flex="~ col">
    <GameMatchGround v-if="status === 'match'" />
    <GamePlayground v-if="(status === 'play' && !showFightAnimation)" />
    <FightAnimation v-if="(status === 'play' && showFightAnimation)" />
    <ResultBoard v-if="loser !== 'none'" />
    <Confetti :passed="showConfetti" />

    <div v-if="status === 'play'">
      <div v-if="user!.id === players[0]?.id" text-20px font-bold flex justify-center items-center>
        <div i-akar-icons-face-wink mr-2 />
        您在左下角
      </div>
      <div v-if="user!.id === players[1]?.id" text-20px font-bold flex justify-center items-center>
        <div i-akar-icons-face-wink mr-2 />
        您在右上角
      </div>
    </div>
  </div>
</template>
