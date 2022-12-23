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

function createRequest() {
  const axios = createAxios()

  const _get = <T = any>(url: string, { params }: GetParams = {}) => {
    url += handleUrlParams(params)
    return axios.get<T, Result<T>>(url)
  }

  const _post = <T = any>(url: string, { params, body = {} }: PostParams = {}) => {
    url += handleUrlParams(params)
    return axios.post<T, Result<T>>(url, body)
  }

  const _put = <T = any>(url: string, { params, body = {} }: PutParams = {}) => {
    url += handleUrlParams(params)
    return axios.put<T, Result<T>>(url, body)
  }

  const _delete = <T = any>(url: string, { params }: DeleteParams = {}) => {
    url += handleUrlParams(params)
    return axios.delete<T, Result<T>>(url)
  }

  return {
    get: _get,
    post: _post,
    put: _put,
    delete: _delete,
  }
}

export const R = createRequest()
