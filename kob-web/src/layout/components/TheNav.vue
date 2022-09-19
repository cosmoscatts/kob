<script setup lang="ts">
import { NavAvatar, NavLogo, NavMenu } from './nav-items'
</script>

<template>
  <div flex-y-center>
    <NavLogo ml-10 />
    <NavMenu mx-5 />
    <div flex-auto />
    <div flex-y-center mr-10>
      <a
        icon-btn text-lg i-carbon-logo-github mx-5
        href="https://github.com/dud9/kob"
        target="_blank" title="GitHub"
      />
      <DarkToggle mr-5 />
      <NavAvatar />
    </div>
  </div>
</template>

