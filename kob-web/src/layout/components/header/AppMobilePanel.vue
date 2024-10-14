<script setup lang="ts">
import { appMeta } from '~/config';
import AppAvatar from './AppAvatar.vue';
import AppMenu from './AppMenu.vue';

const userStore = useUserStore();
const { isLoggedIn } = storeToRefs(userStore);

const isMobilePanelVisible = ref(false);
const toggleNavPanel = () => isMobilePanelVisible.value = !isMobilePanelVisible.value;

const mobilePanelBackgroundColor = computed(() => isDark.value ? '#121212' : '#FFFFFF');
</script>

<template>
  <div flex-y-center h-full mr-10>
    <button
      v-if="isLoggedIn"
      icon-btn text-lg
      i-carbon-menu
      @click="toggleNavPanel"
    />
    <AppAvatar v-else />
  </div>

  <n-drawer
    :style="{ backgroundColor: mobilePanelBackgroundColor }"
    :width="240"
    :auto-focus="false"
    :show="isMobilePanelVisible"
    placement="right"
    display-directive="show"
    @mask-click="isMobilePanelVisible = false"
  >
    <div flex-y-center h-50px ml-3>
      <AppAvatar />
      <div flex-auto />
      <a
        icon-btn text-lg i-carbon-logo-github mx-3
        :href="appMeta.github"
        target="_blank" title="GitHub"
      />
      <DarkToggle mr-3 />
    </div>
    <AppMenu mode="vertical" />
  </n-drawer>
</template>
