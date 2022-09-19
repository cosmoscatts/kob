import type { Menu } from '~/types'

/**
 * 项目基本信息
 */
export const appMeta = {
  appShortName: '',
  appName: '',
  description: '',
  author: '',
  github: '',
}

/**
 * 项目基础布局
 */
export const appLayout = {
  navHeight: '',
  contentPadding: '',
  footHeight: '',
}

/**
 * 项目菜单
 */
export const appMenus: Menu[] = [
  {
    id: 101,
    title: 'PK 对战',
    path: '/pk',
    icon: '',
  },
]
