import type { User } from '~/types'
import opponentDefaultAvatar from '~/assets/opponent.png'

type Opponent = Pick<User, 'name' | 'avatar'>

// 初始对手信息
const defaultOpponent = {
  name: '你的对手',
  avatar: opponentDefaultAvatar,
}

export const usePkStore = defineStore(
  'pkStore',
  () => {
    // `match` - 匹配界面， `play` - 对战界面
    const status = ref<'match' | 'play'>('match')
    const socket = ref()
    const opponent = ref<Opponent>()
    const gameMap = ref<number[][]>()

    function updateStatus(_status: 'match' | 'play') {
      status.value = _status
    }

    function updateSocket(_socket: WebSocket) {
      socket.value = _socket
    }

    function updateOpponent(_opponent = defaultOpponent) {
      opponent.value = _opponent
    }

    function updateGameMap(_gameMap: number[][]) {
      gameMap.value = _gameMap
    }

    return {
      status,
      socket,
      opponent,
      gameMap,
      updateStatus,
      updateSocket,
      updateOpponent,
      updateGameMap,
    }
  },
  {
    persist: {
      enabled: true,
    },
  },
)
