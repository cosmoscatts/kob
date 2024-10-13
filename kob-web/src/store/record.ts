import type { Game } from '~/types';

interface Player {
  id: number
  sx: number // 起始行
  sy: number // 起始列
}

type GameResult = 'draw' | 'playerAWon' | 'playerBWon' | 'ongoing';

export const useRecordStore = defineStore('record', () => {
  const state = reactive({
    isRecording: false,
    aSteps: '' as string,
    bSteps: '' as string,
    gameMap: [] as number[][],
    players: [] as Player[],
    gameResult: 'ongoing' as GameResult,
    isReplayFinished: true,
  });

  function setRecordingState(isRecording: boolean) {
    state.isRecording = isRecording;
  }

  function setSteps(aSteps?: string, bSteps?: string) {
    state.aSteps = aSteps ?? '';
    state.bSteps = bSteps ?? '';
  }

  function setGameResult(result: GameResult) {
    state.gameResult = result;
  }

  function setReplayFinished(isFinished: boolean) {
    state.isReplayFinished = isFinished;
  }

  function updateGameState({ aId, aSx, aSy, bId, bSx, bSy, map }: Game) {
    state.gameMap = map;
    state.players = [
      { id: aId, sx: aSx, sy: aSy },
      { id: bId, sx: bSx, sy: bSy },
    ];
  }

  function resetRecordState() {
    Object.assign(state, {
      isRecording: false,
      aSteps: '',
      bSteps: '',
      gameResult: 'ongoing',
      gameMap: [],
      players: [],
    });
  }

  return {
    ...toRefs(state),
    setRecordingState,
    setSteps,
    updateGameState,
    setGameResult,
    resetRecordState,
    setReplayFinished,
  };
});

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useRecordStore, import.meta.hot));
}
