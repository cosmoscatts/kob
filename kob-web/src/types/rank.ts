import type { AnyObject } from './global'

export interface Rank extends AnyObject {
  /** 用户 id */
  id?: number
  /** 用户名称 */
  name?: string
  /** 头像 */
  avatar?: string
  /** 天梯分 */
  rating?: number
  /** 创建时间 */
  createTime?: Date
  /** 排名 */
  rankNum?: number
}
