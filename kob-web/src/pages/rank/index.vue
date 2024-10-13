<script setup lang="ts">
import {
  Refresh as RefreshIcon,
  Search as SearchIcon,
  TrashBinOutline as TrashBinOutlineIcon,
} from '@vicons/ionicons5';
import type { Rank } from '~/types';
import { createColumns } from './columns';

const { loading, startLoading, endLoading } = useLoading();

const pagination = usePagination({ // 分页参数
  onChangeCallback: fetchTableData,
  onUpdatePageSizeCallback: fetchTableData,
});

const columns = createColumns({
  createRowNumber: pagination.createRowNumber,
});

const tableData = ref<Rank[]>([]);
const searchModel = reactive<{ name?: string }>({ name: '' });

async function fetchTableData() {
  startLoading();
  const { page, pageSize } = pagination;
  try {
    const result = await RankApi.getRankList({ page, pageSize, name: searchModel.name?.trim() });
    const { data: { records = [], total = 0 } } = result.data;
    tableData.value = records;
    pagination.itemCount = total;
  } catch (e) {
    console.error(e);
    tableData.value = [];
    pagination.itemCount = 0;
  } finally {
    useTimeoutFn(endLoading, 1000);
  }
}
fetchTableData();

const { isMobile } = useResponsive();

function reset() {
  searchModel.name = '';
  fetchTableData();
}
</script>

<template>
  <div w-full>
    <n-card title="排行榜" hoverable>
      <template v-if="!isMobile" #header-extra>
        <div flex gap-x-2>
          <n-form-item label="玩家名称" label-placement="left" :show-feedback="false">
            <n-input v-model:value="searchModel.name" placeholder="玩家名称" clearable :style="{ width: '150px' }">
              <template #clear-icon>
                <n-icon :component="TrashBinOutlineIcon" />
              </template>
            </n-input>
          </n-form-item>
          <n-button type="primary" text-color="white" @click="fetchTableData">
            <template #icon>
              <n-icon :component="SearchIcon" color="white" />
            </template>
            <span font-bold>查询</span>
          </n-button>
          <n-button secondary @click="reset">
            <template #icon>
              <n-icon :component="RefreshIcon" />
            </template>
            <span font-bold>重置</span>
          </n-button>
        </div>
      </template>
      <n-data-table
        v-if="!isMobile"
        size="small"
        :loading="loading"
        :columns="columns"
        :data="tableData"
        :remote="true"
        :pagination="pagination"
        :paginate-single-page="false"
      />
      <div
        v-else
        hw-full
        flex-center
        text-lg
      >
        {  请在客户端访问 :). }
      </div>
    </n-card>
  </div>
</template>
