<script setup lang="ts">
const changePageIndex = inject('changePageIndex') as Function;

const { user } = storeToRefs(useUserStore());
const { socket } = storeToRefs(usePkStore());

const selectedBot = ref();
const botOptions = ref<{ value: number, label: string }[]>([]);
// 人机
const machineId = ref<number>();
const machineBotId = ref<number>();

function onClick() {
  if (selectedBot.value === undefined || machineId.value === undefined || machineBotId.value === undefined) {
    $message.warning('请选择双方出战Bot');
    return;
  }

  socket.value?.send(JSON.stringify({
    event: 'start-machine-training',
    botId: selectedBot.value,
    machineId: machineId.value,
    machineBotId: machineBotId.value,
  }));
}

const defaultBotOptions = [{ value: -1, label: '亲自出马' }];
async function fetchBotList() {
  try {
    const result = await BotApi.getBotList({});
    const { data: { records = [] } } = result.data;
    botOptions.value = [
      ...defaultBotOptions,
      ...records.map(i => ({ value: i.id, label: i.title })) || [],
    ] as { value: number, label: string }[];
    selectedBot.value = -1;
  } catch (e) {
    console.error(e);
    botOptions.value = [...defaultBotOptions];
    selectedBot.value = -1;
  }
}
fetchBotList();

const levelOptions = ref([
  { value: 0, label: '黑铁' },
  { value: 1, label: '黄金' },
  { value: 2, label: '钻石' },
  { value: 3, label: '王者' },
]);

const selectedTab = ref<'standard' | 'selfDefine'>('standard');
machineId.value = 1;
watch(selectedTab, (val) => {
  machineId.value = val === 'standard' ? -1 : user.value?.id;
  machineBotId.value = undefined;
});

const { isMobile } = useResponsive();
</script>

<template>
  <div w60vw mxa ha flex="col">
    <div flex-y-center justify-between mb15px>
      <div text="primary 30px" font-800>
        人机试炼
      </div>
      <n-button size="large" type="primary" text-color="white" @click="changePageIndex?.(0)">
        返回
      </n-button>
    </div>
    <n-card v-if="!isMobile" :style="{ minHeight: '65vh', padding: '30px' }">
      <div w45vw mxa>
        <n-alert title="注意事项" type="warning">
          请选择我方出战的bot和人机难度，也可以选择我方bot作为对手。
          选择亲自出马时，使用`W`、`A`、`S`、`D`控制方向.
        </n-alert>
      </div>
      <n-card my25px>
        <div h-10vh flex-center>
          <span text-lg font-800>我方出战：</span>
          <n-select v-model:value="selectedBot" :options="botOptions" :style="{ width: '20vw', textAlign: 'center' }" />
        </div>
      </n-card>

      <n-card my25px h30vh>
        <n-tabs v-model:value="selectedTab" size="large" justify-content="space-evenly">
          <n-tab-pane name="standard" tab="标准模式">
            <div flex="col center">
              <n-h3 prefix="bar" align-text>
                请选择人机难度
              </n-h3>
              <n-radio-group v-model:value="machineBotId" name="radiobuttongroup1">
                <n-radio-button
                  v-for="item in levelOptions"
                  :key="item.value"
                  :value="item.value"
                  :label="item.label"
                />
              </n-radio-group>
            </div>
          </n-tab-pane>
          <n-tab-pane name="selfDefine" tab="自定义Bot">
            <div h-10vh flex-center>
              <span text-lg font-800>敌方出战：</span>
              <n-select v-model:value="machineBotId" :options="botOptions.slice(1)" :style="{ width: '20vw', textAlign: 'center' }" />
            </div>
          </n-tab-pane>
        </n-tabs>
      </n-card>

      <div flex-center h-full>
        <n-button size="large" type="warning" text-color="white" @click="onClick">
          开始战斗
        </n-button>
      </div>
    </n-card>
    <n-card v-else>
      <div
        wfull min-h-60vh
        flex-center
        text-lg
      >
        {  请在客户端访问 :). }
      </div>
    </n-card>
  </div>
</template>
