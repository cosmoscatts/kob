<script setup lang="ts">
import defaultAvatar from '~/assets/default-avatar.png';
import Auth from '~/pages/auth/index.vue';
import { createDropdownOptions } from './avatar';

const router = useRouter();
const userStore = useUserStore();
const options = createDropdownOptions(router, userStore);

const loadLottie = () => useTimeoutFn(() => useLottie({
  containerId: '#lottie-cap',
  path: 'https://assets3.lottiefiles.com/packages/lf20_zmIJEx.json',
}), 10);
onMounted(() => {
  if (userStore.isLoggedIn) {
    loadLottie();
  }
});
watch(() => userStore.isLoggedIn, (val) => {
  if (val)
    loadLottie();
});
</script>

<template>
  <n-dropdown v-if="userStore.isLoggedIn" :options="options" trigger="click">
    <div flex-y-center cursor-pointer relative>
      <n-avatar
        round
        size="small"
        :src="userStore.user?.avatar ?? defaultAvatar"
      />
      <div id="lottie-cap" h44px absolute left="[-10px]" />
      <n-ellipsis style="max-width: 100px" ml-3>
        {{ userStore.user?.name || '' }}
      </n-ellipsis>
    </div>
  </n-dropdown>
  <Auth v-else />
</template>
