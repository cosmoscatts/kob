<script setup lang="ts">
import { GameMap } from '~/logic'

const refParentEl = ref<HTMLElement>()
const refCanvas = ref<HTMLCanvasElement>()

const { updateGameMapObject } = usePkStore()

onMounted(() => {
  const { value: canvas } = refCanvas

  updateGameMapObject(new GameMap(canvas!.getContext('2d')!, refParentEl.value!))
})
</script>

<template>
  <div ref="refParentEl" hw-full flex-center>
    <canvas ref="refCanvas" tabindex="0" />
  </div>
</template>
