import type { EntityBase } from './base';

export interface Record extends EntityBase {
  aId: number
  aSx: number
  aSy: number
  bId: number
  bSx: number
  bSy: number
  aSteps: string
  bSteps: string
  map: string
  loser: string

  aName?: string
  aAvatar?: string
  bName?: string
  bAvatar?: string

  mode?: string
}
