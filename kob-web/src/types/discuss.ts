import type { User } from './user'
import type { AnyObject } from './global'

export interface Discuss extends AnyObject {
  id?: number
  userId?: number
  remark?: string
  parentId?: number
  createTime?: Date

  user?: User
  likes?: number
}
