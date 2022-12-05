<script setup lang="ts">
import { GameMap } from '~/scripts/map'

const refParentEl = ref<HTMLElement>()
const refCanvas = ref<HTMLCanvasElement>()

const pkStore = usePkStore()
const recordStore = useRecordStore()
let gameMap: GameMap | null = null

const sendStartGameSingal = () => {
  if (!pkStore.socket)
    return
  pkStore.socket.send(JSON.stringify({
    event: 'start-game',
  }))
}

function createGameMap() {
  const { value: canvas } = refCanvas
  gameMap?.destory()
  gameMap = new GameMap(canvas!.getContext('2d')!, refParentEl.value!)
  pkStore.updateGameMapObject(gameMap)
  if (recordStore.isRecord)
    recordStore.updateRecordFinished(false)
  else
    sendStartGameSingal()
}

onMounted(createGameMap)

const replayVideo = () => createGameMap()

/**
 * 暂停
 */
const pauseVideo = () => gameMap?.recordFn?.pause()

/**
 * 取消暂停
 */
const resumeVideo = () => gameMap?.recordFn?.resume()

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
