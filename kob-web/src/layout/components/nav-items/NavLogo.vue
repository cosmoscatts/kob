<script setup lang="ts">
import { APP_META } from '~/config'
import logo from '~/assets/logo.png'
const { breakpoints } = useResponsive()
const hideTitle = breakpoints.between('sm', 'lg')
</script>

<template>
  <div
    flex-center hfull cursor-pointer
    @click="$router.push('/')"
  >
    <n-avatar
      round
      color="transparent"
      size="small"
      :src="logo"
    />
    <span v-if="!hideTitle" font="bold" pl-16px text-24px>
      {{ APP_META.name }}
    </span>
  </div>
</template>
