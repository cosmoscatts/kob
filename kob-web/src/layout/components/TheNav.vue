<script setup lang="ts">
import { breakpointsTailwind } from '@vueuse/core'
import { NavAvatar, NavLogo, NavMenu, NavResponsivePanel } from './nav-items'

// 是否为移动端（包含 `PC` 端宽度过小的情况）
const breakpoints = useBreakpoints(breakpointsTailwind)
const isMobile = breakpoints.smaller('sm')
</script>

<template>
  <div flex-y-center>
    <NavLogo ml-10 />
    <NavMenu v-if="!isMobile" mx-5 />
    <div flex-auto />
    <div v-if="!isMobile" flex-y-center mr-10>
      <a
        icon-btn text-lg i-carbon-logo-github mx-5
        href="https://github.com/dud9/kob"
        target="_blank" title="GitHub"
      />
      <DarkToggle mr-5 />
      <NavAvatar />
    </div>
    <NavResponsivePanel v-if="isMobile" />
  </div>
</template>

