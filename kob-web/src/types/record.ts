import type { AnyObject } from './global'

export interface Record extends AnyObject {
  id: number
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
  createTime: Date

  aName?: string
  aAvatar?: string
  bName?: string
  bAvatar?: string

  mode?: string
}
