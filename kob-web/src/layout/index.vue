<script  setup lang="ts">
import TheNav from './components/TheNav.vue'
import TheContent from './components/TheContent.vue'
import TheFoot from './components/TheFoot.vue'
import { appLayout } from '~/config'

const {
  navHeight,
  contentPadding,
  footHeight,
  backTopRight,
  backTopBottom,
  backTopvisibilityHeight,
} = appLayout

// 设置 `backTop` 的监听目标
const refWrapper = ref()
</script>

<template>
  <n-layout hw-screen of-hidden :native-scrollbar="false">
    <n-layout-header bordered position="absolute">
      <TheNav w-full :style="{ height: `${navHeight}px` }" />
    </n-layout-header>
    <n-layout
      ref="refWrapper"
      position="absolute"
      :style="{
        marginTop: `${navHeight + 1}px`,
        height: 'auto',
        minHeight: `calc(100% - ${navHeight + 1}px)`,
      }"
      :native-scrollbar="false"
    >
      <n-layout-content>
        <TheContent ha :style="{ padding: `${contentPadding}px`, minHeight: `calc(100vh - ${navHeight + 1 + footHeight + 1}px)` }" />
      </n-layout-content>
      <n-layout-footer :style="{ height: `${footHeight}px` }" bordered>
        <TheFoot hw-full />
      </n-layout-footer>
      <n-back-top
        :listen-to="refWrapper"
        :right="backTopRight"
        :bottom="backTopBottom"
        :visibility-height="backTopvisibilityHeight"
      />
    </n-layout>
  </n-layout>
</template>

