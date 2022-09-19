<script setup lang="ts">
import { appMeta } from '~/config'

const { appName } = appMeta
</script>

<template>
  <div
    flex-center h-full
  >
    <div i-ri-vip-crown-2-fill text="primary 24px" />
    <span font="bold sans" pl-16px text-16px>
      {{ appName }}
    </span>
  </div>
</template>

