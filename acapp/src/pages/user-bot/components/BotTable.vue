<script setup lang="ts">
import type { Ref } from 'vue'
import { createColumns, handleSaveBot } from '../helper'
import BotTableForm from './BotTableForm.vue'
import type { Bot } from '~/types'

const containerWidth = inject<Ref<number>>('containerWidth')!

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
 * 删除用户 `bot`
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
</script>

<template>
  <div w-full hfull>
    <n-card title="我的Bot" hoverable hfull>
      <template #header-extra>
        <NButton type="primary" text-color="white" @click="onAddBot">
          添加Bot
        </NButton>
      </template>
      <n-data-table
        v-if="containerWidth > 500"
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
        wfull hfull
        flex justify-center items-center
        text-lg
      >
        {  当前屏幕尺寸过小 :). }
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
