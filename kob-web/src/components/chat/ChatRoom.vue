<script setup lang="ts">
import type { User } from '~/types';
import ChatMessageItem from './ChatMessageItem.vue';

const el = ref();
const isHovered = useElementHover(el);
const visible = ref(false);
const userStore = useUserStore();

const currentMessage = ref('');
const chatSocket = ref<WebSocket | null>(null);
const messageData = ref<MessageData[]>([]);
const onlineUserIds = ref<string[]>([]);
const userInfoMap = new Map();

interface MessageData {
  type: string
  userId?: string
  content?: string
  action?: string
  timestamp?: string
  user?: User
}

async function getUser(userId: string) {
  let user;
  if (userInfoMap.has(userId)) {
    user = userInfoMap.get(userId);
  } else {
    const result = await UserApi.getUserInfoById(userId);
    if (result.data.data) {
      user = result.data.data;
      userInfoMap.set(userId, user);
    }
  }
  return user;
}

function connect() {
  if (!chatSocket.value) {
    chatSocket.value = useChatSocket(async (msg) => {
      msg = JSON.parse(msg.data);
      if (msg.type === 'USER_LIST') {
        onlineUserIds.value = msg.users || [];
      } else if (msg.type === 'SYSTEM') {
        const { type, userId, action = '', timestamp = '' } = msg;
        const user = await getUser(userId);
        const owner = !!userStore.user?.id && String(userStore.user?.id) === userId;
        const content = action
          ? `${owner ? '您' : user?.name || ''}${action}`
          : '';
        messageData.value.push({
          type,
          content,
          timestamp,
        });
      } else if (msg.type === 'CHAT') {
        const { type, userId, content = '', timestamp = '' } = msg;
        const user = await getUser(userId);
        messageData.value.push({
          type,
          userId,
          content,
          timestamp,
          user,
        });
      }
    });
  }
}

function disconnect() {
  if (chatSocket.value) {
    chatSocket.value.close();
  }
  chatSocket.value = null;
  visible.value = false;
  currentMessage.value = '';
  messageData.value = [];
  onlineUserIds.value = [];
  userInfoMap.clear();
}

function toggleRoom() {
  visible.value = !visible.value;
  if (visible.value) {
    connect();
  }
}

function sendMessage() {
  if (!chatSocket.value) {
    $message.error('当前未连接到聊天室！');
    return;
  }
  if (!currentMessage.value) {
    $message.warning('请输入消息文本！');
    return;
  }
  chatSocket.value.send(JSON.stringify({
    content: currentMessage.value,
  }));
  currentMessage.value = '';
}
</script>

<template>
  <div
    v-if="userStore.isLoggedIn" ref="el" class="group"
    fixed bottom-60px right-0 rounded-l-32px h-36px px-8px py-0 cursor-pointer
    flex items-center gap-6px select-none box-border bg-white dark:bg-hex-202020
    border-1 border-solid border="#ffffff0a"
    text-hex-663399 shadow-lg dark:text-hex-9880f8 transition-all duration-200 ease-in-out
    hover="bg-#dfd5fe dark:bg-#3f3663"
    :class="visible ? 'right-502px' : ''"
    @click="toggleRoom"
  >
    <div i-tabler:brand-hipchat size-26px />

    <div
      v-if="isHovered"
      font-1000 text-16px
    >
      畅所欲言
    </div>
  </div>

  <n-drawer v-model:show="visible" :width="502" placement="right" :show-mask="false" :mask-closable="false">
    <n-drawer-content>
      <template #header>
        <div flex items-center justify-between>
          <div>KOB 聊天大厅</div>

          <n-button type="error" @click="disconnect">
            离开聊天大厅
          </n-button>
        </div>
      </template>

      <div h-full flex="~ col">
        <div flex="~ 1 col gap-3" of-x-hidden of-y-auto pr-10px pb-20px>
          <ChatMessageItem
            v-for="item, index in messageData"
            :key="index"
            :type="item.type"
            :timestamp="item.timestamp"
            :content="item.content"
            :user="item.user"
            :online="!!item.userId && onlineUserIds.includes(item.userId)"
            :owner="!!item.userId && userStore.user?.id === Number(item.userId)"
          />
        </div>

        <div flex gap-2 items-center>
          <n-input v-model:value="currentMessage" type="textarea" placeholder="消息文本" maxlength="100" show-count :autosize="{ maxRows: 4, minRows: 1 }" />
          <n-button type="primary" @click="sendMessage">
            发送
          </n-button>
        </div>
      </div>
    </n-drawer-content>
  </n-drawer>
</template>
