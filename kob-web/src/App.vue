<script setup lang="ts">
import { darkTheme, dateZhCN, zhCN } from 'naive-ui'
import PreLoaded from './components/PreLoaded.vue'

const theme = computed(() => {
  return isDark.value
    ? darkTheme
    : null
})

// `useHead` 创建 `head` 数据
useHeadMeta()

// themeOverrides
const themeOverrides = ref()

// 初始化 `loading`
const { loading: appLoading, endLoading } = useLoading(true)

// 定义 `loading` 整体时间和内层动画持续时间
const LOADING_INTERVAL = 2500
const LOADING_INNER_INTERVAL = 1800
useTimeoutFn(endLoading, LOADING_INTERVAL)
</script>

<template>
  <n-config-provider
    :theme="theme"
    :theme-overrides="themeOverrides"
    :locale="zhCN"
    :date-locale="dateZhCN"
  >
    <n-loading-bar-provider>
      <PreLoaded v-if="appLoading" :inner-loading="LOADING_INNER_INTERVAL" />
      <RouterView v-else />
    </n-loading-bar-provider>
  </n-config-provider>
</template>
