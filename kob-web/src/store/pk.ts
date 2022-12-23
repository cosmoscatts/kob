import type { Game, User } from '~/types'
import type { GameMap } from '~/scripts/map'
import opponentDefaultAvatar from '~/assets/opponent.png'

type Opponent = Pick<User, 'name' | 'avatar'>

interface Player {
  id: number
  sx: number // 起始行
  sy: number // 起始列
}

type Status = 'match' | 'play'

type Loser = 'all' | 'A' | 'B' | 'none'

const defaultOpponent = { // 初始对手信息
  name: '你的对手',
  avatar: opponentDefaultAvatar,
}

export const usePkStore = defineStore(
  'pkStore',
  () => {
    const status = ref<Status>('match')
    const socket = ref<WebSocket>()
    const opponent = ref<Opponent>()
    const gameMap = ref<number[][]>()
    const players = ref<Player[]>([])
    const gameMapObject = ref<GameMap>()
    const loser = ref<Loser>('none')

    const updateStatus = (value: Status) => status.value = value
    const updateSocket = (value: WebSocket) => socket.value = value
    const updateOpponent = (value = defaultOpponent) => opponent.value = value
    const updateGameMapObject = (value: GameMap) => gameMapObject.value = value
    const updateLoser = (value: Loser) => loser.value = value

    function updateGame({ aId, aSx, aSy, bId, bSx, bSy, map }: Game) {
      gameMap.value = map
      players.value = [
        { id: aId, sx: aSx, sy: aSy },
        { id: bId, sx: bSx, sy: bSy },
      ]
    }

    function reset() {
      updateStatus('match')
      updateOpponent()
      updateLoser('none')
      gameMap.value = undefined
      players.value = []
      gameMapObject.value = undefined
    }

    return {
      status,
      socket,
      opponent,
      gameMap,
      players,
      gameMapObject,
      loser,
      updateStatus,
      updateSocket,
      updateOpponent,
      updateGame,
      updateGameMapObject,
      updateLoser,
      reset,
    }
  },
)

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(usePkStore, import.meta.hot))
}
