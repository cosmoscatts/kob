import type { AnyObject } from './global'

export interface Bot extends AnyObject {
  id?: number // id
  userId?: number // 用户 id
  title?: string // 标题
  description?: string // 描述
  content?: string // 内容
  createTime?: Date // 创建时间
  modifyTime?: Date // 修改时间
}
