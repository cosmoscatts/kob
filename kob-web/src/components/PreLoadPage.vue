<script setup lang="ts">
import { useThemeVars } from 'naive-ui'
import { APP_META } from '~/config'
import logo from '~/assets/logo.png'

const themeVars = useThemeVars()
const bodyColor = computed(() => ['#FFFFFF', '#121212'][Number(isDark.value)])

// 控制内层动画
const { loading, endLoading } = useLoading(true)
useTimeoutFn(endLoading, 800)

const createLottie = () => useLottie({
  containerId: '#lottie-container',
  path: 'https://assets2.lottiefiles.com/packages/lf20_IOZ1VGjvs1.json',
})
onMounted(createLottie)
</script>

<template>
  <div class="loading-mask" />
  <Transition leave-active-class="animate__animated animate__bounceOut">
    <div v-if="loading" class="loading-wrapper">
      <div mb-100px>
        <div flex-center>
          <div id="lottie-container" w300px h300px />
        </div>
        <div class="animate__animated animate__fadeInUpBig animate__faster">
          <div flex-center>
            <n-avatar
              round
              color="transparent"
              size="large"
              :src="logo"
            />
            <div ml-20px>
              <n-gradient-text
                :gradient="`linear-gradient(90deg, ${themeVars.successColor} 0%, ${themeVars.warningColor} 33%, ${themeVars.infoColor} 66%, ${themeVars.errorColor} 100%)`"
                :size="36" style="font-weight: bold;"
              >
                {{ APP_META.name }}
              </n-gradient-text>
            </div>
          </div>

          <div flex-center mt-20px text-24px>
            <n-gradient-text
              :gradient="`linear-gradient(90deg, ${themeVars.successColor} 0%, ${themeVars.warningColor} 33%, ${themeVars.infoColor} 66%, ${themeVars.errorColor} 100%)`"
              :size="24" style="font-weight: bold;"
            >
              {{ APP_META.description }}
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
