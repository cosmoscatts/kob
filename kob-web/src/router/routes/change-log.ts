import BaseLayout from '~/layout/default.vue';

export default {
  path: '/changelog',
  component: BaseLayout,
  children: [
    {
      path: '',
      name: 'ChangeLog',
      component: () => import('~/pages/change-log/index.vue'),
      meta: {
        title: '更新日志',
        requiresAuth: false,
      },
    },
  ],
};
