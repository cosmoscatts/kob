import type { Game } from '~/types'

interface Player {
  id: number
  sx: number // 起始行
  sy: number // 起始列
}

type Loser = 'all' | 'A' | 'B' | 'none'

export const useRecordStore = defineStore(
  'recordStore',
  () => {
    let isRecord = $ref(false) // 是否为录像
    let aSteps = $ref<string | undefined>() // 玩家 A 的操作字符串
    let bSteps = $ref<string | undefined>() // 玩家 B 的操作字符串
    let gameMap = $ref<number[][]>()
    let players = $ref<Player[]>([])
    let loser = $ref<Loser>('none')
    let recordFinished = $ref(true) // 录像是否播放完毕

    const updateIsRecord = (value: boolean) => isRecord = value
    const updateSteps = (_aSteps?: string, _bSteps?: string) => {
      aSteps = _aSteps
      bSteps = _bSteps
    }
    const updateLoser = (value: Loser) => loser = value
    const updateRecordFinished = (value: boolean) => recordFinished = value

    function updateGame({ aId, aSx, aSy, bId, bSx, bSy, map }: Game) {
      gameMap = map
      players = [
        { id: aId, sx: aSx, sy: aSy },
        { id: bId, sx: bSx, sy: bSy },
      ]
    }

    function clearVideo() {
      updateIsRecord(false)
      updateSteps(undefined, undefined)
      updateLoser('none')
      gameMap = undefined
      players = []
    }

    return $$({
      isRecord,
      aSteps,
      bSteps,
      gameMap,
      players,
      loser,
      recordFinished,
      updateIsRecord,
      updateSteps,
      updateGame,
      updateLoser,
      clearVideo,
      updateRecordFinished,
    })
  },
)

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useRecordStore, import.meta.hot))
}
