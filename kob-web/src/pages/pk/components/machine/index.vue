<script setup lang="ts">
import ChooseLevel from './components/ChooseLevel.vue'
import { appLayout } from '~/config'
import { getToken } from '~/utils'
import defaultAvatar from '~/assets/default-avatar.png'

const token = getToken()
const { navHeight, footHeight, contentPadding } = appLayout

const diffHeight = computed(() => {
  return navHeight + footHeight + contentPadding * 2 + 1 + 1 + 3
})

const { user } = storeToRefs(useUserStore())

const pkStore = usePkStore()
const { status, loser, gameMapObject, players } = storeToRefs(pkStore)
const { updateSocket, updateOpponent, updateGame, updateStatus, updateLoser } = pkStore

// 更新对手信息
updateOpponent()

const urlPrefix = import.meta.env.MODE === 'development'
  ? 'ws://127.0.0.1:3000'
  : 'wss://app3626.acapp.acwing.com.cn'
const socketUrl = `${urlPrefix}/websocket/${token}/`

const socket = new WebSocket(socketUrl)

socket.onopen = () => updateSocket(socket)

let showFightAnimation = $ref(false)
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
    showFightAnimation = true
    message.success('人机试炼开始')
    useTimeoutFn(() => {
      showFightAnimation = false
      socket.send(JSON.stringify({
        event: 'start-game',
      }))
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
  <div :style="{ minHeight: `calc(100vh - ${diffHeight}px)` }" flex="col center">
    <ChooseLevel v-if="status === 'match'" />
    <GamePlayground v-if="(status === 'play' && !showFightAnimation)" />
    <FightAnimation v-if="(status === 'play' && showFightAnimation)" />
    <ResultBoard v-if="loser !== 'none'" />
    <Confetti :passed="showConfetti" />

    <div v-if="status === 'play'" mt-15px h-5vh>
      <div text-24px font-bold flex-center>
        <div i-akar-icons-face-wink mr-2 />
        您在左下角
      </div>
    </div>
  </div>
</template>
