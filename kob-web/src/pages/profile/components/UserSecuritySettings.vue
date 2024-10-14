<script setup lang="ts">
import type { UserSecurity } from '~/types';
import SecuritySettingsModal from './security/SecuritySettingsModal.vue'; // 假设这是你的API导入

const security = ref<UserSecurity>({});
const modalVisible = ref(false);
const modalTitle = ref('');
const formIndex = ref(0);

type SecurityType = 'password' | 'phone' | 'github' | 'qq' | 'wx' | 'bilibili';
type SecurityProperty = 'hasPassword' | 'hasPhone' | 'hasGithub' | 'hasQQ' | 'hasWechat' | 'hasBilibili';

interface SecurityItem {
  type: SecurityType
  icon: string
  label: string
  property: SecurityProperty
}

const securityItems: SecurityItem[] = [
  { type: 'password', icon: 'i-carbon-locked', label: '密码', property: 'hasPassword' },
  { type: 'phone', icon: 'i-ant-design-mobile-outlined', label: '手机号', property: 'hasPhone' },
  { type: 'github', icon: 'i-ant-design-github-outlined', label: 'Github', property: 'hasGithub' },
  { type: 'qq', icon: 'i-ant-design-qq-outlined', label: 'QQ', property: 'hasQQ' },
  { type: 'wx', icon: 'i-ant-design-wechat-filled', label: '微信', property: 'hasWechat' },
  { type: 'bilibili', icon: 'i-ri-bilibili-fill', label: 'Bilibili', property: 'hasBilibili' },
];

const checkUserSecurity = async () => {
  try {
    const result = await UserSecurityApi.checkSecurity();
    security.value = result.data.data || {};
  } catch (error) {
    console.error(error);
    security.value = {};
  }
};

const onClick = (type: SecurityType) => {
  const item = securityItems.find(item => item.type === type);
  if (!item)
    return;

  modalVisible.value = true;
  modalTitle.value = `设置${item.label}`;
  formIndex.value = securityItems.indexOf(item);
};

const getSecurityStatus = (property: SecurityProperty) => {
  return security.value[property] ?? false;
};

onMounted(checkUserSecurity);
</script>

<template>
  <div w-full>
    <n-card
      title="安全设置"
      hoverable
      :segmented="{ content: 'soft' }"
    >
      <template v-for="item in securityItems" :key="item.type">
        <div h-30px flex justify-between items-center>
          <div flex items-center>
            <div mr-2 :class="[item.icon]" />
            {{ item.label }}
          </div>
          <div>
            {{ getSecurityStatus(item.property) ? '已设置' : '未设置' }}
          </div>
          <n-button quaternary type="primary" @click="onClick(item.type)">
            {{ getSecurityStatus(item.property) ? `修改${item.label}` : `设置${item.label}` }}
          </n-button>
        </div>
        <n-divider v-if="item !== securityItems[securityItems.length - 1]" />
      </template>
    </n-card>
    <SecuritySettingsModal
      v-model:modal-visible="modalVisible"
      :form-index="formIndex"
      :title="modalTitle"
    />
  </div>
</template>
