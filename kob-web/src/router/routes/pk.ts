import { BaseLayout } from '~/layout';

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
  ],
};
