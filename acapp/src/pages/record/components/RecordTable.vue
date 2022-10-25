<script setup lang="ts">
import type { Ref } from 'vue'
import { createColumns } from '../helper'
import type { Record } from '~/types'

const {
  page = 1,
  pageSize = 10,
} = defineProps<{
  page?: number
  pageSize?: number
}>()

const containerWidth = inject<Ref<number>>('containerWidth')!
const changeCurrentTab = inject<Function>('changeCurrentTab')!

const { message, dialog } = useGlobalNaiveApi()
const { loading, startLoading, endLoading } = useLoading()

// 分页参数
const pagination = usePagination({
  page,
  pageSize,
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

/**
 * 是否有删除权限
 */
function canDelete(aId: number, bId: number): boolean {
  const userId = useUserStore()?.user?.id
  return userId === aId || userId === bId
}

/**
 * 删除对局
 */
function onRemoveRecord({ id }: Record) {
  dialog.warning({
    title: '警告',
    content: '你确定要删除该对局吗？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      const { code, msg } = await RecordApi.deleteRecord(id as number)
      if (code !== 0) {
        message.error(msg ?? '删除失败')
        return
      }
      message.success('删除成功')
      fetchTableData()
    },
  })
}

const { updateIsRecord, updateSteps, updateGame, updateLoser } = useRecordStore()

/** 将地图从字符串转为二维数组 */
const convert2DArray = (map: string) => {
  const g: number[][] = []
  for (let i = 0, k = 0; i < 13; i++) {
    const line = []
    for (let j = 0; j < 14; j++, k++) {
      if (map[k] === '0')
        line.push(0)
      else line.push(1)
    }
    g.push(line)
  }
  return g
}

/**
 * 查看录像
 */
function checkVideo({ aId, aSx, aSy, bId, bSx, bSy, map, aSteps, bSteps, loser, aAvatar, aName, bAvatar, bName }: Record) {
  updateIsRecord(true)
  updateSteps(aSteps, bSteps)
  updateGame({ aId, aSx, aSy, bId, bSx, bSy, map: convert2DArray(map) })
  updateLoser(loser as 'A' | 'B' | 'all' | 'none')

  const { page, pageSize } = pagination
  const playerInfoList = [
    { name: aName, avatar: aAvatar },
    { name: bName, avatar: bAvatar },
  ]
  changeCurrentTab(1, { page, pageSize }, playerInfoList)
}

const columns = createColumns({
  createRowNumber,
  canDelete,
  checkVideo,
  onRemoveRecord,
})

let tableData = $ref<Record[]>([])

/**
 * 查询表格数据
 */
async function fetchTableData() {
  startLoading()
  const { page, pageSize } = pagination
  try {
    const { data: { records, total } } = await RecordApi.getRecordList({ page, pageSize })
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
  <div w-full hfull>
    <n-card title="对局记录" hoverable hfull>
      <n-data-table
        v-if="containerWidth > 500"
        size="small"
        :loading="loading"
        :columns="columns"
        :data="tableData"
        :remote="true"
        :pagination="pagination"
        :paginate-single-page="false"
        :max-height="250"
      />
      <div
        v-else
        hfull wfull text-lg
        flex justify-center items-center
      >
        {  当前屏幕尺寸过小 :). }
      </div>
    </n-card>
  </div>
</template>
