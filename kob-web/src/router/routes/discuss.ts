import BaseLayout from '~/layout/default.vue';

export default {
  path: '/discuss',
  component: BaseLayout,
  children: [
    {
      path: '',
      name: 'Discuss',
      component: () => import('~/pages/discuss/index.vue'),
      meta: {
        title: '讨论区',
        requiresAuth: true,
      },
    },
  ],
};
