import type { Game } from '~/types';

interface Player {
  id: number
  sx: number // 起始行
  sy: number // 起始列
}

type Loser = 'all' | 'A' | 'B' | 'none';

export const useRecordStore = defineStore('recordStore', () => {
  const state = reactive({
    isRecord: false,
    aSteps: undefined as string | undefined,
    bSteps: undefined as string | undefined,
    gameMap: undefined as number[][] | undefined,
    players: [] as Player[],
    loser: 'none' as Loser,
    recordFinished: true,
  });

  function updateIsRecord(value: boolean) {
    state.isRecord = value;
  }

  function updateSteps(_aSteps?: string, _bSteps?: string) {
    state.aSteps = _aSteps;
    state.bSteps = _bSteps;
  }

  function updateLoser(value: Loser) {
    state.loser = value;
  }

  function updateRecordFinished(value: boolean) {
    state.recordFinished = value;
  }

  function updateGame({ aId, aSx, aSy, bId, bSx, bSy, map }: Game) {
    state.gameMap = map;
    state.players = [
      { id: aId, sx: aSx, sy: aSy },
      { id: bId, sx: bSx, sy: bSy },
    ];
  }

  function clearVideo() {
    Object.assign(state, {
      isRecord: false,
      aSteps: undefined,
      bSteps: undefined,
      loser: 'none',
      gameMap: undefined,
      players: [],
    });
  }

  return {
    ...toRefs(state),
    updateIsRecord,
    updateSteps,
    updateGame,
    updateLoser,
    clearVideo,
    updateRecordFinished,
  };
});

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useRecordStore, import.meta.hot));
}
