<script setup lang="ts">
import { appMeta } from '~/config';
import AppAvatar from './header/AppAvatar.vue';
import AppLogo from './header/AppLogo.vue';
import AppMenu from './header/AppMenu.vue';
import AppMobilePanel from './header/AppMobilePanel.vue';

const { breakpoints } = useResponsive();

const showMobilePanel = breakpoints.smaller('md');
const showFullNavigation = computed(() => !showMobilePanel.value);
const showExtraButtons = breakpoints.greaterOrEqual('lg');
</script>

<template>
  <div flex-y-center>
    <AppLogo ml6 w-250px />
    <AppMenu v-if="showFullNavigation" mx-5 />
    <div flex-auto />
    <div v-if="showFullNavigation" flex-y-center mr-10>
      <template v-if="showExtraButtons">
        <div i-carbon-debug icon-btn text-lg mx-5 @click="$router.push('/changelog')" />
        <a
          icon-btn text-lg i-uil-github-alt mr-5
          :href="appMeta.github"
          target="_blank" title="GitHub"
        />
        <DarkToggle mr-5 />
      </template>
      <AppAvatar />
    </div>
    <AppMobilePanel v-if="showMobilePanel" />
  </div>
</template>
