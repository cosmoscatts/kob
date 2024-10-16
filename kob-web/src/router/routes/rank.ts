import BaseLayout from '~/layout/default.vue';

export default {
  path: '/rank',
  component: BaseLayout,
  children: [
    {
      path: '',
      name: 'Rank',
      component: () => import('~/pages/rank/index.vue'),
      meta: {
        title: '排行榜',
        requiresAuth: true,
      },
    },
  ],
};
