<script setup lang="ts">
import { Machine, Match } from './components'
import { appLayout } from '~/config'

const { navHeight, footHeight, contentPadding } = appLayout
const diffHeight = computed(() => navHeight + footHeight + contentPadding * 2 + 1 + 1 + 6)

const borderColor = computed(() => isDark.value ? 'rgba(255, 255, 255, 0.09)' : 'rgb(239, 239, 245)')

let currentPageIndex = $ref(0)
currentPageIndex = 0
</script>

<template>
  <div>
    <div
      v-if="currentPageIndex === 0"
      flex-y-center justify-between w60vw mxa rounded-10px
      :style="{
        minHeight: `calc(100vh - ${diffHeight}px)`,
        border: `3px solid ${borderColor}`,
        padding: '0 8vw',
      }"
    >
      <div w25vw h40vh text-5vw text-center lh-40vh border="1 red" cursor-pointer font-800 @click="currentPageIndex = 1">
        <span text-7vw>匹</span>配
      </div>
      <div w25vw h40vh text-5vw text-center lh-40vh border="1 red" cursor-pointer font-800>
        <span text-7vw>人</span>机
      </div>
    </div>
    <Match v-if="currentPageIndex === 1" />
    <Machine v-if="currentPageIndex === 2" />
  </div>
</template>
