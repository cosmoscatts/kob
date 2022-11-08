<script setup lang="ts">
import lottie from 'lottie-web'
import { useThemeVars } from 'naive-ui'
import { appMeta } from '~/config'

const {
  beforeLeaveMs = 1500,
} = defineProps<{
  beforeLeaveMs: number
}>()

const themeVars = useThemeVars()

const bodyColor = computed(() => isDark.value ? '#121212' : '#FFFFFF')

// 控制内层动画
const { loading, endLoading } = useLoading(true)
useTimeoutFn(endLoading, beforeLeaveMs)

onMounted(() => {
  lottie.loadAnimation({
    container: document.querySelector('#lottie-container')!,
    path: 'https://assets6.lottiefiles.com/packages/lf20_iqxl5bjr.json',
    loop: true,
    renderer: 'svg',
  })
})
</script>

<template>
  <div class="loading-mask" />
  <Transition leave-active-class="animate__animated animate__bounceOut">
    <div v-if="loading" class="loading-wrapper">
      <div mb-100px>
        <div id="lottie-container" w300px h300px />
        <div class="animate__animated animate__fadeInUpBig animate__faster">
          <div flex-center>
            <div i-ri-game-fill text="primary 40px" />
            <div ml-20px>
              <n-gradient-text
                :gradient="`linear-gradient(90deg, ${themeVars.successColor} 0%, ${themeVars.warningColor} 33%, ${themeVars.infoColor} 66%, ${themeVars.errorColor} 100%)`"
                :size="36" style="font-weight: bold;"
              >
                {{ appMeta.appName }}
              </n-gradient-text>
            </div>
          </div>

          <div flex-center mt-20px text-24px>
            <n-gradient-text
              :gradient="`linear-gradient(90deg, ${themeVars.successColor} 0%, ${themeVars.warningColor} 33%, ${themeVars.infoColor} 66%, ${themeVars.errorColor} 100%)`"
              :size="24" style="font-weight: bold;"
            >
              {{ appMeta.description }}
            </n-gradient-text>
          </div>
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
  background-color: v-bind('bodyColor');
  z-index: 10001;
}
</style>

