import type { Game, User } from '~/types'
import type { GameMap } from '~/logic'
import opponentDefaultAvatar from '~/assets/opponent.png'

type Opponent = Pick<User, 'name' | 'avatar'>

interface Player {
  id: number
  /** 起始行 */
  sx: number
  /** 起始列 */
  sy: number
}

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
    const players = ref<Player[]>([])
    const gameMapObject = ref<GameMap>()

    function updateStatus(_status: 'match' | 'play') {
      status.value = _status
    }

    function updateSocket(_socket: WebSocket) {
      socket.value = _socket
    }

    function updateOpponent(_opponent = defaultOpponent) {
      opponent.value = _opponent
    }

    function updateGame({ aId, aSx, aSy, bId, bSx, bSy, map }: Game) {
      gameMap.value = map
      players.value = [
        { id: aId, sx: aSx, sy: aSy },
        { id: bId, sx: bSx, sy: bSy },
      ]
    }

    function updateGameMapObject(_gameMapObject: GameMap) {
      gameMapObject.value = _gameMapObject
    }

    return {
      status,
      socket,
      opponent,
      gameMap,
      gameMapObject,
      updateStatus,
      updateSocket,
      updateOpponent,
      updateGame,
      updateGameMapObject,
    }
  },
  {
    persist: {
      enabled: true,
    },
  },
)
