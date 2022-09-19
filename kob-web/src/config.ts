import type { Menu } from '~/types'

/** 是否为 `debug` 模式 */
export const debug = true

/**
 * 项目基本信息
 */
export const appMeta = {
  appShortName: 'KOB',
  appName: 'King Of Bots',
  description: '惊世骇俗的贪吃蛇双人对抗游戏',
  author: 'duende',
  github: 'https://github.com/dud9/kob',
}

/**
 * 项目基础布局
 */
export const appLayout = {
  /** 导航栏高度 */
  navHeight: '60px',
  /** 内容区内边距 */
  contentPadding: '20px',
  /** 底部栏高度 */
  footHeight: '50px',
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
