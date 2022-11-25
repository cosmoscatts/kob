<script setup lang="ts">
const { user } = storeToRefs(useUserStore())
const { opponent, players } = storeToRefs(usePkStore())

const bodyColor = computed(() => isDark.value ? '#121212' : '#FFFFFF')

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
    ? 'calc(50% - 10rem)'
    : 'calc(50% - 5rem)'
})

onMounted(() => {
  useTimeoutFn(() => {
    useLottie({
      container: document.querySelector('#lottie-loading')!,
      path: 'https://assets1.lottiefiles.com/packages/lf20_F5Nz1IPcfz.json',
    })
  }, 1000)
})
</script>

<template>
  <div class="loading-mask" />
  <Transition leave-active-class="animate__animated animate__bounceOut">
    <div v-if="loading" class="loading-wrapper">
      <div id="lottie-loading" w400px h250px absolute z-10000 :style="{ top: '40vh', left: 'calc(50% - 200px)' }" />
      <div class="side left animate__animated animate__fadeInLeftBig">
        <div w250px h250px rounded-2px cursor-pointer>
          <img :src="players?.[0]?.id === user!.id ? user?.avatar : opponent?.avatar" h-full w-full rounded-full>
        </div>
      </div>
      <div class="vs animate__animated animate__fadeInDownBig animate__faster font-game">
        {{ title }}
      </div>
      <div class="side right animate__animated animate__fadeInRightBig">
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
    background-color: v-bind('bodyColor');
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
    padding-left: 300px;
    background: rgb(26, 188, 156);
    background: -moz-linear-gradient(-45deg, rgba(26, 188, 156, 1) 0%, rgba(142, 68, 173, 1) 100%);
    background: -webkit-linear-gradient(-45deg, rgba(26, 188, 156, 1) 0%, rgba(142, 68, 173, 1) 100%);
    background: linear-gradient(135deg, rgba(26, 188, 156, 1) 0%, rgba(142, 68, 173, 1) 100%);
  }

  .side.right {
    padding-right: 300px;
    background: #A1161F;
    background: -moz-linear-gradient(-45deg, rgba(161, 22, 31, 1) 0%, rgba(176, 27, 182, 1) 100%);
    background: -webkit-linear-gradient(-45deg, rgba(161, 22, 31, 1) 0%, rgba(176, 27, 182, 1) 100%);
    background: linear-gradient(135deg, rgba(161, 22, 31, 1) 0%, rgba(176, 27, 182, 1) 100%);
  }

  .side.left div {
    box-shadow: 0px 0px 20px blue inset;
    padding: 10px;
    border-radius: 10px;
    border: 6px blue solid;
  }

  .side.right div {
    box-shadow: 0px 0px 20px red inset;
    padding: 10px;
    border-radius: 10px;
    border: 6px red solid;
  }

  .vs {
    position: absolute;
    left:v-bind(titleLeft);
    top: calc(50% - 12rem);
    font-size: 14rem;
    font-style: italic;
    font-weight: bold;
    text-align: center;
    color: #FF9626;
    z-index: 1000;
    text-shadow: 0 0 0.6rem #FFE3B8, 0 0 1.5rem #FFCD87,
    -0.2rem 0.1rem 1rem #FFCD87, 0.2rem 0.1rem 1rem #FFCD87,
    0 -0.5rem 2rem #D26913, 0 0.5rem 3rem #D26913;
  }
</style>

