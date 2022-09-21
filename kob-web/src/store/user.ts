import type { User } from '~/types'
import defaultAvatar from '~/assets/avatar.jpg'

export const useUserStore = defineStore(
  'userStore',
  () => {
    const user = ref<User>()
    // 是否打开登录 / 注册 `Modal`
    const authModalVisible = ref(false)

    function updateUser(_user: User) {
      if (!_user.avatar)
        _user.avatar = defaultAvatar
      user.value = _user
    }

    function removeUser() {
      user.value = undefined
    }

    function setAuthModalVisible(value: boolean) {
      authModalVisible.value = value
    }

    return {
      user,
      authModalVisible,
      updateUser,
      removeUser,
      setAuthModalVisible,
    }
  },
  {
    persist: {
      enabled: true,
    },
  },
)
