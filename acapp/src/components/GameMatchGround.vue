<script setup lang="ts">
const { user } = storeToRefs(useUserStore())
const { opponent, socket } = storeToRefs(usePkStore())

const selectedBot = ref(-1)
const botOptions = ref<{ value: number; label: string }[]>([])

let matchBtnText = $ref('开始匹配')
function onClick() {
  if (matchBtnText === '开始匹配') {
    matchBtnText = '取消匹配'
    socket.value?.send(JSON.stringify({
      event: 'start-matching',
      botId: selectedBot.value,
    }))
  }
  else {
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
  <div w-60vw h-70vh mx-a lt-md="ha" lt-sm:w-96vw>
    <n-card>
      <div grid="~ cols-2" md:grid-flow-row-dense>
        <div col-span-2 h10vh flex justify-center items-center>
          <n-select
            v-model:value="selectedBot" :options="botOptions"
            :style="{ width: '20vw', textAlign: 'center', border: '1px #4b5563 solid' }"
          />
        </div>
        <div col-span-1 h40vh lt-md="col-span-2 ha py-20px">
          <div flex="~ col" justify-center items-center h-full>
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
          <div flex justify-center items-center h-full>
            <n-button size="large" type="warning" text-color="white" @click="onClick">
              {{ matchBtnText }}
            </n-button>
          </div>
        </div>
        <div col-span-1 h-40vh lt-md="col-span-2 ha py-20px">
          <div flex="~ col" justify-center items-center h-full>
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
  </div>
</template>
