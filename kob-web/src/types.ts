import type { App } from 'vue'

export type UserModule = (app: App) => void
export type { App as AppContext }

/**
 * 登录状态
 */
export type LoginState = 'hasLogin' | 'notLogin' | 'expire'

/**
 * 菜单项数据结构
 */
export interface Menu {
  id: number
  label: string
  path?: string
  icon?: string
  children?: Menu[]
}

/**
 * 定义用户的数据类型
 */
export interface User {
  /** id */
  id?: number
  /** 账号 */
  username?: string
  /** 用户名称 */
  name?: string
  /** 密码 */
  password?: string
  /** 头像 */
  avatar?: string
  /** 手机号 */
  phone?: string
  /** 邮箱 */
  email?: string
  /** 创建时间 */
  createTime?: Date
}
