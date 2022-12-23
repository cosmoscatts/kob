<script setup lang="ts">
import { breakpointsTailwind } from '@vueuse/core'
import { APP_META } from '~/config'

// 是否为移动端（包含 PC 端宽度过小的情况）
const breakpoints = useBreakpoints(breakpointsTailwind)
const hideTitle = breakpoints.between('sm', 'lg')

onMounted(() => {
  useTimeoutFn(() => {
    useLottie({
      container: document.querySelector('#lottie-logo')!,
      path: 'https://assets10.lottiefiles.com/packages/lf20_i9arxzcg.json',
    })
  }, 10)
})
</script>

<template>
  <div
    flex-center h-full cursor-pointer
    @click="$router.push('/')"
  >
    <div id="lottie-logo" h44px />
    <span v-if="!hideTitle" font="bold" pl-16px text-24px>
      {{ APP_META.name }}
    </span>
  </div>
</template>
