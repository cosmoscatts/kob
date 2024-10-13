import defaultAvatar from '~/assets/default-avatar.png';
import type { LoginState, User } from '~/types';
import { Token } from '~/utils';

export const useUserStore = defineStore(
  'userStore',
  () => {
    const user = ref<User>();
    const hasLogin = ref(false); // 是否登录
    const authModalVisible = ref(false); // 是否打开 [登录 / 注册] 模态框

    // setters

    const setUser = (data?: User) => user.value = data;
    const setHasLogin = (state = false) => hasLogin.value = state;
    const setAuthModalVisible = (state: boolean) => authModalVisible.value = state;

    // fn

    const updateUser = () => {
      UserApi
        .getLoginUserInfo()
        .then(({ data = {} }) => {
          if (!data.avatar) {
            data.avatar = defaultAvatar;
          }
          setUser(data);
        });
    };

    const removeUser = () => setUser();

    const login = (token: string) => {
      updateUser();
      Token.set(token);
      setHasLogin(true);
    };

    const logout = () => [setHasLogin, removeUser, Token.remove]
      .forEach(fn => fn());

    /**
     * 判断是否登录 && token 是否过期
     * token 过期需要清空
     * @return
     *  - hasLogin - 已经登录 & token 未过期
     *  - notLogin - 未登录
     *  - expire - token 过期
     */
    const checkLoginState = () => {
      const fns: [boolean, () => LoginState | Promise<LoginState>][] = [
        [!Token.get(), () => {
          hasLogin.value = false;
          return 'notLogin';
        }],
        [hasLogin && !!user.value?.id, () => 'hasLogin'],
        [true, async () => {
          const { code, data = {} } = await UserApi.getLoginUserInfo();
          const inValid = code !== 0 || !Object.keys(data).length;
          hasLogin.value = !inValid;
          if (inValid) {
            logout();
          } else {
            if (!data.avatar)
              data.avatar = defaultAvatar;
            user.value = data;
          }
          return inValid
            ? 'expire'
            : 'hasLogin';
        }],
      ];
      return ConditionalExecutor.executeFirstWithResult<LoginState>(fns) ?? 'notLogin';
    };

    return $$({
      user,
      hasLogin,
      authModalVisible,
      updateUser,
      removeUser,
      login,
      logout,
      checkLoginState,
      setAuthModalVisible,
    });
  },
  { persist: { enabled: true } },
);

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUserStore, import.meta.hot));
}
