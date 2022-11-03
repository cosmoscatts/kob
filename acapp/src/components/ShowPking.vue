<script setup lang="ts">
const { user } = storeToRefs(useUserStore())
const { opponent, players } = storeToRefs(usePkStore())

// 控制内层动画
const { loading, endLoading } = useLoading(true)
useTimeoutFn(endLoading, 5000)

const title = ref<string | number>('VS')

const { pause, resume } = useIntervalFn(() => {
  if (title.value === 0) {
    pause()
    title.value = 'VS'
    return
  }
  if (title.value === 'VS')
    title.value = 3

  else
    title.value = title.value as number - 1
}, 1000)
useTimeoutFn(resume, 500)

const titleLeft = computed(() => {
  return title.value === 'VS'
    ? 'calc(50% - 35px)'
    : 'calc(50% - 18px)'
})
</script>

<template>
  <div class="loading-mask" />
  <Transition leave-active-class="animate__animated animate__bounceOut">
    <div v-if="loading" class="loading-wrapper">
      <div class="side left animate__animated animate__fadeInLeftBig animate__faster">
        <div w250px h250px rounded-2px cursor-pointer>
          <img :src="players?.[0]?.id === user!.id ? user?.avatar : opponent?.avatar" h-full w-full rounded-full>
        </div>
      </div>

      <div class="vs animate__animated animate__fadeInDownBig animate__faster">
        {{ title }}
      </div>
      <div class="side right animate__animated animate__fadeInRightBig animate__faster">
        <div w250px h250px rounded-2px cursor-pointer>
          <img :src="players?.[1]?.id === user!.id ? user?.avatar : opponent?.avatar" hfull wfull rounded-full>
        </div>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
  .loading-mask {
    position: fixed;
    left: 0;
    top: 0;
    bottom: 0;
    right: 0;
    background-color: #47485C;
    pointer-events: none;
    z-index: 10000;
  }

  .loading-wrapper {
    position: fixed;
    left: -300px;
    top: -300px;
    bottom: -300px;
    right: -300px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: rgba(255, 255, 255, 0.5);
    z-index: 10001;
  }

  .side {
    width: 50%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .side.left {
    background-color: rgba(2, 132, 199, 0.6);
    padding-left: 300px;
  }

  .side.right {
    background-color: rgba(220, 38, 38, 0.6);
    padding-right: 300px;
  }

  .side.left div {
    box-shadow: 0px 0px 20px blue inset;
  }

  .side.right div {
    box-shadow: 0px 0px 20px red inset;
  }

  .vs {
    position: absolute;
    left:v-bind(titleLeft);
    top: calc(50% - 50px);
    font-size: 50px;
    font-style: italic;
    font-weight: bold;
    text-align: center;
    color: yellow;
  }
</style>

