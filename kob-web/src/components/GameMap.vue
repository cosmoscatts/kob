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
  <div ref="refParentEl" hw-full flex-center>
    <canvas ref="refCanvas" tabindex="0" />
  </div>
</template>
