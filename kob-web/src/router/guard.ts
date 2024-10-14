import type { NavigationGuardNext, RouteLocationNormalized, Router } from 'vue-router';
import { appMeta } from '~/config';
import type { LoginState } from '~/types';

export function createRouterGuard(router: Router): void {
  router.beforeEach(async (to: RouteLocationNormalized, _from: RouteLocationNormalized, next: NavigationGuardNext) => {
    $loadingBar.start();
    const userStore = useUserStore();
    const needLogin = to.meta.requiresAuth as boolean;

    let state: LoginState;
    if (needLogin) {
      state = await userStore.checkLoginState() as LoginState;
    } else {
      state = 'authNotRequired';
    }

    const fns: [boolean, () => void][] = [
      [['authNotRequired', 'authenticated'].includes(state), () => next()],
      [state === 'unauthenticated', () => {
        $message.error('您还未登录！');
        userStore.setAuthModalVisibility(true);
        next('/home');
      }],
      [state === 'tokenExpired', () => {
        $message.error('您的登录已过期！');
        userStore.setAuthModalVisibility(true);
        next('/home');
      }],
    ];

    ConditionalExecutor.executeFirst(fns);
  });

  router.afterEach((to: RouteLocationNormalized, _from: RouteLocationNormalized, failure: any) => {
    useTitle(to.meta?.title as string ?? appMeta.shortName);
    failure ? $loadingBar.error() : $loadingBar.finish();
  });
}
