import type { AnyObject } from './models/base';

export interface PageQuery extends AnyObject {
  page?: number
  pageSize?: number
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
export type ApiResponseWithoutData = Omit<ApiResponse<never>, 'data'>;
export type ApiResponseWithData<T> = ApiResponse<T>;
export type PageDataResponse<T> = ApiResponseWithData<PageData<T>>;
