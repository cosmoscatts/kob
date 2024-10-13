import type { AnyObject } from './global';
import type { User } from './user';

export interface Discuss extends AnyObject {
  id?: number
  userId?: number
  remark?: string
  parentId?: number
  createTime?: Date

  user?: User
  likes?: number
}
