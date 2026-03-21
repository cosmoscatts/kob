import { defineStore } from 'pinia';
import { reactive, toRefs } from 'vue';
import opponentDefaultAvatar from '~/assets/opponent.png';
import type { GameMap } from '~/game/map';
import type { Game, User } from '~/types';

export type Opponent = Pick<User, 'name' | 'avatar'>;
export type GameStatus = 'matching' | 'playing';
export type GameResult = 'draw' | 'playerAWon' | 'playerBWon' | 'ongoing';
export type ConnectionStatus = 'connected' | 'disconnected' | 'reconnecting';

interface Player {
  id: number
  sx: number
  sy: number
}

interface PkState {
  status: GameStatus
  socket: WebSocket | null
  opponent: Opponent
  gameMap: number[][] | null
  players: Player[]
  gameMapObject: GameMap | null
  gameResult: GameResult
  connectionStatus: ConnectionStatus
}

const DEFAULT_OPPONENT: Opponent = {
  name: '你的对手',
  avatar: opponentDefaultAvatar,
};

export const usePkStore = defineStore('pkStore', () => {
  const state = reactive<PkState>({
    status: 'matching',
    socket: null,
    opponent: DEFAULT_OPPONENT,
    gameMap: null,
    players: [],
    gameMapObject: null,
    gameResult: 'ongoing',
    connectionStatus: 'disconnected',
  });

  const updateGameState = <K extends keyof PkState>(
    newState: Partial<Pick<PkState, K>> & { opponent?: PkState['opponent'] | null | undefined },
  ) => {
    (Object.keys(newState) as K[]).forEach((key) => {
      if (key === 'opponent') {
        if (newState[key] === null || newState[key] === undefined) {
          (state[key] as PkState['opponent']) = DEFAULT_OPPONENT;
        } else {
          (state[key] as PkState['opponent']) = newState[key] as PkState['opponent'];
        }
      } else {
        (state[key] as PkState[K]) = newState[key] as PkState[K];
      }
    });
  };

  const updateGame = ({ aId, aSx, aSy, bId, bSx, bSy, map }: Game) => {
    updateGameState({
      gameMap: map,
      players: [
        { id: aId, sx: aSx, sy: aSy },
        { id: bId, sx: bSx, sy: bSy },
      ],
    });
  };

  const resetGame = () => {
    updateGameState({
      status: 'matching',
      opponent: DEFAULT_OPPONENT,
      gameResult: 'ongoing',
      gameMap: null,
      players: [],
      gameMapObject: null,
    });
  };

  return {
    ...toRefs(state),
    updateGameState,
    updateGame,
    resetGame,
  };
});

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(usePkStore, import.meta.hot));
}
