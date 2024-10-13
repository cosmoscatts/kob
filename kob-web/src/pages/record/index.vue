<script setup lang="ts">
import type { PlayerInfo } from './columns';
import type { PageQuery } from '~/types';
import RecordTable from './components/RecordTable.vue';
import RecordVideo from './components/RecordVideo.vue';

const currentTab = ref(0);
const name = ref<string | undefined>(''); // 搜索的玩家
const pagination = ref<PageQuery>();
const playerInfoList = ref<PlayerInfo[]>([]);

const route = useRoute();
watch(() => route.path, () => {
  currentTab.value = 0;
  name.value = undefined;
  pagination.value = {};
  playerInfoList.value = [];
});

function changeCurrentTab(
  tab: 0 | 1,
  _pagination: PageQuery,
  _playerInfoList: PlayerInfo[],
  _name = undefined,
) {
  if (tab === 1) { // 保存表格的分页数据
    name.value = _name;
    pagination.value = _pagination;
    playerInfoList.value = _playerInfoList;
  } else {
    playerInfoList.value = [];
  }
  currentTab.value = tab;
}

provide('changeCurrentTab', changeCurrentTab);
</script>

<template>
  <div w-full>
    <Transition name="fade-slide" mode="out-in" appear>
      <RecordTable v-if="currentTab === 0" v-bind="{ ...pagination, name }" />
      <RecordVideo v-else v-bind="{ playerInfoList }" />
    </Transition>
  </div>
</template>
