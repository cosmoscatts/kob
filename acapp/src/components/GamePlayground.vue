<script setup lang="ts">
import type { Ref } from 'vue'

const containerWidth = inject<Ref<number>>('containerWidth')!
</script>

<template>
  <div
    :style="{
      width: containerWidth < 768 ? '96%' : '60%',
      height: '65%',
      marginLeft: containerWidth < 768 ? '2%' : '20%',
    }"
  >
    <GameMap />
  </div>
</template>
