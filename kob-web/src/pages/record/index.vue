<script setup lang="ts">
import RecordTable from './components/RecordTable.vue'
import RecordVideo from './components/RecordVideo.vue'
import type { PlayerInfo } from './helper'
import type { PageQuery } from '~/types'

let currentTab = $ref(0)
let name = $ref<string | undefined>('') // 搜索的玩家
let pagination = $ref<PageQuery>()
let playerInfoList = $ref<PlayerInfo[]>([])

const route = useRoute()
watch(() => route.path, () => {
  currentTab = 0
  name = undefined
  pagination = {}
  playerInfoList = []
})

function changeCurrentTab(
  tab: 0 | 1,
  _pagination: PageQuery,
  _playerInfoList: PlayerInfo[],
  _name = undefined,
) {
  if (tab === 1) { // 保存表格的分页数据
    name = _name
    pagination = _pagination
    playerInfoList = _playerInfoList
  }
  else {
    playerInfoList = []
  }

  currentTab = tab
}

provide('changeCurrentTab', changeCurrentTab)
</script>

<template>
  <div w-full>
    <Transition name="fade-slide" mode="out-in" appear>
      <RecordTable v-if="currentTab === 0" v-bind="{ ...pagination, name }" />
      <RecordVideo v-else v-bind="{ playerInfoList }" />
    </Transition>
  </div>
</template>
