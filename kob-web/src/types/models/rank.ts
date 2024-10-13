import type { EntityBase } from './base';

export interface Rank extends EntityBase {
  name?: string
  avatar?: string
  rating?: number
  rankNum?: number
}
