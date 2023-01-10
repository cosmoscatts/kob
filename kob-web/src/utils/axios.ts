import type { AxiosRequestConfig, AxiosResponse } from 'axios'
import axios from 'axios'
import { Token } from './token'

const AXIOS_TIMEOUT = 5000

export function createAxios() {
  const _axios = axios.create({
    baseURL: import.meta.env.VITE_BASE_API_URL as string,
    timeout: AXIOS_TIMEOUT,
  })
  _axios.interceptors.request.use(
    (config: AxiosRequestConfig) => {
      if (Token.get()) { // 统一在 header 中添加 token
        config = {
          ...config,
          headers: {
            Authorization: `Bearer ${Token.get()}`,
          },
        }
      }
      return config
    },
    (e: any) => { Promise.reject(e) },
  )
  _axios.interceptors.response.use(
    async (response: AxiosResponse) => {
      const {
        data: { code, data, msg },
      } = response
      return Promise.resolve({ code, data, msg }) as any
    },
    (error: any) => Promise.reject(error),
  )
  return _axios
}
