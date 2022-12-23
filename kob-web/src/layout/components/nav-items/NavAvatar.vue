<script setup lang="ts">
import { createDropdownOptions } from './avatar'
import Auth from '~/pages/auth/index.vue'
import defaultAvatar from '~/assets/default-avatar.png'

const router = useRouter()
const userStore = useUserStore()
const options = createDropdownOptions(router, userStore)
</script>

<template>
  <n-dropdown v-if="userStore.hasLogin" :options="options" trigger="click">
    <div flex-y-center cursor-pointer>
      <n-avatar
        round
        size="small"
        :src="userStore.user?.avatar ?? defaultAvatar"
      />
      <n-ellipsis style="max-width: 100px" ml-3>
        {{ userStore.user?.name || '' }}
      </n-ellipsis>
    </div>
  </n-dropdown>
  <Auth v-else />
</template>
