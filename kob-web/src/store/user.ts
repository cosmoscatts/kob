import defaultAvatar from '~/assets/default-avatar.png';
import type { LoginState, User } from '~/types';
import { tokenStorage } from '~/utils/token-storage';

export const useUserStore = defineStore('userStore', () => {
  const state = reactive({
    user: null as User | null,
    isLoggedIn: false,
    isAuthModalVisible: false,
  });

  const setUser = (userData?: User | null) => {
    state.user = userData ?? null;
    state.isLoggedIn = !!userData;
  };

  const setAuthModalVisibility = (isVisible: boolean) => {
    state.isAuthModalVisible = isVisible;
  };

  const fetchAndUpdateUser = async () => {
    try {
      const result = await UserApi.getLoginUserInfo();
      const { data = {} } = result.data;
      setUser({
        ...data,
        avatar: data.avatar || defaultAvatar,
      });
    } catch (error) {
      console.error('获取用户信息失败：', error);
      setUser(null);
    }
  };

  const login = async (token: string) => {
    tokenStorage.set(token);
    await fetchAndUpdateUser();
  };

  const logout = () => {
    setUser(null);
    tokenStorage.remove();
  };

  const checkLoginState = async (): Promise<LoginState> => {
    const token = tokenStorage.get();
    if (!token)
      return 'unauthenticated';
    if (state.isLoggedIn && state.user?.id)
      return 'authenticated';

    try {
      await fetchAndUpdateUser();
      return state.isLoggedIn ? 'authenticated' : 'tokenExpired';
    } catch (error) {
      console.error('Error checking login state:', error);
      logout();
      return 'tokenExpired';
    }
  };

  return {
    ...toRefs(state),
    login,
    logout,
    checkLoginState,
    setAuthModalVisibility,
  };
}, { persist: { enabled: true } });

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUserStore, import.meta.hot));
}
