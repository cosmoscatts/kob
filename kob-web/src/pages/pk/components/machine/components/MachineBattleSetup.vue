<script setup lang="ts">
import type { SelectOption } from 'naive-ui';

const emit = defineEmits(['changePage']);

const { user } = storeToRefs(useUserStore());
const { socket } = storeToRefs(usePkStore());

const selectedBot = ref<number>();
const botList = ref<SelectOption[]>([]);

const machineId = ref<number>(-1);
const machineBotId = ref<number>();

const DEFAULT_BOT_OPTIONS: SelectOption[] = [{ value: -1, label: '亲自出马' }];

const LEVEL_OPTIONS: SelectOption[] = [
  { value: 0, label: '黑铁' },
  { value: 1, label: '黄金' },
  { value: 2, label: '钻石' },
  { value: 3, label: '王者' },
];

const botOptions = computed<(SelectOption)[]>(() => [
  ...DEFAULT_BOT_OPTIONS,
  ...botList.value,
]);

const startBattle = () => {
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
};

const fetchBotList = async () => {
  try {
    const result = await BotApi.getBotList({});
    const { data: { records = [] } } = result.data;
    botList.value = records.map(i => ({ value: i.id, label: i.title })) || [];
    selectedBot.value = -1;
  } catch (e) {
    console.error(e);
    botList.value = [];
    selectedBot.value = -1;
  }
};

const selectedTab = ref<'standard' | 'selfDefine'>('standard');

watch(selectedTab, (val) => {
  machineId.value = val === 'standard' ? -1 : user.value?.id ?? -1;
  machineBotId.value = undefined;
});

const { isMobile } = useResponsive();

onMounted(fetchBotList);
</script>

<template>
  <div w60vw mxa ha flex="col">
    <div flex-y-center justify-between mb15px>
      <div text="primary 30px" font-800>
        人机试炼
      </div>
      <n-button size="large" type="primary" text-color="white" @click="emit('changePage', 0)">
        返回
      </n-button>
    </div>
    <n-card :style="{ minHeight: '65vh', padding: '30px' }">
      <template v-if="!isMobile">
        <div w45vw mxa>
          <n-alert title="注意事项" type="warning">
            请选择我方出战的bot和人机难度，也可以选择我方bot作为对手。
            选择亲自出马时，使用`W`、`A`、`S`、`D`控制方向.
          </n-alert>
        </div>
        <n-card my25px>
          <div h10vh flex-center>
            <span text-lg font-800>我方出战：</span>
            <n-select v-model:value="selectedBot" :options="botOptions" :style="{ width: '20vw', textAlign: 'center' }" />
          </div>
        </n-card>

        <n-card my25px h30vh>
          <n-tabs v-model:value="selectedTab" size="large" justify-content="space-evenly">
            <n-tab-pane name="standard" tab="标准模式">
              <div flex="~ col" items-center>
                <n-h3 prefix="bar" align-text>
                  请选择人机难度
                </n-h3>
                <n-radio-group v-model:value="machineBotId" name="radiobuttongroup1">
                  <n-radio-button
                    v-for="item in LEVEL_OPTIONS"
                    :key="item.value"
                    :value="item.value"
                    :label="item.label as string"
                  />
                </n-radio-group>
              </div>
            </n-tab-pane>
            <n-tab-pane name="selfDefine" tab="自定义Bot">
              <div h10vh flex-center>
                <span text-lg font-800>敌方出战：</span>
                <n-select v-model:value="machineBotId" :options="botOptions.slice(1)" :style="{ width: '20vw', textAlign: 'center' }" />
              </div>
            </n-tab-pane>
          </n-tabs>
        </n-card>

        <div flex-center h-full>
          <n-button size="large" type="warning" text-color="white" @click="startBattle">
            开始战斗
          </n-button>
        </div>
      </template>
      <template v-else>
        <div w-full min-h-60vh flex-center text-lg>
          请在客户端访问 :).
        </div>
      </template>
    </n-card>
  </div>
</template>
