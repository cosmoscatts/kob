<script setup lang="ts">
import { BulbOutline } from '@vicons/ionicons5'
import { createColumns } from '../columns'
import BotTableForm from './BotTableForm.vue'
import How2Code from './How2Code.vue'
import type { Bot } from '~/types'

const { loading, startLoading, endLoading } = useLoading()

const pagination = usePagination({ // 分页参数
  onChangeCallback: fetchTableData,
  onUpdatePageSizeCallback: fetchTableData,
})

let modalVisible = $ref(false)
let modalAction = $ref<'add' | 'edit'>()
let selectedBot = $ref<Bot>()

function onAddBot() {
  selectedBot = {}
  modalAction = 'add'
  modalVisible = true
}

function onUpdateBot(bot: Bot) {
  selectedBot = bot
  modalAction = 'edit'
  modalVisible = true
}

function onSaveBotData(bot: Bot) {
  const { addBot: add, updateBot: update } = BotApi
  const fn = [add, update][Number(modalAction === 'edit')]
  const msgPrefix = ['添加', '编辑'][Number(modalAction === 'edit')]
  fn(bot).then(({ code, msg }) => {
    if (code !== 0) {
      $message.error(msg ?? `${msgPrefix}失败`)
      return
    }
    $message.success(`${msgPrefix}成功`)
    modalVisible = false
    fetchTableData()
  })
}

function onRemoveBot({ id }: Bot) {
  useConfirm(
    '你确定要删除该Bot吗？',
    () => {
      BotApi
        .deleteBot(id as number)
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

const columns = createColumns({
  createRowNumber: pagination.createRowNumber,
  onUpdateBot,
  onRemoveBot,
})

let tableData = $ref<Bot[]>([])

function fetchTableData() {
  startLoading()
  const { page, pageSize } = pagination
  BotApi
    .getBotList({ page, pageSize })
    .then(({ data: { records = [], total = 0 } }) => {
      tableData = records
      pagination.itemCount = total
    })
    .finally(() => useTimeoutFn(endLoading, 1000))
}
fetchTableData()

const { isMobile } = useResponsive()
const how2CodeVisible = ref(false)
</script>

<template>
  <div w-full>
    <n-card title="我的Bot" hoverable>
      <template #header-extra>
        <n-button v-if="!isMobile" text :style="{ marginRight: '10px' }" @click="how2CodeVisible = true">
          <template #icon>
            <n-icon>
              <BulbOutline />
            </n-icon>
          </template>
          怎么编写bot代码？
        </n-button>
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
    <BotTableForm
      v-model:modal-visible="modalVisible"
      v-bind="{
        type: modalAction,
        form: selectedBot,
      }"
      @save-bot-data="onSaveBotData"
    />
    <How2Code
      v-model:visible="how2CodeVisible"
    />
  </div>
</template>
