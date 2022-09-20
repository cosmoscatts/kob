<script setup lang="ts">
import GamePlayground from '~/components/GamePlayground.vue'
import { appLayout } from '~/config'

const { navHeight, footHeight, contentPadding } = appLayout

const diffHeight = computed(() => {
  return navHeight + footHeight + contentPadding * 2 + 1 + 1 + 3
})
</script>

<template>
  <div :style="{ minHeight: `calc(100vh - ${diffHeight}px)` }" flex-center>
    <GamePlayground />
  </div>
</template>
