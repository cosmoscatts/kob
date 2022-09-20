import type { Router } from 'vue-router'
import { createRouter, createWebHashHistory } from 'vue-router'
import { BaseLayout } from '~/layout'
import { appMeta } from '~/config'

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
      component: () => import('~/pages/exception/not-found/index.vue'),
    },
  ],
  scrollBehavior() {
    return { top: 0 }
  },
})

function createRouterGuard(router: Router) {
  const { loadingBar } = useGlobalNaiveApi()
  router.beforeEach(() => {
    loadingBar.start()
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

