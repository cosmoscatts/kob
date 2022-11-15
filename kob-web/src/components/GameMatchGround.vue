<script setup lang="ts">
import { GameControllerOutline } from '@vicons/ionicons5'

const changePageIndex = inject('changePageIndex') as Function

const { user } = storeToRefs(useUserStore())
const { opponent, socket } = storeToRefs(usePkStore())

const selectedBot = ref()
const botOptions = ref<{ value: number; label: string }[]>([])

let matchBtnText = $ref('开始匹配')
const { loading, startLoading, endLoading } = useLoading()
function onClick() {
  if (matchBtnText === '开始匹配') {
    startLoading()
    matchBtnText = '取消匹配'
    socket.value?.send(JSON.stringify({
      event: 'start-matching',
      botId: selectedBot.value,
    }))
  }
  else {
    endLoading()
    matchBtnText = '开始匹配'
    socket.value?.send(JSON.stringify({
      event: 'stop-matching',
    }))
  }
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
</script>

<template>
  <div w60vw mxa ha flex="col">
    <div flex-y-center justify-between mb15px>
      <div text="primary 30px" font-800>
        匹配对抗
      </div>
      <n-button size="large" type="primary" text-color="white" @click="changePageIndex?.(0)">
        返回
      </n-button>
    </div>
    <n-card>
      <div grid="~ cols-2" md:grid-flow-row-dense>
        <div col-span-2 h-10vh flex-center>
          <n-select v-model:value="selectedBot" :options="botOptions" :style="{ width: '20vw', textAlign: 'center' }" />
        </div>
        <div col-span-1 h-40vh lt-md="col-span-2 ha py-20px">
          <div flex="~ col center" h-full>
            <n-avatar
              :style="{
                width: '100%',
                maxWidth: '180px',
                height: 'auto',
                cursor: 'pointer',
              }"
              lt-md="!max-w-120px"
              round
              :src="user?.avatar"
            />
            <div mt-20px text-24px font-600>
              {{ user?.name }}
            </div>
          </div>
        </div>
        <div col-span-2 h-20vh>
          <div flex-center h-full>
            <n-button size="large" type="warning" text-color="white" @click="onClick">
              {{ matchBtnText }}
            </n-button>
          </div>
        </div>
        <div col-span-1 h-40vh lt-md="col-span-2 ha py-20px">
          <div flex="~ col center" h-full>
            <n-avatar
              :style="{
                width: '100%',
                maxWidth: '180px',
                height: 'auto',
                cursor: 'pointer',
              }"
              lt-md="!max-w-120px"
              round
              :src="opponent?.avatar"
            />
            <div mt-20px text-24px font-600>
              {{ opponent?.name }}
            </div>
          </div>
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
