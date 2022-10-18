<script setup lang="ts">
import { GameMap } from '~/logic'

const refParentEl = ref<HTMLElement>()
const refCanvas = ref<HTMLCanvasElement>()

const { updateGameMapObject } = usePkStore()

function createGameMap() {
  const { value: canvas } = refCanvas
  updateGameMapObject(new GameMap(canvas!.getContext('2d')!, refParentEl.value!))
}

onMounted(createGameMap)

function replayVideo() {
  createGameMap()
}

defineExpose({
  replayVideo,
})
</script>

<template>
  <div ref="refParentEl" hw-full flex-center>
    <canvas ref="refCanvas" tabindex="0" />
  </div>
</template>
