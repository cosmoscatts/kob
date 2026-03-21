import { tokenStorage } from '~/utils/token-storage'

const formatSocketUrl = () => {
  const urlPrefix = import.meta.env.VITE_BASE_SOCKET_URL
  return `${urlPrefix}/websocket/${tokenStorage.get()}/`
}

/** 心跳间隔 15 秒 */
const HEARTBEAT_INTERVAL = 15_000
/** 最大重连次数 */
const MAX_RECONNECT_ATTEMPTS = 5
/** 重连基础延迟（指数退避） */
const RECONNECT_BASE_DELAY = 1000

export function useSocket(onmessage: (msg: MessageEvent) => void) {
  const pkStore = usePkStore()

  let socket: WebSocket | null = null
  let heartbeatTimer: ReturnType<typeof setInterval> | null = null
  let reconnectAttempts = 0
  let manualClose = false

  const startHeartbeat = () => {
    stopHeartbeat()
    heartbeatTimer = setInterval(() => {
      if (socket?.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify({ event: 'ping' }))
      }
    }, HEARTBEAT_INTERVAL)
  }

  const stopHeartbeat = () => {
    if (heartbeatTimer) {
      clearInterval(heartbeatTimer)
      heartbeatTimer = null
    }
  }

  const connect = () => {
    socket = new WebSocket(formatSocketUrl())

    socket.onopen = () => {
      reconnectAttempts = 0
      pkStore.updateGameState({ socket, connectionStatus: 'connected' })
      startHeartbeat()
    }

    socket.onmessage = (msg: MessageEvent) => {
      const data = JSON.parse(msg.data)
      // pong 是心跳回复，不传递给业务层
      if (data.event === 'pong') return
      onmessage(msg)
    }

    socket.onclose = () => {
      stopHeartbeat()
      pkStore.updateGameState({ socket: null, connectionStatus: 'disconnected' })

      if (!manualClose && reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
        pkStore.updateGameState({ connectionStatus: 'reconnecting' })
        const delay = RECONNECT_BASE_DELAY * Math.pow(2, reconnectAttempts)
        reconnectAttempts++
        setTimeout(connect, delay)
      }
    }

    socket.onerror = () => {
      // onclose 会紧随其后被调用，重连逻辑在 onclose 中处理
    }
  }

  const close = () => {
    manualClose = true
    stopHeartbeat()
    if (socket) {
      socket.close()
      socket = null
    }
  }

  connect()

  return { close }
}
