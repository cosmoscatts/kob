<script setup lang="ts">
import type { Ref } from 'vue'
import {
  Refresh as RefreshIcon,
  Search as SearchIcon,
  TrashBinOutline as TrashBinOutlineIcon,
} from '@vicons/ionicons5'
import { createColumns } from './helper'
import type { Rank } from '~/types'

const containerWidth = inject<Ref<number>>('containerWidth')!

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
  const { name: _name } = searchModel
  try {
    const { data: { records, total } } = await RankApi.getRankList({ page, pageSize, name: _name?.trim() })
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
</script>

<template>
  <div
    :style="{
      width: '80%',
      marginLeft: '10%',
      height: '370px',
    }"
  >
    <n-card title="排行榜" hoverable hfull>
      <template v-if="containerWidth > 500" #header-extra>
        <div flex gap-x-2>
          <n-form-item label="玩家名称" label-placement="left" :show-label="containerWidth > 800" :show-feedback="false" size="small">
            <n-input v-model:value="searchModel.name" placeholder="玩家名称" clearable :style="{ width: '120px' }">
              <template #clear-icon>
                <n-icon :component="TrashBinOutlineIcon" />
              </template>
            </n-input>
          </n-form-item>
          <n-button type="primary" text-color="white" size="small" @click="fetchTableData">
            <template #icon>
              <n-icon :component="SearchIcon" color="white" />
            </template>
            <span font-bold>查询</span>
          </n-button>
          <n-button secondary size="small" @click="searchModel.name = '' && fetchTableData()">
            <template #icon>
              <n-icon :component="RefreshIcon" />
            </template>
            <span font-bold>重置</span>
          </n-button>
        </div>
      </template>
      <n-data-table
        v-if="containerWidth > 500"
        :style="{ marginTop: '-5px' }"
        size="small"
        :loading="loading"
        :columns="columns"
        :data="tableData"
        :remote="true"
        :pagination="pagination"
        :paginate-single-page="false"
        :max-height="200"
      />
      <div
        v-else
        hw-full
        flex justify-center items-center
        text-lg
      >
        {  当前屏幕尺寸过小 :). }
      </div>
    </n-card>
  </div>
</template>
