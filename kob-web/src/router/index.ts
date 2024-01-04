import { createRouter, createWebHistory } from 'vue-router'
import { createRouterGuard } from './guard'
import routes from './format-routes'

const router = createRouter({
  history: createWebHistory('/'),
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
