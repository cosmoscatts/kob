<script setup lang="ts">
import { NavAvatar, NavLogo, NavMenu } from './nav-items'
</script>

<template>
  <div>
    <NavLogo />
    <NavMenu mx-4 />
    <div flex-auto />
    <div flex-y-center>
      <a
        icon-btn text-lg i-carbon-logo-github mx-4
        href="https://github.com/dud9/petite"
        target="_blank" title="GitHub"
      />
      <DarkToggle mr-4 />
      <NavAvatar />
    </div>
  </div>
</template>

