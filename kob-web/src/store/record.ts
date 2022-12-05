import type { Game } from '~/types'

interface Player {
  id: number
  sx: number // 起始行
  sy: number // 起始列
}

export const useRecordStore = defineStore(
  'recordStore',
  () => {
    // 是否为录像
    const isRecord = ref(false)
    // 玩家 A 的操作字符串
    const aSteps = ref()
    // 玩家 B 的操作字符串
    const bSteps = ref()
    // 游戏对局信息
    const gameMap = ref<number[][]>()
    const players = ref<Player[]>([])
    // all - 平局 | A - A 输 | B - B 输
    const loser = ref<'all' | 'A' | 'B' | 'none'>('none')
    // 录像是否播放完毕
    const recordFinished = ref(true)

    function updateIsRecord(_isRecord: boolean) {
      isRecord.value = _isRecord
    }

    function updateSteps(_aSteps?: string, _bSteps?: string) {
      aSteps.value = _aSteps
      bSteps.value = _bSteps
    }

    function updateGame({ aId, aSx, aSy, bId, bSx, bSy, map }: Game) {
      gameMap.value = map
      players.value = [
        { id: aId, sx: aSx, sy: aSy },
        { id: bId, sx: bSx, sy: bSy },
      ]
    }

    function updateLoser(_loser: 'all' | 'A' | 'B' | 'none') {
      loser.value = _loser
    }

    function clearVideo() {
      updateIsRecord(false)
      updateSteps(undefined, undefined)
      updateLoser('none')
      gameMap.value = undefined
      players.value = []
    }

    function updateRecordFinished(_recordFinished: boolean) {
      recordFinished.value = _recordFinished
    }

    return {
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
    }
  },
)
