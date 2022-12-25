import type { Component } from 'vue'
import { RobotOutlined } from '@vicons/antd'
import {
  ChatbubblesOutline,
  DocumentTextOutline,
  HomeOutline,
  MedalOutline,
} from '@vicons/ionicons5'
import type { Menu } from '~/types'

export const APP_MENUS: Menu[] = [
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
  {
    id: 105,
    label: '讨论区',
    path: '/discuss',
    icon: 'discuss',
  },
]

export const APP_MENU_ICON_MAP: Record<string, Component> = {
  home: HomeOutline,
  pk: RobotOutlined,
  record: DocumentTextOutline,
  rank: MedalOutline,
  discuss: ChatbubblesOutline,
}
