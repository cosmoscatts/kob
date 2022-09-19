<script setup lang="ts">
import { appMeta } from '~/config'

const { copyRight } = appMeta
</script>

<template>
  <div flex-center font-bold text-14px>
    {{ copyRight }}
  </div>
</template>

