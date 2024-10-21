import type { DropdownMixedOption } from 'naive-ui/es/dropdown/src/interface';
import type { Component } from 'vue';
import type { Router } from 'vue-router';
import { RobotOutlined as BotIcon } from '@vicons/antd';
import {
  LogOutOutline as LogoutIcon,
  PersonCircleOutline as UserIcon,
} from '@vicons/ionicons5';
import { NIcon } from 'naive-ui';

const renderIcon = (icon: Component) => () => h(NIcon, null, { default: () => h(icon) });

export const createDropdownOptions = (router: Router): DropdownMixedOption[] => {
  const userStore = useUserStore();

  const navigateTo = (path: string) => () => router.push(path);

  const logout = () => {
    $notification.success({
      title: '登出成功',
      content: '记得回来~',
      duration: 1000,
    });
    router.push('/home');
    useTimeoutFn(userStore.logout, 500);
  };

  return [
    {
      label: '我的Bot',
      key: 'userBot',
      icon: renderIcon(BotIcon),
      props: { onClick: navigateTo('/userBot') },
    },
    {
      label: '个人中心',
      key: 'profile',
      icon: renderIcon(UserIcon),
      props: { onClick: navigateTo('/profile') },
    },
    {
      label: '退出登录',
      key: 'logout',
      icon: renderIcon(LogoutIcon),
      props: { onClick: logout },
    },
  ];
};
