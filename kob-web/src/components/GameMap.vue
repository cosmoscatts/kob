<script setup lang="ts">
import { GameMap } from '~/logic'

const refParentEl = ref<HTMLElement>()
const refCanvas = ref<HTMLCanvasElement>()

onMounted(() => {
  const { value: canvas } = refCanvas
  // eslint-disable-next-line no-new
  new GameMap(canvas!.getContext('2d')!, refParentEl.value!)
})
</script>

<template>
  <div ref="refParentEl">
    <canvas ref="refCanvas" />
  </div>
</template>
