import type { EntityBase } from './base';

export interface User extends EntityBase {
  username?: string
  name?: string
  password?: string
  avatar?: string
  phone?: string
  email?: string
  rating?: number
}

export interface UserSecurity {
  hasPassword?: boolean
  hasPhone?: boolean
  hasBilibili?: boolean
  hasGithub?: boolean
  hasQQ?: boolean
  hasWechat?: boolean
}
