import opponentDefaultAvatar from '~/assets/opponent.png';
import type { GameMap } from '~/scripts/map';
import type { Game, User } from '~/types';

type Opponent = Pick<User, 'name' | 'avatar'>;

interface Player {
  id: number
  sx: number // 起始行
  sy: number // 起始列
}

type Status = 'match' | 'play';

type Loser = 'all' | 'A' | 'B' | 'none';

const defaultOpponent = { // 初始对手信息
  name: '你的对手',
  avatar: opponentDefaultAvatar,
};

export const usePkStore = defineStore(
  'pkStore',
  () => {
    let status = $ref<Status>('match');
    let socket = $ref<WebSocket>();
    let opponent = $ref<Opponent>();
    let gameMap = $ref<number[][]>();
    let players = $ref<Player[]>([]);
    let gameMapObject = $ref<GameMap>();
    let loser = $ref<Loser>('none');

    const updateStatus = (value: Status) => status = value;
    const updateSocket = (value: WebSocket) => socket = value;
    const updateOpponent = (value = defaultOpponent) => opponent = value;
    const updateGameMapObject = (value: GameMap) => gameMapObject = value;
    const updateLoser = (value: Loser) => loser = value;

    function updateGame({ aId, aSx, aSy, bId, bSx, bSy, map }: Game) {
      gameMap = map;
      players = [
        { id: aId, sx: aSx, sy: aSy },
        { id: bId, sx: bSx, sy: bSy },
      ];
    }

    function reset() {
      updateStatus('match');
      updateOpponent();
      updateLoser('none');
      gameMap = undefined;
      players = [];
      gameMapObject = undefined;
    }

    return $$({
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
    });
  },
);

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(usePkStore, import.meta.hot));
}
