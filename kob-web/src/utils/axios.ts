import type { AxiosRequestConfig, AxiosResponse } from 'axios'
import axios from 'axios'
import { Token } from '.'

const AXIOS_TIMEOUT = 5000

export function createAxios() {
  const _axios = axios.create({
    baseURL: import.meta.env.VITE_BASE_API_URL as string,
    timeout: AXIOS_TIMEOUT,
  })

  _axios.interceptors.request.use(
    (config: AxiosRequestConfig) => {
      // 统一在 header 中添加 token
      const token = Token.get()
      if (token)
        config!.headers!.Authorization = `Bearer ${token}`
      return config
    },
    (e: any) => {
      Promise.reject(e)
    },
  )

  _axios.interceptors.response.use(
    async (response: AxiosResponse) => {
      const {
        data: { code, data, msg },
      } = response
      return Promise.resolve({ code, data, msg }) as any
    },
    (error: any) => {
      return Promise.reject(error)
    },
  )

  return _axios
}
