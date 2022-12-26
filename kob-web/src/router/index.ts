import { createRouter, createWebHashHistory } from 'vue-router'
import { createRouterGuard } from './guard'
import routes from './format-routes'

const router = createRouter({
  history: createWebHashHistory('/'),
  routes: [
    {
      path: '/',
      redirect: '/home',
    },
    ...routes,
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('~/pages/exception/NotFound.vue'),
    },
  ],
  scrollBehavior: () => ({ top: 0 }),
})
createRouterGuard(router)

export default router
