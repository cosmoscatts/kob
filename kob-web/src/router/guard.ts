import type { Router } from 'vue-router';
import { APP_META } from '~/config';

export function createRouterGuard(router: Router): void {
  router.beforeEach(async (to, _from, next) => {
    $loadingBar.start();
    const userStore = useUserStore();
    const needLogin = to.meta.requiresAuth;
    const state = needLogin
      ? userStore.checkLoginState()
      : 'noNeedLogin';
    const fns: [boolean, Function][] = [
      [['noNeedLogin', 'hasLogin'].includes(state), () => next()],
      [state === 'notLogin', () => {
        $message.error('您还未登录！');
        userStore.setAuthModalVisible(true);
        next('/home');
      }],
      [state === 'expire', () => {
        $message.error('您的登录已过期！');
        userStore.setAuthModalVisible(true);
        next('/home');
      }],
    ];
    Conditional.some(fns);
  });
  router.afterEach((to, from, failure) => {
    useTitle(to.meta?.title as string ?? APP_META.shortName);
    if (to.path !== from.path && failure)
      $loadingBar.error();
    else $loadingBar.finish();
  });
}
