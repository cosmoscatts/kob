<script setup lang="ts">
import { appMeta } from '~/config';
import { NavAvatar, NavLogo, NavMenu, NavResponsivePanel } from './nav-items';

const { isMobile } = useResponsive();
const development = isDevelopment;
</script>

<template>
  <div flex-y-center>
    <NavLogo ml6 />
    <NavMenu v-if="!isMobile" mx-5 />
    <div flex-auto />
    <div v-if="!isMobile" flex-y-center mr-10>
      <div i-carbon-debug icon-btn text-lg mx-5 @click="$router.push('/changelog')" />
      <a
        v-if="development"
        icon-btn text-lg i-uil-github-alt mr-5
        :href="appMeta.github"
        target="_blank" title="GitHub"
      />
      <DarkToggle mr-5 />
      <NavAvatar />
    </div>
    <NavResponsivePanel v-if="isMobile" />
  </div>
</template>
