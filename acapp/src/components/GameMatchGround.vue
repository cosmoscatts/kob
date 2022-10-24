<script setup lang="ts">
import type { Ref } from 'vue'

const containerWidth = inject<Ref<number>>('containerWidth')!

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
  <div
    :style="{
      width: containerWidth < 768 ? '96%' : '80%',
      height: '90%',
      minHeight: '400px',
      marginLeft: containerWidth < 768 ? '2%' : '10%',
    }"
  >
    <n-card>
      <div grid="~ cols-2" :style="{ gridAutoFlow: 'row dense' }">
        <div col-span-2 h100px flex justify-center items-center>
          <n-select
            v-model:value="selectedBot" :options="botOptions"
            :style="{ width: '200px', textAlign: 'center', border: '1px #4b5563 solid' }"
          />
        </div>
        <div
          h200px col-span-1
          :class="{
            h320px: containerWidth >= 1024,
          }"
        >
          <div flex="~ col" justify-center items-center h-full>
            <n-avatar
              :style="{
                width: '100%',
                maxWidth: containerWidth < 640 ? '80px' : containerWidth < 1024 ? '120px' : '180px',
                height: 'auto',
                cursor: 'pointer',
              }"
              round
              :src="user?.avatar"
            />
            <div mt-20px text-24px font-600>
              {{ user?.name }}
            </div>
          </div>
        </div>
        <div col-span-2 h100px>
          <div flex justify-center items-center h-full>
            <n-button size="large" type="warning" text-color="white" @click="onClick">
              {{ matchBtnText }}
            </n-button>
          </div>
        </div>
        <div
          h200pxcol-span-1
          :class="{
            h320px: containerWidth >= 1024,
          }"
        >
          <div flex="~ col" justify-center items-center h-full>
            <n-avatar
              :style="{
                width: '100%',
                maxWidth: containerWidth < 640 ? '80px' : containerWidth < 1024 ? '120px' : '180px',
                height: 'auto',
                cursor: 'pointer',
              }"
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
