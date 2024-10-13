import type { AnyObject } from './models/base';

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

export interface ApiResponse<T = any> {
  code: number
  data: T
  msg: string
}
export type ApiResponseWithoutData = Omit<Result<never>, 'data'>;
export type ApiResponseWithData<T> = Result<T>;
export type PageDataResponse<T> = ApiResponseWithData<PageData<T>>;
