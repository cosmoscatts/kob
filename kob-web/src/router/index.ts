import type { Router } from 'vue-router'
import { createRouter, createWebHashHistory } from 'vue-router'
import { BaseLayout } from '~/layout'
import { appMeta } from '~/config'
import type { LoginState } from '~/types'

const router = createRouter({
  history: createWebHashHistory('/'),
  routes: [
    {
      path: '/',
      redirect: '/home',
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
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('~/pages/exception/NotFound.vue'),
    },
  ],
  scrollBehavior() {
    return { top: 0 }
  },
})

function createRouterGuard(router: Router) {
  const { loadingBar, message } = useGlobalNaiveApi()
  router.beforeEach(async (to, from, next) => {
    loadingBar.start()
    const userStore = useUserStore()
    const needLogin = to.meta.requiresAuth
    if (!needLogin) {
      next()
      return
    }
    const checkLoginState = await userStore.checkLoginState()
    const actions: Record<LoginState, Function> = {
      hasLogin: () => {
        next()
      },
      notLogin: () => {
        message.error('您还未登录！')
        next('/')
        userStore.setAuthModalVisible(true)
      },
      expire: () => {
        message.error('您的登录已过期！')
        next('/')
        userStore.setAuthModalVisible(true)
      },
    }
    actions[checkLoginState]()
  })
  router.afterEach((to) => {
    // 设置 `document title`
    const title = to.meta?.title as string ?? appMeta.appShortName
    useTitle(title)
    // 结束 loadingBar
    loadingBar.finish()
  })
}
createRouterGuard(router)

export default router

