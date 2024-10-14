import BaseLayout from '~/layout/default.vue';

export default {
  path: '/profile',
  component: BaseLayout,
  children: [
    {
      path: '',
      name: 'Profile',
      component: () => import('~/pages/profile/index.vue'),
      meta: {
        title: '个人中心',
        requiresAuth: true,
      },
    },
  ],
};
