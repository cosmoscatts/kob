<script setup lang="ts">
import { GameControllerOutline } from '@vicons/ionicons5';
import MatchPlayerAvatar from './MatchPlayerAvatar.vue';

const router = useRouter();

const { contentStyle } = useLayoutStyle({ heightProperty: 'minHeight', additionalOffset: 8 });

const { user } = storeToRefs(useUserStore());
const { opponent, socket } = storeToRefs(usePkStore());

const selectedBot = ref();
const botOptions = ref<{ value: number, label: string }[]>([]);

const matchBtnText = ref('开始匹配');
const { loading, startLoading, endLoading } = useLoading();
function onClick() {
  if (matchBtnText.value === '开始匹配') {
    startLoading();
    matchBtnText.value = '取消匹配';
    socket.value?.send(JSON.stringify({
      event: 'start-matching',
      botId: selectedBot.value,
    }));
  } else {
    endLoading();
    matchBtnText.value = '开始匹配';
    socket.value?.send(JSON.stringify({
      event: 'stop-matching',
    }));
  }
}

const defaultBotOptions = [{ value: -1, label: '亲自出马' }];
async function fetchBotList() {
  try {
    const result = await BotApi.getBotList({});
    const { data: { records = [] } } = result.data;
    botOptions.value = [
      ...defaultBotOptions,
      ...records?.map(i => ({ value: i.id, label: i.title })) || [],
    ] as { value: number, label: string }[];
    selectedBot.value = -1;
  } catch (e) {
    console.error(e);
    botOptions.value = [...defaultBotOptions];
    selectedBot.value = -1;
  }
}
fetchBotList();

function navigateTo(path: string) {
  router.push(path);
}
</script>

<template>
  <div w60vw mxa ha flex="col" :style="contentStyle">
    <div flex-y-center justify-between mb-10px>
      <div text="primary 30px" font-800>
        匹配对抗
      </div>
      <n-button size="large" type="primary" text-color="white" @click="navigateTo('/pk')">
        返回
      </n-button>
    </div>

    <n-card>
      <div grid="~ cols-2" md:grid-flow-row-dense>
        <div col-span-2 flex="col center" pb-10px>
          <n-alert title="注意事项" type="warning" :style="{ width: '40vw' }">
            选择亲自出马时，使用`W`、`A`、`S`、`D`控制方向.
          </n-alert>
          <n-select
            v-model:value="selectedBot" :options="botOptions"
            :style="{ width: '20vw', textAlign: 'center', marginTop: '20px' }"
          />
        </div>

        <div col-span-1 lt-md="col-span-2 ha py-20px">
          <MatchPlayerAvatar :user="user" />
        </div>

        <div col-span-2>
          <div flex-center h-full>
            <n-button size="large" type="warning" text-color="white" @click="onClick">
              {{ matchBtnText }}
            </n-button>
          </div>
        </div>

        <div col-span-1 lt-md="col-span-2 ha py-20px">
          <MatchPlayerAvatar :user="opponent" />
        </div>
      </div>
    </n-card>

    <div v-if="loading" fixed w-400px :style="{ top: '6vh', left: 'calc(50% - 200px)' }">
      <n-alert title="这么好玩的项目竟然没人玩？" type="warning" closable>
        <template #icon>
          <n-icon>
            <GameControllerOutline />
          </n-icon>
        </template>
        正在拼命寻找对手(╯°□°）╯︵ ┻━┻
      </n-alert>
    </div>
  </div>
</template>
