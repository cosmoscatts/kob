import { createRouter, createWebHistory } from 'vue-router';
import routes from './format-routes';
import { createRouterGuard } from './guard';

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
});
createRouterGuard(router);

export default router;
