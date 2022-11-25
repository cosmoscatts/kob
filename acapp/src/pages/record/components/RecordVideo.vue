<script setup lang="ts">
import lottie from 'lottie-web'
import type { Ref } from 'vue'
import type { PlayerInfo } from '../helper'

const {
  playerInfoList = [],
} = defineProps<{
  playerInfoList: PlayerInfo[]
}>()

const containerWidth = inject<Ref<number>>('containerWidth')!
const changeCurrentTab = inject<Function>('changeCurrentTab')!

const refGameMap = ref()

const { clearVideo } = useRecordStore()
const { loser, recordFinished } = storeToRefs(useRecordStore())

function goBack() {
  clearVideo()
  refGameMap.value?.pauseVideo?.()
  changeCurrentTab(0, {})
}

function replay() {
  refGameMap.value?.replayVideo?.()
}

let recordPaused = $ref(false)
function pause() {
  recordPaused = !recordPaused
  if (recordPaused)
    refGameMap.value?.pauseVideo?.()
  else
    refGameMap.value?.resumeVideo?.()
}

onMounted(() => {
  lottie.loadAnimation({
    container: document.querySelector('#lottie-trophy')!,
    path: 'https://assets8.lottiefiles.com/packages/lf20_touohxv0.json',
    loop: true,
    renderer: 'svg',
  })
})
</script>

<template>
  <div wfull hfull flex="~ col">
    <div
      wfull h50px font-bold
      flex justify-center items-center
      relative text="24px center"
    >
      <div mr-10>
        录像回放
      </div>
      <div absolute flex gap-x-3 right-0>
        <n-button type="primary" text-color="white" :disabled="!recordFinished" @click="replay">
          重新回放
        </n-button>
        <n-button type="warning" text-color="white" :disabled="recordFinished" @click="pause">
          {{ ['暂停回放', '取消暂停'][Number(recordPaused)] }}
        </n-button>
        <n-button type="error" text-color="white" @click="goBack">
          Back
        </n-button>
      </div>
    </div>
    <div wfull h370px flex justify-center>
      <GameMap
        ref="refGameMap" hfull
        :style="{ width: '60%', minWidth: '300px', minHeight: '300px' }"
      />
      <div v-if="playerInfoList?.length" w-300px ha :class="{ hidden: containerWidth < 768 }">
        <n-card
          hoverable wfull
          flex="~ col" justify-center items-center
          :content-style="{ padding: '10px 20px', width: '100%' }"
        >
          <div text="24px center" font="bold italic">
            对局信息
          </div>
          <div flex justify-between items-center wfull mt10px>
            <div flex="~ col" justify-between items-center gap-y-4>
              <div text="[#4876EC] 18px" font-bold>
                蓝方
              </div>
              <n-avatar
                round
                :size="60"
                :src="playerInfoList[0].avatar"
              />
              <n-ellipsis :style="{ maxWidth: '80px' }">
                {{ playerInfoList[0].name ?? '-' }}
              </n-ellipsis>
            </div>
            <div w80px text="50px yellow center" font="bold italic game">
              VS
            </div>
            <div flex="~ col" justify-between items-center gap-y-4>
              <div text="[#F94848] 18px" font-bold>
                红方
              </div>
              <n-avatar
                round
                :size="60"
                :src="playerInfoList[1].avatar"
              />
              <n-ellipsis :style="{ maxWidth: '80px' }">
                {{ playerInfoList[1].name ?? '-' }}
              </n-ellipsis>
            </div>
          </div>
          <div text="center 24px" font="bold italic" mt-10px>
            <div v-if="loser === 'all'">
              平局
            </div>
            <div
              v-else-if="['A', 'B'].includes(loser)"
              w200px mxa
              flex justify-between items-center
              :style="{ color: loser === 'A' ? '#F94848' : '#4876EC' }"
            >
              <div id="lottie-trophy" mr2 w50px h50px />
              {{ loser === 'A' ? '红方' : '蓝方' }} <span text-yellow ml-4>胜利</span>
            </div>
            <div v-else>
              无结果
            </div>
          </div>
        </n-card>
      </div>
    </div>
  </div>
</template>
