import type { App } from 'vue'

export type UserModule = (app: App) => void
export type { App as AppContext }

export interface AnyObject {
  [key: string]: unknown
}

export type LoginState = 'hasLogin' | 'notLogin' | 'expire'

// ----- Http -----

export interface HttpParams extends AnyObject {
  params?: AnyObject | AnyObject[]
}

export interface GetParams extends HttpParams {}

export interface PostParams extends HttpParams {
  body?: AnyObject
}

export interface PutParams extends HttpParams {
  body?: AnyObject
}

export interface DeleteParams extends HttpParams {}

// ----- Query & Result -----

export interface PageQuery extends AnyObject {
  page?: number
  pageSize?: number
}

export interface Result<T> extends AnyObject {
  code: number
  data: T
  msg?: string
}

export interface PageData<T> extends AnyObject {
  records?: T[]
  total?: number
}
