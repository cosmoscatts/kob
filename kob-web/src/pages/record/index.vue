<script setup lang="ts">
import type { PlayerInfo } from './utils/types';
import type { PageQuery } from '~/types';
import RecordTable from './components/RecordTable.vue';
import RecordVideo from './components/RecordVideo.vue';

interface TabChangeParams {
  pagination?: PageQuery
  playerInfoList?: PlayerInfo[]
  name?: string
}

const currentTab = ref(0);
const name = ref('');
const pagination = ref<PageQuery>({});
const playerInfoList = ref<PlayerInfo[]>([]);

const route = useRoute();

watch(() => route.path, () => {
  currentTab.value = 0;
  name.value = '';
  pagination.value = {};
  playerInfoList.value = [];
}, { immediate: true });

const currentTabComponent = computed(() =>
  currentTab.value === 0 ? RecordTable : RecordVideo,
);

function changeCurrentTab(
  tab: 0 | 1,
  { pagination: _pagination, playerInfoList: _playerInfoList, name: _name }: TabChangeParams = {},
) {
  if (tab === 1) {
    name.value = _name ?? '';
    pagination.value = _pagination ?? {};
    playerInfoList.value = _playerInfoList ?? [];
  } else {
    playerInfoList.value = [];
  }
  currentTab.value = tab;
}

provide('changeCurrentTab', changeCurrentTab);
</script>

<template>
  <div class="w-full">
    <Transition name="fade-slide" mode="out-in" appear>
      <div>
        <component
          :is="currentTabComponent"
          v-bind="currentTab === 0 ? { ...pagination, name } : { playerInfoList }"
        />
      </div>
    </Transition>
  </div>
</template>
