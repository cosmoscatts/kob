import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  history: createWebHashHistory('/'),
  routes: [
    {
      path: '/',
      component: '',
    },
    {
      path: '/login',
      component: BLANK_LAYOUT,
      children: [
        {
          path: '',
          name: 'Login',
          component: () => import('~/pages/login/index.vue'),
          meta: {
            requiresAuth: false,
          },
        },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      component: BLANK_LAYOUT,
      children: [
        {
          path: '',
          name: 'NotFound',
          component: () => import('~/pages/exception/not-found/index.vue'),
        },
      ],
    },
  ],
  scrollBehavior() {
    return { top: 0 }
  },
})

export default router

