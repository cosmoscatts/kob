<script setup lang="ts">
import { breakpointsTailwind } from '@vueuse/core'
import { createColumns, handleSaveBot } from '../helper'
import BotTableForm from './BotTableForm.vue'
import type { Bot } from '~/types'

const { message, dialog } = useGlobalNaiveApi()
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

// 是否显示『添加』、『编辑』`bot` 表单
let botModalVisible = $ref(false)
// `bot` 表单操作类型 - `add`： 新增、`edit`：编辑
let botModalAction = $ref<'add' | 'edit'>()
// 编辑用户时，选中的用户
let selectedBot = $ref<Bot>()

/**
 * 添加 `bot`
 */
function onAddBot() {
  selectedBot = {}
  botModalAction = 'add'
  botModalVisible = true
}

/**
 * 编辑 `bot`
 */
function onUpdateBot(bot: Bot) {
  selectedBot = bot
  botModalAction = 'edit'
  botModalVisible = true
}

/**
 * 保存 `bot` -『新增』&『编辑』
 */
async function onSaveBotData(bot: Bot) {
  const result = await handleSaveBot({
    type: botModalAction,
    data: bot,
  })
  if (result) {
    botModalVisible = false
    fetchTableData()
  }
}

/**
 * 删除用户
 */
function onRemoveBot({ id }: Bot) {
  dialog.warning({
    title: '警告',
    content: '你确定要删除该Bot吗？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      const { code, msg } = await BotApi.deleteBot(id as number)
      if (code !== 0) {
        message.error(msg ?? '删除失败')
        return
      }
      message.success('删除成功')
      botModalVisible = false
      fetchTableData()
    },
  })
}

const columns = createColumns({
  createRowNumber,
  onUpdateBot,
  onRemoveBot,
})

let tableData = $ref<Bot[]>([])

/**
 * 查询表格数据
 */
async function fetchTableData() {
  startLoading()
  const { page, pageSize } = pagination
  try {
    const { data: { records, total } } = await BotApi.getBotList({ page, pageSize })
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
</script>

<template>
  <div h-full>
    <n-card title="我的Bot" hoverable>
      <template #header-extra>
        <NButton type="primary" text-color="white" @click="onAddBot">
          添加Bot
        </NButton>
      </template>
      <n-data-table
        v-if="!isMobile"
        size="small"
        :loading="loading"
        :columns="columns"
        :data="tableData"
        :pagination="pagination"
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
    <BotTableForm
      v-model:modal-visible="botModalVisible"
      v-bind="{
        type: botModalAction,
        form: selectedBot,
      }"
      @save-bot-data="onSaveBotData"
    />
  </div>
</template>
