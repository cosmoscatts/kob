import type { LoginState, User } from '~/types'
import defaultAvatar from '~/assets/default-avatar.png'
import { getToken, removeToken, setToken } from '~/utils'

export const useUserStore = defineStore(
  'userStore',
  () => {
    const user = ref<User>()
    // 是否登录
    const hasLogin = ref(false)
    // 是否打开登录 / 注册 `Modal`
    const authModalVisible = ref(false)

    /**
     * 判断是否登录 && `token` 是否过期
     * `token` 过期需要清空
     * @return
     *  - `hasLogin` - 已经登录 & `token` 未过期
     *  - `notLogin` - 未登录
     *  - `expire` - `token `过期
     */
    async function checkLoginState(): Promise<LoginState> {
      const token = getToken()
      if (!token) {
        hasLogin.value = false
        return 'notLogin'
      }

      if (hasLogin.value && user.value?.id)
        return 'hasLogin'

      const { code, data } = await UserApi.getLoginUserInfo()
      const validation = code !== 0 || !data
      hasLogin.value = !validation
      if (validation) { logout() }
      else {
        if (!data.avatar)
          data.avatar = defaultAvatar
        user.value = data
      }
      return validation
        ? 'expire'
        : 'hasLogin'
    }

    async function updateUser() {
      const { code, data } = await UserApi.getLoginUserInfo()
      if (code !== 0 || !data)
        return
      if (!data.avatar)
        data.avatar = defaultAvatar
      user.value = data
    }

    function removeUser() {
      user.value = undefined
    }

    function setAuthModalVisible(value: boolean) {
      authModalVisible.value = value
    }

    function login(token: string) {
      hasLogin.value = true
      updateUser()
      setToken(token)
    }

    function logout() {
      hasLogin.value = false
      removeUser()
      removeToken()
    }

    return {
      user,
      hasLogin,
      authModalVisible,
      updateUser,
      removeUser,
      login,
      logout,
      checkLoginState,
      setAuthModalVisible,
    }
  },
  {
    persist: {
      enabled: true,
    },
  },
)
