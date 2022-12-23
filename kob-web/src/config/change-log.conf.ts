import type { ChangeLog } from '~/types'

export const APP_CHANGE_LOG: ChangeLog[] = [
  {
    date: 'IDEAS 💡',
    title: '🧐 一些想法...',
    description: '大概率只是想想 😅',
    changes: [
      {
        id: 'IDEA-1',
        type: 'add',
        title: '全局大厅聊天系统',
        content: '',
      },
      {
        id: 'IDEA-2',
        type: 'add',
        title: '通过链接加入匹配对战',
        content: '',
      },
      {
        id: 'IDEA-3',
        type: 'add',
        title: '讨论区多人嵌套回复',
        content: '',
      },
    ],
  },
  {
    date: '22/12/23',
    title: '💎 代码重构',
    description: '',
    changes: [
      {
        id: '22/12/23/1',
        type: 'update',
        title: '重构了 Web 端大部分代码',
        content: '',
      },
    ],
  },
  {
    date: '22/12/05',
    title: '💎 功能优化 & 代码优化',
    description: '这个问题本地一直不能复现，我只能尽可能地做一些优化',
    changes: [
      {
        id: '22/12/05/1',
        type: 'update',
        title: '对战时前端偶尔出现错位的问题',
        content: '',
      },
      {
        id: '22/12/05/2',
        type: 'update',
        title: '重构 acapp 游戏逻辑代码',
        content: '',
      },
    ],
  },
  {
    date: '22/12/04',
    title: '💎 功能优化 & 代码优化',
    description: '页面展示优化，重构了部分代码',
    changes: [
      {
        id: '22/12/04/1',
        type: 'update',
        title: '优化匹配成功页面的动画展示',
        content: '',
      },
      {
        id: '22/12/04/2',
        type: 'update',
        title: '代码重构',
        content: '',
      },
    ],
  },
  {
    date: '22/11/25',
    title: '🐛 问题修复',
    description: '',
    changes: [
      {
        id: '22/11/25/1',
        type: 'fix',
        title: '回放时，如果用户名太长，对局详情看板会错位的问题',
        content: '',
      },
    ],
  },
  {
    date: '22/11/18',
    title: '😋 一锅烩',
    description: '新增功能 & 页面样式优化',
    changes: [
      {
        id: '22/11/18/1',
        type: 'add',
        title: 'Bot代码示例',
        content: '',
      },
      {
        id: '22/11/18/2',
        type: 'update',
        title: '匹配成功界面的背景优化，增加阴影',
        content: '',
      },
      {
        id: '22/11/18/3',
        type: 'update',
        title: '对局列表页面表格行增加每个对局的胜者标识',
        content: '',
      },
    ],
  },
  {
    date: '22/11/17',
    title: '😋 一锅烩',
    description: '新增功能 & 页面样式优化',
    changes: [
      {
        id: '22/11/17/1',
        type: 'add',
        title: '讨论区功能，可以留下对项目的意见',
        content: '只能简单的添加意见，还没有实现多人回复，嵌套回复等功能',
      },
      {
        id: '22/11/17/2',
        type: 'update',
        title: '优化地图样式',
        content: '墙，障碍物，蛇死亡等配色优化',
      },
    ],
  },
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
        title: '重新回放 / 暂停回放 / 取消暂停',
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
