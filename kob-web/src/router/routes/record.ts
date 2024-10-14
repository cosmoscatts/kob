import BaseLayout from '~/layout/default.vue';

export default {
  path: '/record',
  component: BaseLayout,
  children: [
    {
      path: '',
      name: 'Record',
      component: () => import('~/pages/record/index.vue'),
      meta: {
        title: '对局记录',
        requiresAuth: true,
      },
    },
  ],
};
