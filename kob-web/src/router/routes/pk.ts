import BaseLayout from '~/layout/default.vue';

export default {
  path: '/pk',
  component: BaseLayout,
  children: [
    {
      path: '',
      name: 'PK',
      component: () => import('~/pages/pk/index.vue'),
      meta: {
        title: 'PK 对战',
        requiresAuth: true,
      },
    },
    {
      path: 'match',
      name: 'Match',
      component: () => import('~/pages/match/index.vue'),
      meta: {
        title: '匹配对战',
        requiresAuth: true,
      },
    },
    {
      path: 'machine',
      name: 'Machine',
      component: () => import('~/pages/machine/index.vue'),
      meta: {
        title: '人机对战',
        requiresAuth: true,
      },
    },
  ],
};
