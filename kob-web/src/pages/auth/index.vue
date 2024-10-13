<script setup lang="ts">
import type { Ref } from 'vue';
import type { Tab } from './components';
import { LoginAccount, LoginPhone, Register } from './components';

const themeDark = isDark;
const router = useRouter();

const userStore = useUserStore();
const { authModalVisible } = storeToRefs(userStore);
const { login, setAuthModalVisible } = userStore;

const bodyStyle = {
  width: '390px',
};

const currentTab = ref<Tab>('account');

const title = computed(() => {
  const { value: tab } = currentTab;
  return {
    account: '账密登录',
    phone: '验证码登录',
    register: '用户注册',
  }[tab];
});

function changeTab(tab: Tab) {
  currentTab.value = tab;
}

function close() {
  currentTab.value = 'account';
  setAuthModalVisible(false);
}

const refLoginAccount = ref();
const refLoginPhone = ref();
const refRegister = ref();

/**
 * 实现 form 显示时，input 框自动 focus
 */
function inputAutoFocus() {
  const refMap: Record<Tab, Ref> = {
    account: refLoginAccount,
    phone: refLoginPhone,
    register: refRegister,
  };
  refMap[currentTab.value]?.value?.focusFirstInput();
}

watch(authModalVisible, (val) => {
  if (val)
    useTimeoutFn(inputAutoFocus, 200);
});
watch(currentTab, () => useTimeoutFn(inputAutoFocus, 200));

function loginCallback(token: string) {
  router.push('/');
  login(token);
  $notification.success({
    title: '登录成功',
    content: '欢迎使用，快来一场伟大的战斗吧~',
    duration: 3000,
  });
  setAuthModalVisible(false);
}

function registerCallback() {
  $notification.success({
    title: '注册成功',
    content: '快去登录把~',
    duration: 3000,
  });
  currentTab.value = 'account';
}

provide('tab', currentTab);
provide('changeTab', changeTab);
provide('loginCallback', loginCallback);
provide('registerCallback', registerCallback);
</script>

<template>
  <n-button type="primary" :secondary="themeDark" @click="setAuthModalVisible(true)">
    登录 <n-divider vertical /> 注册
  </n-button>
  <n-modal
    :show="authModalVisible"
    size="huge"
    preset="card"
    :style="bodyStyle"
    :bordered="true"
    @close="close"
  >
    <template #header>
      <span text="1.5rem" font-bold>{{ title }}</span>
    </template>
    <LoginAccount v-if="currentTab === 'account'" ref="refLoginAccount" />
    <LoginPhone v-if="currentTab === 'phone'" ref="refLoginPhone" />
    <Register v-if="currentTab === 'register'" ref="refRegister" />
  </n-modal>
</template>
