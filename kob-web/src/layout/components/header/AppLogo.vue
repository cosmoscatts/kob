<script setup lang="ts">
import logo from '~/assets/logo.png';
import { appMeta } from '~/config';

const { breakpoints } = useResponsive();
const hideTitle = breakpoints.between('sm', 'xl');
</script>

<template>
  <div
    flex-center hfull cursor-pointer min-w-100px
    @click="$router.push('/')"
  >
    <n-avatar
      round
      color="transparent"
      size="small"
      :src="logo"
    />
    <span v-if="!hideTitle" font="bold" pl-16px text-24px>
      {{ appMeta.name }}
    </span>
  </div>
</template>
