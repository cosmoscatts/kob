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
</script>

<template>
  <div class="loading-mask" />
  <Transition leave-active-class="animate__animated animate__bounceOut">
    <div v-if="loading" class="loading-wrapper">
      <div class="side left animate__animated animate__fadeInLeftBig">
        <div w250px h250px rounded-2px cursor-pointer hover:shadow-nav_item>
          <img :src="players?.[0]?.id === user!.id ? user?.avatar : opponent?.avatar" h-full w-full rounded-full>
        </div>
      </div>
      <div class="vs animate__animated animate__fadeInDownBig animate__faster">
        {{ title }}
      </div>
      <div class="side right animate__animated animate__fadeInRightBig">
        <div w250px h250px rounded-2px cursor-pointer border="1 red">
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
    background-color: #0284c7;
    padding-left: 300px;
  }

  .side.right {
    background-color: #881337;
    padding-right: 300px;
  }

  .side.left div {
    box-shadow: 0px 0px 20px blue inset;
    padding: 10px;
    border-radius: 10px;
  }

  .side.right div {
    box-shadow: 0px 0px 20px #DF5A49 inset;
    padding: 10px;
    border-radius: 10px;
  }

  .vs {
    position: absolute;
    left:v-bind(titleLeft);
    top: calc(50% - 12rem);
    font-size: 14rem;
    font-style: italic;
    font-weight: bold;
    text-align: center;
    color: #FFDD40;
    z-index: 1000;
  }
</style>

