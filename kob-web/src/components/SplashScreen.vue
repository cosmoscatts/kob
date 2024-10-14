<script setup lang="ts">
import { useThemeVars } from 'naive-ui';
import logo from '~/assets/logo.png';
import { appMeta } from '~/config';

const themeVars = useThemeVars();
const bodyColor = computed(() => isDark.value ? '#121212' : '#FFFFFF');

const visible = ref(true);

const gradientStyle = computed(() => ({
  background: `linear-gradient(90deg, ${themeVars.value.successColor} 0%, ${themeVars.value.warningColor} 33%, ${themeVars.value.infoColor} 66%, ${themeVars.value.errorColor} 100%)`,
  WebkitBackgroundClip: 'text',
  WebkitTextFillColor: 'transparent',
}));

onMounted(() => {
  useLottie({
    containerId: '#lottie-container',
    path: '/public/lottie/load.json',
    loop: true,
  });
});

const hide = () => {
  visible.value = false;
};

defineExpose({ hide });
</script>

<template>
  <Transition name="fade">
    <div v-if="visible" class="splash-screen">
      <div class="content">
        <div id="lottie-container" class="lottie-animation" />
        <div class="app-info">
          <div class="logo-title">
            <n-avatar round color="transparent" size="large" :src="logo" class="logo-spin" />
            <h1 :style="gradientStyle" class="title-fade-in">
              {{ appMeta.name }}
            </h1>
          </div>
          <p :style="gradientStyle" class="description-fade-in">
            {{ appMeta.description }}
          </p>
        </div>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
.splash-screen {
  position: fixed;
  left: 0;
  top: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: v-bind('bodyColor');
  z-index: 10000;
}

.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.lottie-animation {
  width: 120px;
  height: 120px;
  margin-bottom: 20px;
}

.app-info {
  text-align: center;
}

.logo-title {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
}

.logo-spin {
  animation: spin 1.6s linear infinite;
}

.logo-title h1 {
  font-size: 28px;
  font-weight: bold;
  margin-left: 10px;
}

.app-info p {
  font-size: 18px;
  font-weight: bold;
}

.title-fade-in, .description-fade-in {
  opacity: 0;
  animation: fade-in 0.5s ease 0.3s forwards;
}

.description-fade-in {
  animation-delay: 0.5s;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@keyframes fade-in {
  to {
    opacity: 1;
  }
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
