<script setup lang="ts">
import ChooseLevel from './components/ChooseLevel.vue'
import { getToken } from '~/utils'
import defaultAvatar from '~/assets/default-avatar.png'

const token = getToken()

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

let showPking = $ref(false)
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
    showPking = true
    message.success('人机试炼开始')
    useTimeoutFn(() => {
      showPking = false
      socket.send(JSON.stringify({
        event: 'start-game',
      }))
    }, 4500)
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
  <div w-full h-full flex="~ col">
    <ChooseLevel v-if="status === 'match'" />
    <GamePlayground v-if="status === 'play' && !showPking" />
    <ShowPking v-if="status === 'play' && showPking" />
    <ResultBoard v-if="loser !== 'none'" />
    <Confetti :passed="showConfetti" />

    <div v-if="status === 'play'">
      <div text-20px font-bold flex justify-center items-center>
        <div i-akar-icons-face-wink mr-2 />
        您在左下角
      </div>
    </div>
  </div>
</template>
