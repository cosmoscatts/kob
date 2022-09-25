<script setup lang="ts">
const { user } = storeToRefs(useUserStore())
const { opponent, socket } = storeToRefs(usePkStore())

let matchBtnText = $ref('开始匹配')
function onClick() {
  if (matchBtnText === '开始匹配') {
    matchBtnText = '取消匹配'
    socket.value?.send(JSON.stringify({
      event: 'start-matching',
    }))
  }
  else {
    matchBtnText = '开始匹配'
    socket.value?.send(JSON.stringify({
      event: 'stop-matching',
    }))
  }
}
</script>

<template>
  <div w-60vw h-70vh mx-a lt-md="ha" flex-y-center>
    <n-card>
      <div grid="~ cols-2" md:grid-flow-row-dense>
        <div col-span-1 h-50vh lt-md="col-span-2 ha py-20px">
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
        <div col-span-1 h-50vh lt-md="col-span-2 ha py-20px">
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
  </div>
</template>
