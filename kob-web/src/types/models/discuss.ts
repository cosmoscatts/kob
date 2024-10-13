import type { EntityBase, UserOwnedEntity } from './base';
import type { User } from './user';

export interface Discuss extends EntityBase, UserOwnedEntity {
  remark?: string
  parentId?: number
  user?: User
  likes?: number
}
