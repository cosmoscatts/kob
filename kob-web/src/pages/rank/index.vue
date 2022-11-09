<script setup lang="ts">
import { breakpointsTailwind } from '@vueuse/core'
import {
  Refresh as RefreshIcon,
  Search as SearchIcon,
  TrashBinOutline as TrashBinOutlineIcon,
} from '@vicons/ionicons5'
import { createColumns } from './helper'
import type { Rank } from '~/types'

const { loading, startLoading, endLoading } = useLoading()

// 分页参数
const pagination = usePagination({
  onChangeCallback: fetchTableData,
  onUpdatePageSizeCallback: fetchTableData,
})

/**
 * 创建表格序号
 */
function createRowNumber(rowIndex: number) {
  const { page, pageSize } = pagination
  return (page - 1) * pageSize + rowIndex + 1
}

const columns = createColumns({
  createRowNumber,
})

let tableData = $ref<Rank[]>([])
const searchModel = reactive<{ name?: string }>({ name: '' })
/**
 * 查询表格数据
 */
async function fetchTableData() {
  startLoading()
  const { page, pageSize } = pagination
  const { name } = searchModel
  try {
    const { data: { records, total } } = await RankApi.getRankList({ page, pageSize, name })
    tableData = records!
    pagination.itemCount = total!
  }
  catch (err) {
    // 处理异常
  }
  finally {
    useTimeoutFn(endLoading, 1000)
  }
}
fetchTableData()

// 是否为移动端（包含 `PC` 端宽度过小的情况）
const breakpoints = useBreakpoints(breakpointsTailwind)
const isMobile = breakpoints.smaller('sm')
const labelHidden = breakpoints.smaller('md')
</script>

<template>
  <div w-full>
    <n-card title="排行榜" hoverable>
      <template v-if="!isMobile" #header-extra>
        <div flex gap-x-2>
          <n-form-item label="玩家名称" label-placement="left" :show-label="!labelHidden" :show-feedback="false">
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
          <n-button secondary @click="searchModel.name = '' && fetchTableData()">
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
