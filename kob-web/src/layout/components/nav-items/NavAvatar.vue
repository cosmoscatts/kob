<script setup lang="ts">
import { createDropdownOptions } from './avatar'
import Auth from '~/pages/auth/index.vue'

const router = useRouter()

const { user, hasLogin } = storeToRefs(useUserStore())

const options = createDropdownOptions(router)

const avatar = computed(() => {
  return user.value?.avatar
})
</script>

<template>
  <n-dropdown v-if="hasLogin" :options="options" trigger="click">
    <div flex-y-center cursor-pointer>
      <n-avatar
        round
        size="small"
        :src="avatar"
        fallback-src="https://07akioni.oss-cn-beijing.aliyuncs.com/07akioni.jpeg"
      />
      <n-ellipsis style="max-width: 100px" ml-3>
        {{ user?.name || '' }}
      </n-ellipsis>
    </div>
  </n-dropdown>

  <Auth v-else />
</template>

