<script setup lang="ts">
import type { SelectOption } from 'naive-ui';

const router = useRouter();
const { contentStyle } = useLayoutStyle({ heightProperty: 'minHeight', additionalOffset: 8 });

const { user } = storeToRefs(useUserStore());
const { socket } = storeToRefs(usePkStore());

const selectedBot = ref<number>();
const botList = ref<SelectOption[]>([]);

const machineId = ref<number>(-1);
const machineBotId = ref<number>();

const DEFAULT_BOT_OPTIONS: SelectOption[] = [{ value: -1, label: '亲自出马' }];

const levelOptions = ref<SelectOption[]>([]);
const botOptions = computed<SelectOption[]>(() => [
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

const fetchMachineBots = async () => {
  try {
    const result = await BotApi.getMachineBots();
    const { data = [] } = result.data;
    levelOptions.value = data.map(i => ({ value: i.id, label: i.title })) || [];
    machineBotId.value = undefined;
  } catch (e) {
    console.error(e);
    levelOptions.value = [];
    machineBotId.value = undefined;
  }
};

const selectedTab = ref<'standard' | 'selfDefine'>('standard');

watch(selectedTab, (val) => {
  machineId.value = val === 'standard' ? 1 : user.value?.id ?? -1;
  machineBotId.value = undefined;
});

function navigateTo(path: string) {
  router.push(path);
}

const { isMobile } = useResponsive();
const tabsPlacement = computed(() => isMobile.value ? 'top' : 'left');

onMounted(() => {
  fetchBotList();
  fetchMachineBots();
});
</script>

<template>
  <div w60vw mxa ha flex="~ col" :style="contentStyle" lt-md:w-full>
    <div flex-y-center justify-between mb-10px>
      <div text="primary 30px" font-800>
        人机试炼
      </div>
      <n-button size="large" type="primary" text-color="white" @click="navigateTo('/pk')">
        返回
      </n-button>
    </div>

    <n-card style="height: 100%;">
      <div w-full mxa>
        <n-alert title="注意事项" type="warning">
          请选择我方出战的bot和人机难度，也可以选择我方bot作为对手。
          选择亲自出马时，使用`W`、`A`、`S`、`D`控制方向.
        </n-alert>
      </div>

      <div grid="~ cols-2 gap-3" lt-md:grid-cols-1 py-15px min-h-260px>
        <n-card col-span-1>
          <div flex-center h-full flex-col gap-y-5 lt-md:flex-row>
            <span text-lg font-800>我方出战：</span>
            <n-select v-model:value="selectedBot" :options="botOptions" :style="{ width: isMobile ? '200px' : '15vw', textAlign: 'center' }" />
          </div>
        </n-card>

        <n-card col-span-1>
          <n-tabs v-model:value="selectedTab" :placement="tabsPlacement" justify-content="space-evenly" style="height: 100%;">
            <n-tab-pane name="standard" tab="标准模式">
              <div flex="center col" h-full gap-y-5>
                <span text-lg font-800>请选择人机难度：</span>
                <n-radio-group v-model:value="machineBotId" name="radiobuttongroup1">
                  <n-radio-button
                    v-for="item in levelOptions"
                    :key="item.value"
                    :value="item.value"
                    :label="item.label as string"
                  />
                </n-radio-group>
              </div>
            </n-tab-pane>

            <n-tab-pane name="selfDefine" tab="自定义Bot">
              <div flex-center h-full flex-col gap-y-5 lt-md:flex-row>
                <span text-lg font-800>敌方出战：</span>
                <n-select v-model:value="machineBotId" :options="botOptions.slice(1)" :style="{ width: isMobile ? '200px' : '15vw', textAlign: 'center' }" />
              </div>
            </n-tab-pane>
          </n-tabs>
        </n-card>
      </div>

      <div flex-center h-full>
        <n-button size="large" type="warning" text-color="white" @click="startBattle">
          开始战斗
        </n-button>
      </div>
    </n-card>
  </div>
</template>
