import type { App } from 'vue'
export type UserModule = (app: App) => void
export type { App as AppContext }

/**
 * 菜单项数据结构
 */
export interface Menu {
  id: number
  title: string
  path?: string
  icon?: string
  children?: Menu[]
}
