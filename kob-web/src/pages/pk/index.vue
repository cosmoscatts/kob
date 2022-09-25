<script setup lang="ts">
import { appLayout } from '~/config'

const { navHeight, footHeight, contentPadding } = appLayout

const diffHeight = computed(() => {
  return navHeight + footHeight + contentPadding * 2 + 1 + 1 + 3
})

const { status } = storeToRefs(usePkStore())
</script>

<template>
  <div :style="{ minHeight: `calc(100vh - ${diffHeight}px)` }" flex-center>
    <GameMatchGround v-if="status === 'match'" />
    <GamePlayground v-else />
  </div>
</template>
