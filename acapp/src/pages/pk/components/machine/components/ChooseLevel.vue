<script setup lang="ts">
import { breakpointsTailwind } from '@vueuse/core'

const changePageIndex = inject('changePageIndex') as Function

const { message } = useGlobalNaiveApi()

const { user } = storeToRefs(useUserStore())
const { socket } = storeToRefs(usePkStore())

const selectedBot = ref()
const botOptions = ref<{ value: number; label: string }[]>([])
// 人机
const machineId = ref<number>()
const machineBotId = ref<number>()

function onClick() {
  if (selectedBot.value === undefined || machineId.value === undefined || machineBotId.value === undefined) {
    message.warning('请选择双方出战Bot')
    return
  }

  socket.value?.send(JSON.stringify({
    event: 'start-machine-training',
    botId: selectedBot.value,
    machineId: machineId.value,
    machineBotId: machineBotId.value,
  }))
}

const defaultBotOptions = [{ value: -1, label: '亲自出马' }]
async function fetchBotList() {
  const { data: { records } } = await BotApi.getBotList({})
  botOptions.value = [
    ...defaultBotOptions,
    ...records?.map(i => ({ value: i.id, label: i.title })) || [],
  ] as { value: number; label: string }[]
  selectedBot.value = -1
}
fetchBotList()

const levelOptions = ref([
  { value: 0, label: '黑铁' },
  { value: 1, label: '黄金' },
  { value: 2, label: '钻石' },
  { value: 3, label: '王者' },
])

const selectedTab = ref<'standard' | 'selfDefine'>('standard')
machineId.value = 1
watch(selectedTab, (val) => {
  machineId.value = val === 'standard' ? -1 : user.value?.id
  machineBotId.value = undefined
})

const breakpoints = useBreakpoints(breakpointsTailwind)
const isMobile = breakpoints.smaller('sm')
</script>

<template>
  <div
    :style="{
      width: '80%',
      height: '370px',
      marginLeft: '10%',
    }"
    flex="col"
  >
    <div flex items-center justify-between mb5px h50px>
      <div text-24px font-800>
        人机试炼
      </div>
      <n-button size="large" type="primary" text-color="white" @click="changePageIndex?.(0)">
        Back
      </n-button>
    </div>
    <n-card v-if="!isMobile" :style="{ minHeight: '320px', padding: '0 15px' }">
      <n-card :content-style="{ padding: '0' }">
        <div h50px flex justify-center items-center>
          <span text-lg font-800>我方出战：</span>
          <n-select v-model:value="selectedBot" :options="botOptions" :style="{ width: '20vw', textAlign: 'center' }" />
        </div>
      </n-card>

      <n-card :content-style="{ padding: '0' }" mt15px h160px>
        <n-tabs v-model:value="selectedTab" size="large" justify-content="space-evenly">
          <n-tab-pane name="standard" tab="标准模式">
            <div flex="col ~" justify-center items-center>
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
            <div h50px flex justify-center items-center>
              <span text-lg font-800>敌方出战：</span>
              <n-select v-model:value="machineBotId" :options="botOptions.slice(1)" :style="{ width: '20vw', textAlign: 'center' }" />
            </div>
          </n-tab-pane>
        </n-tabs>
      </n-card>

      <div flex justify-center items-center mt15px>
        <n-button size="large" type="warning" text-color="white" @click="onClick">
          开始战斗
        </n-button>
      </div>
    </n-card>
    <n-card v-else>
      <div
        wfull min-h-60vh
        flex justify-center items-center
        text-lg
      >
        {  请在客户端访问 :). }
      </div>
    </n-card>
  </div>
</template>
