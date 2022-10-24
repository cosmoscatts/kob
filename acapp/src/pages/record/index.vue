<script setup lang="ts">
import type { Ref } from 'vue'
import RecordTable from './components/RecordTable.vue'
import RecordVideo from './components/RecordVideo.vue'
import type { PlayerInfo } from './helper'
import type { PageQuery } from '~/types'

const containerWidth = inject<Ref<number>>('containerWidth')!

let currentTab = $ref(0)
let pagination = $ref<PageQuery>()
let playerInfoList = $ref<PlayerInfo[]>([])

function changeCurrentTab(
  tab: 0 | 1,
  _pagination: PageQuery,
  _playerInfoList: PlayerInfo[],
) {
  // 保存表格的分页数据
  if (tab === 1) {
    pagination = _pagination
    playerInfoList = _playerInfoList
  }
  else {
    pagination = {}
    playerInfoList = []
  }

  currentTab = tab
}

provide('changeCurrentTab', changeCurrentTab)
</script>

<template>
  <div
    pt-20px
    :style="{
      width: containerWidth < 768 ? '96%' : '80%',
      marginLeft: containerWidth < 768 ? '2%' : '10%',
      height: '70%',
    }"
  >
    <RecordTable v-if="currentTab === 0" v-bind="{ ...pagination }" />
    <RecordVideo v-else v-bind="{ playerInfoList }" />
  </div>
</template>
