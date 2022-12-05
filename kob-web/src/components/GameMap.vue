<script setup lang="ts">
import { GameMap } from '~/scripts/map'

const refParentEl = ref<HTMLElement>()
const refCanvas = ref<HTMLCanvasElement>()

const { updateGameMapObject } = usePkStore()
const { updateRecordFinished } = useRecordStore()
let gameMap: GameMap | null = null

function createGameMap() {
  const { value: canvas } = refCanvas
  gameMap?.destory()
  gameMap = new GameMap(canvas!.getContext('2d')!, refParentEl.value!)
  updateGameMapObject(gameMap)
  updateRecordFinished(false)
}

onMounted(createGameMap)

function replayVideo() {
  createGameMap()
}

/**
 * 暂停
 */
function pauseVideo() {
  gameMap?.recordFn?.pause()
}

/**
 * 取消暂停
 */
function resumeVideo() {
  gameMap?.recordFn?.resume()
}

defineExpose({
  replayVideo,
  pauseVideo,
  resumeVideo,
})
</script>

<template>
  <div ref="refParentEl" hw-full flex-center>
    <canvas ref="refCanvas" tabindex="0" />
  </div>
</template>
