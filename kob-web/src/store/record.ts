import type { Game } from '~/types';

interface Player {
  id: number
  sx: number // 起始行
  sy: number // 起始列
}

type Loser = 'all' | 'A' | 'B' | 'none';

export const useRecordStore = defineStore(
  'recordStore',
  () => {
    const isRecord = ref(false); // 是否为录像
    const aSteps = ref<string | undefined>(); // 玩家 A 的操作字符串
    const bSteps = ref<string | undefined>(); // 玩家 B 的操作字符串
    const gameMap = ref<number[][]>();
    const players = ref<Player[]>([]);
    const loser = ref<Loser>('none');
    const recordFinished = ref(true); // 录像是否播放完毕

    const updateIsRecord = (value: boolean) => isRecord.value = value;
    const updateSteps = (_aSteps?: string, _bSteps?: string) => {
      aSteps.value = _aSteps;
      bSteps.value = _bSteps;
    };
    const updateLoser = (value: Loser) => loser.value = value;
    const updateRecordFinished = (value: boolean) => recordFinished.value = value;

    function updateGame({ aId, aSx, aSy, bId, bSx, bSy, map }: Game) {
      gameMap.value = map;
      players.value = [
        { id: aId, sx: aSx, sy: aSy },
        { id: bId, sx: bSx, sy: bSy },
      ];
    }

    function clearVideo() {
      updateIsRecord(false);
      updateSteps(undefined, undefined);
      updateLoser('none');
      gameMap.value = undefined;
      players.value = [];
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
    });
  },
);

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useRecordStore, import.meta.hot));
}
