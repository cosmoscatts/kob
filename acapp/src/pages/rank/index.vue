<script setup lang="ts">
import type { Ref } from 'vue'
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

/**
 * 查询表格数据
 */
async function fetchTableData() {
  startLoading()
  const { page, pageSize } = pagination
  try {
    const { data: { records, total } } = await RankApi.getRankList({ page, pageSize })
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
    pt-20px
    :style="{
      width: containerWidth < 768 ? '96%' : '80%',
      marginLeft: containerWidth < 768 ? '2%' : '10%',
      maxHeight: '70%',
    }"
  >
    <n-card title="排行榜" hoverable>
      <n-data-table
        v-if="containerWidth >= 768"
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
        flex justify-center items-center
        text-lg
      >
        {  当前屏幕尺寸过小 :). }
      </div>
    </n-card>
  </div>
</template>
