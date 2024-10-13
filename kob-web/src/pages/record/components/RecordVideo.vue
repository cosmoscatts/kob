<script setup lang="ts">
import type { PlayerInfo } from '../columns';
import GameResult from './GameResult.vue';
import PlayerInfoCard from './PlayerInfoCard.vue';

const props = defineProps<{
  playerInfoList: PlayerInfo[]
}>();

const changeCurrentTab = inject<Function>('changeCurrentTab')!;

const { contentStyle } = useLayoutStyle({ heightProperty: 'minHeight', additionalOffset: 55 });

const refGameMap = ref();
const recordStore = useRecordStore();
const { gameResult, isReplayFinished } = storeToRefs(recordStore);

const recordPaused = ref(false);

const pause = () => refGameMap.value?.pauseVideo?.();
const replay = () => refGameMap.value?.replayVideo?.();
const resume = () => refGameMap.value?.resumeVideo?.();

const goBack = () => {
  pause();
  recordStore.resetRecordState();
  changeCurrentTab(0, {});
};

const doPause = () => {
  recordPaused.value ? resume() : pause();
  recordPaused.value = !recordPaused.value;
};

const handleVisibilityChange = () => {
  if (document.hidden) {
    pause();
  } else if (!recordPaused.value) {
    replay();
  }
};

onMounted(() => {
  useLottie({
    containerId: '#lottie-trophy',
    path: 'https://assets8.lottiefiles.com/packages/lf20_touohxv0.json',
  });

  document.addEventListener('visibilitychange', handleVisibilityChange);
});

onUnmounted(() => {
  document.removeEventListener('visibilitychange', handleVisibilityChange);
});
</script>

<template>
  <div :style="contentStyle" flex="col center">
    <div
      flex-center relative
      text="30px center"
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
        <n-button type="primary" text-color="white" :disabled="!isReplayFinished" @click="replay">
          重新回放
        </n-button>
        <n-button type="warning" text-color="white" :disabled="isReplayFinished" @click="doPause">
          {{ recordPaused ? '取消暂停' : '暂停回放' }}
        </n-button>
        <n-button type="error" text-color="white" @click="goBack">
          返回
        </n-button>
      </div>
    </div>
    <div flex-x-center gap-x-5vw xl:gap-x-3 pt-3vh>
      <GameMap ref="refGameMap" h-60vh w-40vw lt-md="!w-60vw" />
      <div v-if="props.playerInfoList?.length" w-300px ha lt-md:hidden flex-y-center mb15vh>
        <n-card hoverable flex="col center" w-full :content-style="{ padding: '10px 20px', width: '100%' }">
          <div text="24px center" font="bold italic">
            对局信息
          </div>
          <div flex justify-between items-center w-full mt-10px>
            <PlayerInfoCard :player="props.playerInfoList[0]" color="#4876EC" side="蓝方" />
            <div w80px text="50px yellow center" font="bold italic game">
              VS
            </div>
            <PlayerInfoCard :player="props.playerInfoList[1]" color="#F94848" side="红方" />
          </div>
          <GameResult :result="gameResult" />
        </n-card>
      </div>
    </div>
  </div>
</template>
