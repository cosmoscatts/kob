<script setup lang="ts">
import {
  Refresh as RefreshIcon,
  Search as SearchIcon,
  TrashBinOutline as TrashBinOutlineIcon,
} from '@vicons/ionicons5'
import { createColumns } from '../columns'
import type { Record } from '~/types'

const {
  page = 1,
  pageSize = 10,
  name = '',
} = defineProps<{
  page?: number
  pageSize?: number
  name?: string
}>()

const changeCurrentTab = inject<Function>('changeCurrentTab')!

const { loading, startLoading, endLoading } = useLoading()

// 分页参数
const pagination = usePagination({
  page,
  pageSize,
  onChangeCallback: fetchTableData,
  onUpdatePageSizeCallback: fetchTableData,
})

function canDelete(aId: number, bId: number): boolean {
  const userId = useUserStore()?.user?.id
  return userId === aId || userId === bId
}

function onRemoveRecord({ id }: Record) {
  useConfirm(
    '你确定要删除该对局吗？',
    () => {
      RecordApi
        .deleteRecord(id as number)
        .then(({ code, msg }) => {
          if (code !== 0) {
            $message.error(msg ?? '删除失败')
            return
          }
          $message.success('删除成功')
          fetchTableData()
        })
    },
  )
}

const { updateIsRecord, updateSteps, updateGame, updateLoser } = useRecordStore()

const convert2DArray = (map: string) => { // 将地图从字符串转为二维数组
  const g: number[][] = []
  for (let i = 0, k = 0; i < 13; i++) {
    const line = []
    for (let j = 0; j < 14; j++, k++)
      line.push([1, 0][Number(map[k] === '0')])

    g.push(line)
  }
  return g
}

let tableData = $ref<Record[]>([])
const searchModel = reactive<{ name?: string }>({ name })

function fetchTableData() {
  startLoading()
  const { page, pageSize } = pagination
  RecordApi
    .getRecordList({ page, pageSize, name: searchModel.name?.trim() })
    .then(({ data: { records = [], total = 0 } }) => {
      tableData = records
      pagination.itemCount = total
    })
    .finally(() => useTimeoutFn(endLoading, 1000))
}
fetchTableData()

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
  changeCurrentTab(1, { page, pageSize }, playerInfoList, searchModel.name)
}

const columns = createColumns({
  createRowNumber: pagination.createRowNumber,
  canDelete,
  checkVideo,
  onRemoveRecord,
})

const { isMobile, labelHidden } = useResponsive()
</script>

<template>
  <div w-full>
    <n-card title="对局记录" hoverable>
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
