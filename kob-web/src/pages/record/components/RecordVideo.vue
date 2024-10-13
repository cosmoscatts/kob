<script setup lang="ts">
import type { PlayerInfo } from '../columns';
import defaultAvatar from '~/assets/default-avatar.png';
import { APP_LAYOUT_PARAMS } from '~/config';

const {
  playerInfoList = [],
} = defineProps<{
  playerInfoList: PlayerInfo[]
}>();

const changeCurrentTab = inject<Function>('changeCurrentTab')!;

const { navHeight, footHeight, contentPadding } = APP_LAYOUT_PARAMS;
const diffHeight = computed(() => {
  return navHeight + footHeight + contentPadding * 2 + 1 + 1 + 3 + 50;
});

const refGameMap = ref();
const recordStore = useRecordStore();
const { loser, recordFinished } = storeToRefs(recordStore);

const pause = () => refGameMap.value?.pauseVideo?.();
const replay = () => refGameMap.value?.replayVideo?.();
const resume = () => refGameMap.value?.resumeVideo?.();

function goBack() {
  pause();
  recordStore.clearVideo();
  changeCurrentTab(0, {});
}

const recordPaused = ref(false);
function doPause() {
  if (recordPaused.value)
    resume();
  else pause();
  recordPaused.value = !recordPaused.value;
}

onMounted(() => useLottie({
  containerId: '#lottie-trophy',
  path: 'https://assets8.lottiefiles.com/packages/lf20_touohxv0.json',
}));

document.addEventListener('visibilitychange', () => { // 判断是否离开页面
  if (document.visibilityState === 'hidden') { pause();
  }
  else {
    if (!recordPaused.value)
      replay();
  }
});
</script>

<template>
  <div :style="{ minHeight: `calc(100vh - ${diffHeight}px)` }" flex="col center">
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
        <n-button type="primary" text-color="white" :disabled="!recordFinished" @click="replay">
          重新回放
        </n-button>
        <n-button type="warning" text-color="white" :disabled="recordFinished" @click="doPause">
          {{ ['暂停回放', '取消暂停'][Number(recordPaused)] }}
        </n-button>
        <n-button type="error" text-color="white" @click="goBack">
          返回
        </n-button>
      </div>
    </div>
    <div flex-x-center gap-x-5vw xl:gap-x-3 pt-3vh>
      <GameMap ref="refGameMap" h-60vh w-40vw lt-md="!w-60vw" />
      <div v-if="playerInfoList?.length" w-300px ha lt-md:hidden flex-y-center mb15vh>
        <n-card hoverable flex="col center" w-full :content-style="{ padding: '10px 20px', width: '100%' }">
          <div text="24px center" font="bold italic">
            对局信息
          </div>
          <div flex justify-between items-center w-full mt-10px>
            <div flex="col center" gap-y-4>
              <div text="[#4876EC] 18px" font-bold>
                蓝方
              </div>
              <n-avatar
                round
                :size="60"
                :src="playerInfoList[0].avatar ?? defaultAvatar"
              />
              <n-ellipsis :style="{ maxWidth: '80px' }">
                {{ playerInfoList[0].name ?? '-' }}
              </n-ellipsis>
            </div>
            <div w80px text="50px yellow center" font="bold italic game">
              VS
            </div>
            <div flex="col center" gap-y-4>
              <div text="[#F94848] 18px" font-bold>
                红方
              </div>
              <n-avatar
                round
                :size="60"
                :src="playerInfoList[1].avatar ?? defaultAvatar"
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
            <div v-else-if="['A', 'B'].includes(loser)" :style="{ color: loser === 'A' ? '#F94848' : '#4876EC' }" flex-center>
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
