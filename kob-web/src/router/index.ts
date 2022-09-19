import { createRouter, createWebHashHistory } from 'vue-router'
import { BaseLayout } from '~/layout'

const router = createRouter({
  history: createWebHashHistory('/'),
  routes: [
    {
      path: '/',
      redirect: '/pk',
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('~/pages/login/index.vue'),
      meta: {
        requiresAuth: false,
      },
    },
    {
      path: '/pk',
      component: BaseLayout,
      children: [
        {
          path: '',
          name: 'PK',
          component: () => import('~/pages/pk/index.vue'),
          meta: {
            requiresAuth: true,
          },
        },
      ],
    },
    {
      path: '/record',
      component: BaseLayout,
      children: [
        {
          path: '',
          name: 'Record',
          component: () => import('~/pages/record/index.vue'),
          meta: {
            requiresAuth: true,
          },
        },
      ],
    },
    {
      path: '/rank',
      component: BaseLayout,
      children: [
        {
          path: '',
          name: 'Rank',
          component: () => import('~/pages/rank/index.vue'),
          meta: {
            requiresAuth: true,
          },
        },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('~/pages/exception/not-found/index.vue'),
    },
  ],
  scrollBehavior() {
    return { top: 0 }
  },
})

export default router

