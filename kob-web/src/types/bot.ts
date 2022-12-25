import type { AnyObject } from './global'

export interface Bot extends AnyObject {
  id?: number
  userId?: number
  title?: string
  description?: string
  content?: string
  createTime?: Date
  modifyTime?: Date
}
