import type { App } from 'vue'

export type UserModule = (app: App) => void
export type { App as AppContext }

/** 任意 `Object` */
export interface AnyObject {
  [key: string]: unknown
}

/** `通用` 请求参数 */
export interface HttpParams {
  urlParams?: AnyObject | AnyObject[]
}

/** `Get` 请求参数 */
export interface GetParams extends HttpParams {}

/** `Post` 请求参数 */
export interface PostParams extends HttpParams {
  body?: AnyObject
}

/** `Put` 请求参数 */
export interface PutParams extends HttpParams {
  body?: AnyObject
}

/** `Delete` 请求参数 */
export interface DeleteParams extends HttpParams {}

/** `Http` 返回数据结构 */
export interface Result<T> {
  code: number
  data: T | T[]
  msg?: string
}

/** 分页返回数据结构 */
export interface PageResult<T> {
  [key: string]: any
  records?: T[]
  total?: number
}

/** 登录状态 */
export type LoginState = 'hasLogin' | 'notLogin' | 'expire'

/** 菜单项数据结构 */
export interface Menu {
  id: number
  label: string
  path?: string
  icon?: string
  children?: Menu[]
}

/** 定义用户的数据类型 */
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
