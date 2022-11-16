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
  if (!selectedBot.value)
    selectedBot.value = -1
}
onMounted(fetchBotList)
</script>

<template>
  <div
    :style="{
      width: '80%',
      height: '370px',
      marginLeft: '10%',
    }"
  >
    <div flex items-center justify-between mb5px h50px>
      <div text-24px font-800>
        匹配对战
      </div>
      <n-button size="large" type="primary" text-color="white" @click="changePageIndex?.(0)">
        Back
      </n-button>
    </div>
    <n-card>
      <div grid="~ cols-2" :style="{ gridAutoFlow: 'row dense' }">
        <div col-span-2 h50px flex justify-center items-center>
          <n-select
            v-model:value="selectedBot" :options="botOptions" placeholder="你想怎么玩？"
            :style="{ width: '200px', textAlign: 'center', border: '1px #4b5563 solid' }"
          />
        </div>
        <div h180px col-span-1>
          <div flex="~ col" justify-center items-center hfull>
            <n-avatar
              :style="{
                width: '100px',
                height: 'auto',
                cursor: 'pointer',
              }"
              round
              :src="user?.avatar"
            />
            <div mt5px text-24px font-600>
              {{ user?.name }}
            </div>
          </div>
        </div>
        <div col-span-2 h50px>
          <div flex justify-center items-center h-full>
            <n-button size="large" type="warning" text-color="white" @click="onClick">
              {{ matchBtnText }}
            </n-button>
          </div>
        </div>
        <div h180px col-span-1>
          <div flex="~ col" justify-center items-center h-full>
            <n-avatar
              :style="{
                width: '100px',
                height: 'auto',
                cursor: 'pointer',
              }"
              round
              :src="opponent?.avatar"
            />
            <div mt5px text-24px font-600>
              {{ opponent?.name }}
            </div>
          </div>
        </div>
      </div>
    </n-card>
    <div v-if="loading" fixed w-400px :style="{ top: '10vh', left: 'calc(50% - 200px)' }">
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
