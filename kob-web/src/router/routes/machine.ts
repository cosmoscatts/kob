import BaseLayout from '~/layout/default.vue';

export default {
  path: '/machine',
  component: BaseLayout,
  children: [
    {
      path: '',
      name: 'Machine',
      component: () => import('~/pages/machine/index.vue'),
      meta: {
        title: '人机对战',
        requiresAuth: true,
      },
    },
  ],
};
