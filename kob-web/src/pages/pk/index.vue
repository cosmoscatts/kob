<script setup lang="ts">
import opponentDefaultAvatar from '~/assets/opponent.png'
import { appLayout } from '~/config'
import { getToken } from '~/utils'

const token = getToken()
const { navHeight, footHeight, contentPadding } = appLayout

const diffHeight = computed(() => {
  return navHeight + footHeight + contentPadding * 2 + 1 + 1 + 3
})

const pkStore = usePkStore()
const { status } = storeToRefs(pkStore)
const { updateSocket, updateOpponent, updateGameMap, updateStatus } = pkStore

updateOpponent({
  name: '我的对手',
  avatar: opponentDefaultAvatar,
})

const socketUrl = `ws://127.0.0.1:3000/websocket/${token}/`

const socket = new WebSocket(socketUrl)

socket.onopen = () => {
  updateSocket(socket)
}

socket.onmessage = (msg) => {
  const data = JSON.parse(msg.data)
  // 匹配成功
  if (data.event === 'match-success') {
    updateOpponent({
      name: data.name,
      avatar: data.avatar,
    })
    updateGameMap(data.gameMap)
    useTimeoutFn(() => {
      updateStatus('play')
    }, 2000)
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
