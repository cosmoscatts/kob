import { createRouter, createWebHashHistory } from 'vue-router'
import { createRouterGuard } from './guard'
import { BaseLayout } from '~/layout'

const router = createRouter({
  history: createWebHashHistory('/'),
  routes: [
    {
      path: '/',
      redirect: '/home',
    },
    {
      path: '/home',
      component: BaseLayout,
      children: [
        {
          path: '',
          name: 'Home',
          component: () => import('~/pages/home/index.vue'),
          meta: {
            title: '首页',
            requiresAuth: false,
          },
        },
      ],
    },
    {
      path: '/changelog',
      component: BaseLayout,
      children: [
        {
          path: '',
          name: 'ChangeLog',
          component: () => import('~/pages/change-log/index.vue'),
          meta: {
            title: '更新日志',
            requiresAuth: false,
          },
        },
      ],
    },
    {
      path: '/userBot',
      component: BaseLayout,
      children: [
        {
          path: '',
          name: 'UserBot',
          component: () => import('~/pages/user-bot/index.vue'),
          meta: {
            title: '我的Bot',
            requiresAuth: true,
          },
        },
      ],
    },
    {
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
            title: 'PK 对战',
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
            title: '对局记录',
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
            title: '排行榜',
            requiresAuth: true,
          },
        },
      ],
    },
    {
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
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('~/pages/exception/NotFound.vue'),
    },
  ],
  scrollBehavior() {
    return { top: 0 }
  },
})

createRouterGuard(router)

export default router
