<script setup lang="ts">
import type { Ref } from 'vue';
import type { AuthTab } from '../utils';

const tab = inject<Ref<AuthTab>>('tab')!;
const changeTab = inject<(Function)>('changeTab');

const actionName = computed(() => {
  return ({
    account: '验证码登录',
    phone: '其他方式登录',
    register: '账密登录',
  } as Record<AuthTab, string>)[tab.value ?? 'account'];
});
const go2 = () => {
  const map = { account: 'phone', phone: 'account', register: 'account' };
  changeTab?.(map[tab.value ?? 'account']);
};
const register = () => changeTab?.('register');
const onClick = () => $message.warning('需要备案，所以不实现嘻嘻 (┬┬﹏┬┬)');
</script>

<template>
  <div flex="~ col" w-full>
    <div
      mt-15px
      flex-y-center justify-between
      :class="{ '!justify-center': tab === 'register' }"
    >
      <span text-primary cursor-pointer @click="go2">{{ actionName }}</span>
      <span v-if="tab !== 'register'" text-primary cursor-pointer @click="register">还未注册？</span>
    </div>
    <div v-if="tab === 'account'" w-full flex-center gap-x-10 mt-15px>
      <n-tooltip placement="bottom">
        <template #trigger>
          <n-button color="#44006F" circle @click="onClick">
            <template #icon>
              <div i-ri-github-fill text-white />
            </template>
          </n-button>
        </template>
        <span> Github 登录 </span>
      </n-tooltip>
      <n-tooltip placement="bottom">
        <template #trigger>
          <n-button color="#12B7F5" circle @click="onClick">
            <template #icon>
              <div i-ri-qq-fill text-white />
            </template>
          </n-button>
        </template>
        <span> QQ 登录 </span>
      </n-tooltip>
      <n-tooltip placement="bottom">
        <template #trigger>
          <n-button color="#04D167" circle @click="onClick">
            <template #icon>
              <div i-ri-wechat-fill text-white />
            </template>
          </n-button>
        </template>
        <span> 微信登录 </span>
      </n-tooltip>
      <n-tooltip placement="bottom">
        <template #trigger>
          <n-button color="#F25D8E" circle @click="onClick">
            <template #icon>
              <div i-ri-bilibili-fill text-white />
            </template>
          </n-button>
        </template>
        <span> 小破站登录 </span>
      </n-tooltip>
    </div>
    <div flex-center mt-15px>
      <DarkToggle />
    </div>
    <div mt-15px text-14px>
      注册登录即表示同意 <span text-primary>用户协议</span> 、 <span text-primary>隐私政策</span>
    </div>
  </div>
</template>
