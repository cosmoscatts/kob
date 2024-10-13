<script setup lang="ts">
import { appMeta } from '~/config';
import { NavAvatar, NavLogo, NavMenu, NavResponsivePanel } from './nav-items';

const { breakpoints } = useResponsive();

const showMobilePanel = breakpoints.smaller('md');
const showFullNavigation = computed(() => !showMobilePanel.value);
const showExtraButtons = breakpoints.greaterOrEqual('lg');
</script>

<template>
  <div flex-y-center>
    <NavLogo ml6 w-250px />
    <NavMenu v-if="showFullNavigation" mx-5 />
    <div flex-auto />
    <div v-if="showFullNavigation" flex-y-center mr-10>
      <template v-if="showExtraButtons">
        <div i-carbon-debug icon-btn text-lg mx-5 @click="$router.push('/changelog')" />
        <a
          v-if="isDevelopment"
          icon-btn text-lg i-uil-github-alt mr-5
          :href="appMeta.github"
          target="_blank" title="GitHub"
        />
        <DarkToggle mr-5 />
      </template>
      <NavAvatar />
    </div>
    <NavResponsivePanel v-if="showMobilePanel" />
  </div>
</template>
