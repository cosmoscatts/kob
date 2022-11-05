import type { Component } from 'vue'
import { CrownOutlined, RobotOutlined } from '@vicons/antd'
import { DocumentTextOutline, HomeOutline } from '@vicons/ionicons5'
import type { Menu } from '~/types'

/** 是否为 `debug` 模式 */
export const debug = import.meta.env.MODE === 'development'

/** 存储 `token` 的键 */
export const TOKEN_KEY = 'jwt_token'

/**
 * 项目基本信息
 */
export const appMeta = {
  appShortName: 'KOB',
  appName: '玩蛇仙人',
  description: '玩蛇两年半的练习生',
  author: 'Cosmoscatts',
  github: 'https://github.com/cosmoscatts/kob',
  copyRight: 'KING OF BOTS © COSMOSCATTS',
}

/**
 * 项目基础布局
 */
export const appLayout = {
  /** 主题色 */
  primaryColor: '#0d9488',
  /** 导航栏高度 */
  navHeight: 60,
  /** 内容区内边距 */
  contentPadding: 30,
  /** 底部栏高度 */
  footHeight: 50,
  /** `backTop` 距离页面右部的距离 */
  backTopRight: 20,
  /** `backTop` 距离页面底部的距离 */
  backTopBottom: 200,
  /** `backTop` 滚动时触发显示回到顶部的高度 */
  backTopvisibilityHeight: 250,
}

/**
 * 项目菜单
 */
export const appMenus: Menu[] = [
  {
    id: 101,
    label: '首页',
    path: '/home',
    icon: 'home',
  },
  {
    id: 102,
    label: 'PK 对战',
    path: '/pk',
    icon: 'pk',
  },
  {
    id: 103,
    label: '对局列表',
    path: '/record',
    icon: 'record',
  },
  {
    id: 104,
    label: '排行榜',
    path: '/rank',
    icon: 'rank',
  },
]

/**
 * 菜单图标映射
 */
export const appMenuIconMap: Record<string, Component> = {
  home: HomeOutline,
  pk: RobotOutlined,
  record: DocumentTextOutline,
  rank: CrownOutlined,
}
