import type { Component } from 'vue'
import { RobotOutlined } from '@vicons/antd'
import { DocumentTextOutline, HomeOutline, MedalOutline } from '@vicons/ionicons5'
import type { ChangeLog, Menu } from '~/types'

/** 是否为 `debug` 模式 */
export const debug = import.meta.env.MODE === 'development'

/** 存储 `token` 的键 */
export const TOKEN_KEY = 'jwt_token'

/**
 * 项目基本信息
 */
export const appMeta = {
  appShortName: 'KOB',
  appName: '阿伟你又在玩蛇',
  description: '玩蛇两年半的练习生',
  author: 'Cosmoscatts',
  github: 'https://github.com/cosmoscatts/kob',
  copyRight: 'KING OF BOTS © COSMOSCATTS',
  lastUpdate: '2022/11/16',
}

/**
 * 项目更新日志
 */
export const appChangeLog: ChangeLog[] = [
  {
    date: '22/11/16',
    title: '🆕 新增功能',
    description: '',
    changes: [
      {
        id: '22/11/16/1',
        type: 'add',
        title: '添加了人机试炼功能',
        content: '整个pk可分为『匹配对战』和『人机试炼』',
      },
    ],
  },
  {
    date: '22/11/12',
    title: '💎 功能优化',
    description: '优化了部分页面的用户体验',
    changes: [
      {
        id: '22/11/12/1',
        type: 'update',
        title: '匹配中增加提示框',
        content: '',
      },
      {
        id: '22/11/12/2',
        type: 'update',
        title: '匹配成功的展示优化',
        content: '在原有匹配成功页面的基础上增加了svg动画',
      },
    ],
  },
  {
    date: '22/11/10',
    title: '😋 一锅烩',
    description: '新增功能 & 修复 Bug',
    changes: [
      {
        id: '22/11/10/1',
        type: 'add',
        title: '重新回放/ 暂停回放 / 取消暂停',
        content: '在录像回放时，可以选则暂停，暂停后可以重新开始，整个录像播放完成后，可以重新观看',
      },
      {
        id: '22/11/10/2',
        type: 'fix',
        title: '修复录像播放时，网页端离开页面再返回，录像会错乱的问题',
        content: '并没有完全修复，只是重新返回时，设置重新从头播放',
      },
    ],
  },
  {
    date: '22/11/09',
    title: '🆕 新增功能',
    description: '',
    changes: [
      {
        id: '22/11/09/1',
        type: 'add',
        title: '排行榜、历史对局根据玩家名称搜索',
        content: '',
      },
      {
        id: '22/11/09/2',
        type: 'add',
        title: '排行榜前10名增加奖杯图标',
        content: '',
      },
    ],
  },
  {
    date: '22/11/08',
    title: '💎 功能优化',
    description: '',
    changes: [
      {
        id: '22/11/08/1',
        type: 'update',
        title: '页面布局优化，更换 logo，动画优化',
        content: '',
      },
    ],
  },
  {
    date: '22/11/04',
    title: '🐛 问题修复',
    description: '',
    changes: [
      {
        id: '22/11/04/1',
        type: 'fix',
        title: 'acapp 端 Message 提示被应用遮挡的问题',
        content: '',
      },
    ],
  },
  {
    date: '22/10/31',
    title: '😋 一锅烩',
    description: '新增功能 & 功能优化',
    changes: [
      {
        id: '22/10/31/1',
        type: 'add',
        title: '匹配成功的动画',
        content: '双方玩家匹配成功后，会进入倒计时页面，3，2，1，Fight！',
      },
      {
        id: '22/10/31/2',
        type: 'update',
        title: 'acapp应用高度过小时，优化表单的显示',
        content: '',
      },
    ],
  },
  {
    date: '22/10/30',
    title: '🐛 问题修复',
    description: '',
    changes: [
      {
        id: '22/10/30/1',
        type: 'fix',
        title: 'acapp 端下拉框被遮挡问题',
        content: '',
      },
      {
        id: '22/10/30/2',
        type: 'fix',
        title: 'acapp 端无法连接 websocket 问题',
        content: '',
      },
    ],
  },
]

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
  rank: MedalOutline,
}
