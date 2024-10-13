<script setup lang="ts">
import type { UserSecurity } from '~/types';
import SecurityModal from './security/SecurityModal.vue';

const security = ref<UserSecurity>({});

const checkUserHasPassword = () => {
  UserSecurityApi.checkSecurity().then(({ data = {} }) => security.value = data);
};
checkUserHasPassword();

const modalVisible = ref(false);
const modalTitle = ref('');
const formIndex = ref(0);
type T = 'password' | 'phone' | 'github' | 'qq' | 'wx' | 'bilibili';
function onClick(type: T) {
  const hash = ['password', 'phone', 'github', 'qq', 'wx', 'bilibili'];
  const title = ['密码', '手机号', 'Github', 'QQ', '微信', 'Bilibili'];
  const index = hash.findIndex(i => i === type);
  if (!~index)
    return;
  modalVisible.value = true;
  modalTitle.value = `设置${title[index]}`;
  formIndex.value = index;
}
</script>

<template>
  <div w-full>
    <n-card
      title="安全设置"
      hoverable
      :segmented="{
        content: 'soft',
      }"
    >
      <div h-30px flex justify-between items-center>
        <div flex items-center>
          <div i-carbon-locked mr-2 />
          密码
        </div>
        <div>
          {{ security.hasPassword ? '已设置' : '未设置' }}
        </div>
        <n-button quaternary type="primary" @click="onClick('password')">
          {{ security.hasPassword ? '修改密码' : '设置密码' }}
        </n-button>
      </div>
      <n-divider />
      <div h-30px flex justify-between items-center>
        <div flex items-center>
          <div i-ant-design-mobile-outlined mr-2 />
          手机号
        </div>
        <div>
          {{ security.hasPhone ? '已绑定' : '未绑定' }}
        </div>
        <n-button quaternary type="primary" @click="onClick('phone')">
          {{ security.hasPhone ? '修改手机号' : '绑定手机号' }}
        </n-button>
      </div>
      <n-divider />
      <div h-30px flex justify-between items-center>
        <div flex items-center>
          <div i-ant-design-github-outlined mr-2 />
          Github
        </div>
        <div>
          {{ security.hasGithub ? '已绑定' : '未绑定' }}
        </div>
        <n-button quaternary type="primary" @click="onClick('github')">
          {{ security.hasGithub ? '解除绑定' : '绑定Github' }}
        </n-button>
      </div>
      <n-divider />
      <div h-30px flex justify-between items-center>
        <div flex items-center>
          <div i-ant-design-qq-outlined mr-2 />
          QQ
        </div>
        <div>
          {{ security.hasQQ ? '已绑定' : '未绑定' }}
        </div>
        <n-button quaternary type="primary" @click="onClick('qq')">
          {{ security.hasQQ ? '解除绑定' : '绑定QQ' }}
        </n-button>
      </div>
      <n-divider />
      <div h-30px flex justify-between items-center>
        <div flex items-center>
          <div i-ant-design-wechat-filled mr-2 />
          微信
        </div>
        <div>
          {{ security.hasWechat ? '已绑定' : '未绑定' }}
        </div>
        <n-button quaternary type="primary" @click="onClick('wx')">
          {{ security.hasWechat ? '解除绑定' : '绑定微信' }}
        </n-button>
      </div>
      <n-divider />
      <div h-30px flex justify-between items-center>
        <div flex items-center>
          <div i-ri-bilibili-fill mr-2 />
          Bilibili
        </div>
        <div>
          {{ security.hasWechat ? '已绑定' : '未绑定' }}
        </div>
        <n-button quaternary type="primary" @click="onClick('bilibili')">
          {{ security.hasWechat ? '解除绑定' : '绑定Bilibili' }}
        </n-button>
      </div>
      <n-divider />
    </n-card>
    <SecurityModal v-model:modal-visible="modalVisible" v-bind="{ formIndex, title: modalTitle }" />
  </div>
</template>
