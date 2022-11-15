<script setup lang="ts">
import { Machine, Match } from './components'
import { appLayout } from '~/config'

const { navHeight, footHeight, contentPadding } = appLayout
const diffHeight = computed(() => navHeight + footHeight + contentPadding * 2 + 1 + 1 + 3)

let currentPageIndex = $ref(0)
currentPageIndex = 1
</script>

<template>
  <div>
    <div
      v-if="currentPageIndex === 0"
      flex="col center" border="1 solid []" w60vw
      :style="{ minHeight: `calc(100vh - ${diffHeight}px)` }"
    >
      1
    </div>
    <Match v-if="currentPageIndex === 1" />
    <Machine v-if="currentPageIndex === 2" />
  </div>
</template>
