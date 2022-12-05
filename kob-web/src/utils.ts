import { composite } from 'seemly'
import { colord, extend } from 'colord'
import mixPlugin from 'colord/plugins/mix'

import type { AxiosRequestConfig, AxiosResponse } from 'axios'
import axios from 'axios'
import { TOKEN_KEY } from '~/config'

extend([mixPlugin])
const ALPHA = 0.8

export type RGBA = [number, number, number, number]
export type RGB = [number, number, number]

/**
 * 创建 Hover 颜色
 * @param color
 */
export function createHoverColor(color: string | RGB | RGBA) {
  return composite(color, [255, 255, 255, 0.12])
}
/**
 * 创建 Pressed 颜色
 * @param color
 */
export function createPressedColor(color: string | RGB | RGBA) {
  return composite(color, [0, 0, 0, 0.15])
}
/**
 * 给颜色加透明度
 * @param color - 颜色
 * @param alpha - 透明度(0 - 1)
 */
export function addColorAlpha(color: string, alpha: number) {
  return colord(color).alpha(alpha).toHex()
}
/**
 * 生成主色调的其他状态颜色，包括 hover、pressed、suppl 等状态
 */
export function generatePrimaryColor(_primaryColor: string) {
  return {
    primaryColor: _primaryColor,
    primaryColorHover: createHoverColor(_primaryColor),
    primaryColorPressed: createPressedColor(_primaryColor),
    primaryColorSuppl: addColorAlpha(_primaryColor, ALPHA),
  }
}

/**
 * 获取 token
 */
export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}
/**
 * 设置 token
 */
export function setToken(token: string) {
  localStorage.setItem(TOKEN_KEY, token)
}
/**
 * 删除 token
 */
export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
}

const AXIOS_TIMEOUT = 5000

/**
 * 创建 axios
 */
export function createAxios() {
  const _axios = axios.create({
    baseURL: import.meta.env.VITE_BASE_API_URL as string,
    timeout: AXIOS_TIMEOUT,
  })

  _axios.interceptors.request.use(
    (config: AxiosRequestConfig) => {
      // 统一在 header 中添加 token
      const token = getToken()
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
