import type { Router } from 'vue-router'
import { createRouter, createWebHashHistory } from 'vue-router'
import { BaseLayout } from '~/layout'
import { APP_META } from '~/config'

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

function createRouterGuard(router: Router): void {
  router.beforeEach(async (to, _from, next) => {
    $loadingBar.start()
    const userStore = useUserStore()
    const needLogin = to.meta.requiresAuth
    const state = needLogin
      ? userStore.checkLoginState()
      : 'noNeedLogin'
    const fns: [boolean, Function][] = [
      [['noNeedLogin', 'hasLogin'].includes(state), () => next()],
      [state === 'notLogin', () => {
        $message.error('您还未登录！')
        userStore.setAuthModalVisible(true)
        next('/home')
      }],
      [state === 'expire', () => {
        $message.error('您的登录已过期！')
        userStore.setAuthModalVisible(true)
        next('/home')
      }],
    ]
    Conditional.some(fns)
  })
  router.afterEach((to, from, failure) => {
    useTitle(to.meta?.title as string ?? APP_META.shortName)
    if (to.path !== from.path && failure) $loadingBar.error()
    else $loadingBar.finish()
  })
}
createRouterGuard(router)

export default router
