import type { Component } from 'vue'
import { NIcon } from 'naive-ui'
import {
  LogOutOutline as LogoutIcon,
  PersonCircleOutline as UserIcon,
} from '@vicons/ionicons5'
import type { Router } from 'vue-router'

/**
 * 渲染图标组件
 */
const renderIcon = (icon: Component) => {
  return () => {
    return h(NIcon, null, {
      default: () => h(icon),
    })
  }
}

/**
 * 创建 `dropdown` 选项数据
 */
export function createDropdownOptions(router: Router) {
  const { removeUser } = useUserStore()
  const { notification } = useGlobalNaiveApi()

  return [
    {
      label: '用户资料',
      key: 'profile',
      icon: renderIcon(UserIcon),
      props: {
        onClick: () => {
          router.push('/profile')
        },
      },
    },
    {
      label: '退出登录',
      key: 'logout',
      icon: renderIcon(LogoutIcon),
      props: {
        onClick: () => {
          notification.success({
            title: '登出成功',
            content: '记得回来~',
            duration: 1000,
          })
          useTimeoutFn(removeUser, 500)
        },
      },
    },
  ]
}
