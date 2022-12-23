import type { AnyObject } from './global'

export interface User extends AnyObject {
  id?: number // id
  username?: string // 账号
  name?: string // 用户名称
  password?: string // 密码
  avatar?: string // 头像
  phone?: string // 手机号
  email?: string // 邮箱
  rating?: number // 天梯分
  createTime?: Date // 创建时间
}

export interface UserSecurity extends AnyObject {
  hasPassword?: boolean
  hasPhone?: boolean
  hasBilibili?: boolean
  hasGithub?: boolean
  hasQQ?: boolean
  hasWechat?: boolean
}
