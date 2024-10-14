<script setup lang="ts">
import type { FormInst, FormRules } from 'naive-ui';
import {
  Glasses as GlassesIcon,
  GlassesOutline as GlassesOutlineIcon,
  TrashBinOutline as TrashBinOutlineIcon,
} from '@vicons/ionicons5';
import { validateUsername } from '../../utils';
import AuthFunctionBar from '../AuthFunctionBar.vue';

const loginCallback = inject<Function>('loginCallback');

interface ModelType {
  username: string
  password: string
}

const refForm = ref<FormInst | null>(null);
const refInputUserName = ref<HTMLInputElement | null>(null);

const baseFormModel: ModelType = isDevelopment
  ? { username: 'admin', password: '123456' }
  : { username: '', password: '' };

const formModel = reactive<ModelType>({ ...baseFormModel });

const rules: FormRules = {
  username: [
    { required: true, message: '请输入账号' },
    { validator: validateUsername, trigger: ['input', 'blur'] },
  ],
  password: [
    { required: true, message: '请输入密码' },
  ],
};

const { loading, startLoading, endLoading } = useLoading();

async function onSubmit(e: MouseEvent) {
  e.preventDefault();
  const errors = await refForm.value?.validate();
  if (errors)
    return;

  startLoading();
  try {
    const result = await UserApi.getToken(useClone(formModel));
    const { code, data, msg } = result.data;
    if (code !== 0) {
      $message.error(msg || '账号或密码错误');
      return;
    }
    useTimeoutFn(() => loginCallback?.(data.token), 1000);
  } catch (error) {
    console.error(error);
    $message.error('登录发生异常');
  } finally {
    endLoading();
  }
}

const focusFirstInput = () => refInputUserName.value?.focus();

defineExpose({ focusFirstInput });
</script>

<template>
  <n-form
    ref="refForm"
    :model="formModel"
    :rules="rules"
    :show-label="false"
    :show-require-mark="false"
    size="large"
  >
    <n-form-item path="username">
      <n-input
        ref="refInputUserName"
        v-model:value="formModel.username"
        placeholder="账号"
        clearable
        @keydown.enter.prevent
      >
        <template #clear-icon>
          <n-icon :component="TrashBinOutlineIcon" />
        </template>
      </n-input>
    </n-form-item>
    <n-form-item path="password">
      <n-input
        v-model:value="formModel.password"
        type="password"
        clearable
        show-password-on="click"
        placeholder="密码"
        @keydown.enter.prevent
      >
        <template #clear-icon>
          <n-icon :component="TrashBinOutlineIcon" />
        </template>
        <template #password-visible-icon>
          <n-icon :size="16" :component="GlassesOutlineIcon" />
        </template>
        <template #password-invisible-icon>
          <n-icon :size="16" :component="GlassesIcon" />
        </template>
      </n-input>
    </n-form-item>
    <n-button
      block
      type="primary"
      :loading="loading"
      class="mt-3"
      text-color="white"
      @click="onSubmit"
    >
      <span class="font-bold text-lag">登录</span>
    </n-button>
  </n-form>
  <AuthFunctionBar />
</template>
