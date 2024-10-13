import type { AnyObject } from './global';

export interface Rank extends AnyObject {
  id?: number
  name?: string
  avatar?: string
  rating?: number
  createTime?: Date
  rankNum?: number
}
