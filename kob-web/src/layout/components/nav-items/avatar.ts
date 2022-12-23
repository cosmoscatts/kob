import type { Component } from 'vue'
import { NIcon } from 'naive-ui'
import { RobotOutlined as BotIcon } from '@vicons/antd'
import {
  LogOutOutline as LogoutIcon,
  PersonCircleOutline as UserIcon,
} from '@vicons/ionicons5'
import type { Router } from 'vue-router'

const renderIcon = (icon: Component) => () => h(NIcon, null, {
  default: () => h(icon),
})

export const createDropdownOptions = (router: Router, userStore = useUserStore()) => ([
  {
    label: '我的Bot',
    key: 'userBot',
    icon: renderIcon(BotIcon),
    props: {
      onClick: () => router.push('/userBot'),
    },
  },
  {
    label: '个人中心',
    key: 'profile',
    icon: renderIcon(UserIcon),
    props: {
      onClick: () => router.push('/profile'),
    },
  },
  {
    label: '退出登录',
    key: 'logout',
    icon: renderIcon(LogoutIcon),
    props: {
      onClick: () => {
        $notification.success({
          title: '登出成功',
          content: '记得回来~',
          duration: 1000,
        })
        router.push('/home')
        useTimeoutFn(userStore.logout, 500)
      },
    },
  },
])
