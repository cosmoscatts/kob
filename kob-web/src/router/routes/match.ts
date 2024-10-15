import BaseLayout from '~/layout/default.vue';

export default {
  path: '/match',
  component: BaseLayout,
  children: [
    {
      path: '',
      name: 'Match',
      component: () => import('~/pages/match/index.vue'),
      meta: {
        title: '匹配对战',
        requiresAuth: true,
      },
    },
  ],
};
