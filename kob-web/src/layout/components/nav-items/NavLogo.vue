<script setup lang="ts">
import { APP_META } from '~/config'

const { breakpoints } = useResponsive()
const hideTitle = breakpoints.between('sm', 'lg')
onMounted(() => {
  useTimeoutFn(() => useLottie({
    container: document.querySelector('#lottie-logo')!,
    path: 'https://assets3.lottiefiles.com/packages/lf20_stxjvzmw.json',
  }), 10)
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
