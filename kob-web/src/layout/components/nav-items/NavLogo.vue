<script setup lang="ts">
import { breakpointsTailwind } from '@vueuse/core'
import { appMeta } from '~/config'

const { appName } = appMeta

// 是否为移动端（包含 `PC` 端宽度过小的情况）
const breakpoints = useBreakpoints(breakpointsTailwind)
const hideTitle = breakpoints.between('sm', 'lg')
</script>

<template>
  <div
    flex-center h-full cursor-pointer @click="$router.push('/')"
  >
    <div i-ri-game-fill text="primary 24px" />
    <span v-if="!hideTitle" font="bold sans" pl-16px text-16px>
      {{ appName }}
    </span>
  </div>
</template>

