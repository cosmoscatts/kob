<script setup lang="ts">
import { BulbOutline } from '@vicons/ionicons5';
import type { Bot } from '~/types';
import { createColumns } from '../utils/columns';
import BotCodeExampleDrawer from './BotCodeExampleDrawer.vue';
import BotFormModal from './BotFormModal.vue';

const { loading, startLoading, endLoading } = useLoading();

const pagination = usePagination({
  onChangeCallback: fetchTableData,
  onUpdatePageSizeCallback: fetchTableData,
});

const modalVisible = ref(false);
const modalAction = ref<'add' | 'edit'>();
const selectedBot = ref<Bot>({});

const tableData = ref<Bot[]>([]);
const botCodeExampleDrawerVisible = ref(false);

const { isMobile } = useResponsive();

const columns = createColumns({
  createRowNumber: pagination.createRowNumber,
  onUpdateBot,
  onRemoveBot,
});

function onAddBot() {
  selectedBot.value = {};
  modalAction.value = 'add';
  modalVisible.value = true;
}

function onUpdateBot(bot: Bot) {
  selectedBot.value = bot;
  modalAction.value = 'edit';
  modalVisible.value = true;
}

async function onSaveBotData(bot: Bot) {
  const fn = modalAction.value === 'edit' ? BotApi.updateBot : BotApi.addBot;
  const msgPrefix = modalAction.value === 'edit' ? '编辑' : '添加';
  try {
    const result = await fn(bot);
    const { code, msg } = result.data;
    if (code !== 0) {
      $message.error(msg || `${msgPrefix}失败`);
      return;
    }
    $message.success(`${msgPrefix}成功`);
    modalVisible.value = false;
    fetchTableData();
  } catch (e) {
    console.error(e);
    $message.error(`${msgPrefix}失败`);
  }
}

function onRemoveBot({ id }: Bot) {
  useConfirm(
    '你确定要删除该Bot吗？',
    async () => {
      try {
        const result = await BotApi.deleteBot(id as number);
        const { code, msg } = result.data;
        if (code !== 0) {
          $message.error(msg || '删除失败');
          return;
        }
        $message.success('删除成功');
        fetchTableData();
      } catch (e) {
        console.error(e);
        $message.error('删除失败');
      }
    },
  );
}

async function fetchTableData() {
  startLoading();
  const { page, pageSize } = pagination;
  try {
    const result = await BotApi.getBotList({ page, pageSize });
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

onMounted(fetchTableData);
</script>

<template>
  <div w-full>
    <n-card title="我的Bot" hoverable>
      <template #header-extra>
        <n-button v-if="!isMobile" text :style="{ marginRight: '10px' }" @click="botCodeExampleDrawerVisible = true">
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
      <div v-else hw-full flex-center text-lg>
        { 请在客户端访问 :). }
      </div>
    </n-card>
    <BotFormModal
      v-model:modal-visible="modalVisible"
      :type="modalAction"
      :form="selectedBot"
      @save-bot-data="onSaveBotData"
    />
    <BotCodeExampleDrawer v-model:visible="botCodeExampleDrawerVisible" />
  </div>
</template>
