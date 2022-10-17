<script setup lang="ts">
import RecordTable from './components/RecordTable.vue'
import RecordVideo from './components/RecordVideo.vue'
import type { PageQuery } from '~/types'

let currentTab = $ref(0)
let pagination = $ref<PageQuery>()

function changeCurrentTab(
  tab: 0 | 1,
  _pagination: PageQuery,
) {
  // 保存表格的分页数据
  if (tab === 1)
    pagination = _pagination

  currentTab = tab
}

provide('changeCurrentTab', changeCurrentTab)
</script>

<template>
  <div w-full>
    <RecordTable v-if="currentTab === 0" v-bind="{ ...pagination }" />
    <RecordVideo v-else />
  </div>
</template>
