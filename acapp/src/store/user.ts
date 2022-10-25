import type { User } from '~/types'
import defaultAvatar from '~/assets/default-avatar.png'

export const useUserStore = defineStore(
  'userStore',
  () => {
    const user = ref<User>()
    const token = ref<string | null>(null)
    // AcWingOS, 打包后更改为外部传入的 `AcWingOS` 对象
    const acWingOS = 'AcWingOS' as any

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

    function setToken(_token: string) {
      token.value = _token
    }

    return {
      user,
      token,
      acWingOS,
      updateUser,
      removeUser,
      setToken,
    }
  },
)
