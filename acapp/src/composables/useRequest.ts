import { createAxios } from '~/utils'
import type {
  AnyObject,
  DeleteParams,
  GetParams,
  PostParams,
  PutParams,
  Result,
} from '~/types'

const handleUrlParams = (params?: AnyObject | AnyObject[]) => {
  if (!params) return ''
  if (!Array.isArray(params)) params = [params]
  let paramStr = ''
  for (const [k, v] of params.flatMap((i: AnyObject) => Object.entries(i))) {
    if (!v || (Array.isArray(v) && v.length === 0)) continue
    const values = Array.isArray(v) ? [...v] : [v]
    values.forEach(i => paramStr += `&${encodeURIComponent(k)}=${encodeURIComponent(i as string)}`)
  }
  return `?${paramStr.slice(1)}`
}

/**
 * 统一封装 axios 请求
 */
function createRequest() {
  const axios = createAxios()

  const _get = <T = any>(url: string, { urlParams }: GetParams) => {
    url += handleUrlParams(urlParams)
    return axios.get<any, Result<T>>(url)
  }

  const _post = <T = any>(url: string, { urlParams, body = {} }: PostParams) => {
    url += handleUrlParams(urlParams)
    return axios.post<any, Result<T>>(url, body)
  }

  const _put = <T = any>(url: string, { urlParams, body = {} }: PutParams) => {
    url += handleUrlParams(urlParams)
    return axios.put<any, Result<T>>(url, body)
  }

  const _delete = <T = any>(url: string, { urlParams }: DeleteParams) => {
    url += handleUrlParams(urlParams)
    return axios.delete<any, Result<T>>(url)
  }

  return {
    get: _get,
    post: _post,
    put: _put,
    delete: _delete,
  }
}

export const useRequest = createRequest()
