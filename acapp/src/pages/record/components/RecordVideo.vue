<script setup lang="ts">
import type { PlayerInfo } from '../helper'

const {
  playerInfoList = [],
} = defineProps<{
  playerInfoList: PlayerInfo[]
}>()

const changeCurrentTab = inject<Function>('changeCurrentTab')!

const { clearVideo } = useRecordStore()
const { loser } = storeToRefs(useRecordStore())

function goBack() {
  clearVideo()
  changeCurrentTab(0, {})
}

const refGameMap = ref()

function replay() {
  refGameMap.value?.replayVideo?.()
}
</script>

<template>
  <div hfull flex="~ col" justify-center items-center>
    <div
      flex justify-center items-center
      relative text="30px center"
      :style="{
        height: '10vh',
        width: 'calc(50vw + 300px)',
        fontWeight: 'bold',
      }"
    >
      <div mr-10>
        录像回放
      </div>
      <div absolute right-0 flex gap-x-5 lt-md="right-35px gap-x-2">
        <n-button type="primary" text-color="white" @click="replay">
          重新回放
        </n-button>
        <n-button type="error" text-color="white" @click="goBack">
          返回
        </n-button>
      </div>
    </div>
    <div flex justify-center md:gap-x-5 lg:gap-x-2>
      <GameMap ref="refGameMap" h-60vh w-40vw lt-md="!w-60vw" />
      <div v-if="playerInfoList?.length" w-300px ha lt-md:hidden>
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
              <div>{{ playerInfoList[0].name ?? '-' }}</div>
            </div>
            <div text="50px yellow" font="bold italic">
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
              <div>{{ playerInfoList[1].name ?? '-' }}</div>
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
              <div i-akar-icons-trophy mr-4 />
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
