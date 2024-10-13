import type { AnyObject } from './base';

export interface Game extends AnyObject {
  aId: number
  aSx: number
  aSy: number
  bId: number
  bSx: number
  bSy: number
  map: number[][]
}
