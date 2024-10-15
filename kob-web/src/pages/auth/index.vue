<script setup lang="ts">
import type { Ref } from 'vue';
import type { AuthTab } from './utils';
import AccountLogin from './components/login-form/AccountLogin.vue';
import PhoneLogin from './components/login-form/PhoneLogin.vue';
import RegisterForm from './components/RegisterForm.vue';

const route = useRoute();
const router = useRouter();

const userStore = useUserStore();
const { isAuthModalVisible } = storeToRefs(userStore);
const { login, setAuthModalVisibility } = userStore;

const bodyStyle = { width: '390px' };

const currentTab = ref<AuthTab>('account');

const title = computed(() => ({
  account: '账密登录',
  phone: '验证码登录',
  register: '用户注册',
}[currentTab.value]));

const changeTab = (tab: AuthTab) => {
  currentTab.value = tab;
};

const close = () => {
  currentTab.value = 'account';
  setAuthModalVisibility(false);
};

const refMap: Record<AuthTab, Ref> = {
  account: ref(),
  phone: ref(),
  register: ref(),
};

const inputAutoFocus = () => {
  refMap[currentTab.value]?.value?.focusFirstInput();
};

watch(isAuthModalVisible, (val) => {
  if (val)
    useTimeoutFn(inputAutoFocus, 200);
});

watch(currentTab, () => useTimeoutFn(inputAutoFocus, 200));

const loginCallback = (token: string) => {
  const path = route.query?.redirect as string || '/';
  router.push(path);
  login(token);
  $notification.success({
    title: '登录成功',
    content: '欢迎使用，快来一场伟大的战斗吧~',
    duration: 3000,
  });
  setAuthModalVisibility(false);
};

const registerCallback = () => {
  $notification.success({
    title: '注册成功',
    content: '快去登录把~',
    duration: 3000,
  });
  currentTab.value = 'account';
};

provide('tab', currentTab);
provide('changeTab', changeTab);
provide('loginCallback', loginCallback);
provide('registerCallback', registerCallback);
</script>

<template>
  <n-button type="primary" :secondary="isDark" @click="setAuthModalVisibility(true)">
    登录 <n-divider vertical /> 注册
  </n-button>
  <n-modal
    :show="isAuthModalVisible"
    size="huge"
    preset="card"
    :style="bodyStyle"
    :bordered="true"
    @close="close"
  >
    <template #header>
      <span text="1.5rem" font-bold>{{ title }}</span>
    </template>
    <component
      :is="currentTab === 'account' ? AccountLogin : currentTab === 'phone' ? PhoneLogin : RegisterForm"
      :ref="refMap[currentTab]"
    />
  </n-modal>
</template>
