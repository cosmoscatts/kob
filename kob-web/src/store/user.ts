import type { User } from '~/types'
import defaultAvatar from '~/assets/avatar.jpg'

export const useUserStore = defineStore(
  'userStore',
  () => {
    const user = ref<User>()

    // user.value = {
    //   id: 1,
    //   name: 'duende',
    //   avatar: defaultAvatar,
    // }

    function updateUser(_user: User) {
      if (!_user.avatar)
        _user.avatar = defaultAvatar
      user.value = _user
    }

    function removeUser() {
      user.value = undefined
    }

    return {
      user,
      updateUser,
      removeUser,
    }
  },
  {
    persist: {
      enabled: true,
    },
  },
)
