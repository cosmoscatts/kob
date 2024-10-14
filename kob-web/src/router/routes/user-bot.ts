import BaseLayout from '~/layout/default.vue';

export default {
  path: '/userBot',
  component: BaseLayout,
  children: [
    {
      path: '',
      name: 'UserBot',
      component: () => import('~/pages/user-bot/index.vue'),
      meta: {
        title: '我的Bot',
        requiresAuth: true,
      },
    },
  ],
};
