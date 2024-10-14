import type { GameResult } from '~/store/pk';

export function getGameResult(loser: string): GameResult {
  const resultMap = {
    all: 'draw',
    A: 'playerBWon',
    B: 'playerAWon',
    none: 'ongoing',
  } as const;
  return resultMap[loser as keyof typeof resultMap];
}
