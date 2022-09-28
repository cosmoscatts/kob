<script setup lang="ts">
import { appLayout } from '~/config'
import { getToken } from '~/utils'
import defaultAvatar from '~/assets/default-avatar.png'

const token = getToken()
const { navHeight, footHeight, contentPadding } = appLayout

const diffHeight = computed(() => {
  return navHeight + footHeight + contentPadding * 2 + 1 + 1 + 3
})

const pkStore = usePkStore()
const { status, gameMapObject } = storeToRefs(pkStore)
const { updateSocket, updateOpponent, updateGame, updateStatus } = pkStore

// 更新对手信息
updateOpponent()

const socketUrl = `ws://127.0.0.1:3000/websocket/${token}/`

const socket = new WebSocket(socketUrl)

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
    message.success('匹配成功')
    useTimeoutFn(() => {
      updateStatus('play')
    }, 2000)
  }
  else if (data.event === 'move') {
    const { snakes } = gameMapObject.value
  }
  else if (data.event === 'result') {

  }
}

socket.onclose = () => {
}

onUnmounted(() => {
  socket.close()
})
</script>

<template>
  <div :style="{ minHeight: `calc(100vh - ${diffHeight}px)` }" flex-center>
    <GameMatchGround v-if="status === 'match'" />
    <GamePlayground v-else />
  </div>
</template>
