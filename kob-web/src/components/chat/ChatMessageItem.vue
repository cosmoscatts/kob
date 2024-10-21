<script setup lang="ts">
import defaultAvatar from '~/assets/default-avatar.png';
import systemAvatar from '~/assets/logo.png';
import type { User } from '~/types';

const props = withDefaults(defineProps<{
  type?: string
  timestamp?: string
  content?: string
  user?: User
  online?: boolean
  owner?: boolean
}>(), {
  type: 'CHAT',
  timestamp: '',
  content: '',
  online: false,
  owner: false,
});

const isSystem = computed(() => props.type === 'SYSTEM');
const placement = computed(() => props.owner ? 'right' : 'left');
const backgroundColor = computed(() => props.owner ? '#4ade80' : '#60a5fa');
const avatar = computed(() => isSystem.value ? systemAvatar : props.user?.avatar || defaultAvatar);
const userName = computed(() => isSystem.value ? '系统消息' : props.user?.name || '匿名用户');
</script>

<template>
  <div flex="~ col" :class="placement === 'right' ? 'items-end' : 'items-start'">
    <div flex items-center py-5px>
      <div flex gap-3 items-center>
        <img h-24px w-24px rounded-full :src="avatar">

        <n-ellipsis style="max-width: 200px; font-size: 0.875rem;">
          {{ userName }}
        </n-ellipsis>

        <div flex items-center gap-1>
          <span :class="isSystem ? 'text-yellow' : online ? 'bg-green' : 'bg-gray'" w2 h2 rounded-full />
          <span v-if="!isSystem" :class="online ? 'text-green' : 'text-gray'" text-sm>{{ online ? '在线' : '离线' }}</span>
        </div>

        <span text-sm op-50>{{ timestamp }}</span>
      </div>
    </div>

    <div class="message-bubble">
      {{ content }}
    </div>
  </div>
</template>

<style scoped>
.message-bubble {
  background: v-bind(backgroundColor);
  border-radius: 5px;
  padding: 10px 15px;
  max-width: 70%;
  word-wrap: break-word;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  font-size: 14px;
  line-height: 1.4;
  margin-top: 4px;
  position: relative;
  color: white;
}

.message-bubble::before {
  content: '';
  position: absolute;
  top: -6px;
  border-style: solid;
  border-width: 0 8px 8px;
  border-color: transparent transparent v-bind(backgroundColor) transparent;
}

div[flex="~ col"].items-start .message-bubble::before {
  left: 12px;
}

div[flex="~ col"].items-end .message-bubble::before {
  right: 12px;
}
</style>
