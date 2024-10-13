<script setup lang="ts">
import type {
  FormInst,
  FormItemRule,
  FormRules,
  FormValidationError,
} from 'naive-ui';
import {
  TrashBinOutline as TrashBinOutlineIcon,
} from '@vicons/ionicons5';
import FuncBar from './FuncBar.vue';
import { countSendingSmsCode, getSmsCode, REGEXP_PHONE } from './helper';

interface ModelType {
  phone?: string
  code?: string
}
const refForm = ref<FormInst | null>(null);
const baseFormModel = isDevelopment
  ? {
      phone: '13650223322',
      code: '123456',
    }
  : {
      phone: '',
      code: '',
    };
const formModel = reactive<ModelType>({
  ...baseFormModel,
});

const validatePhone = (value: string) => REGEXP_PHONE.test(value);
const rules: FormRules = {
  phone: [
    {
      required: true,
      message: '请输入手机号',
    },
    {
      validator(_rule: FormItemRule, value: string) {
        return validatePhone(value);
      },
      message: '请输入正确的手机号',
      trigger: ['input'],
    },
  ],
  code: [
    {
      required: true,
      message: '请输入验证码',
    },
  ],
};

const { loading } = useLoading();

function onSubmit(e: MouseEvent) {
  e.preventDefault();
  refForm.value?.validate((errors?: FormValidationError[]) => {
    if (errors)
      return;
    if (formModel.code !== '123456') {
      $message.error('验证码错误');
      return;
    }
    $message.warning('此功能暂不支持嘻嘻 (┬┬﹏┬┬)');
  });
}

const refInputPhone = ref();
const focusFirstInput = () => refInputPhone.value?.focus();

// 禁用验证码及发送按钮
const codeInputDisabled = computed(() => (!formModel.phone || !validatePhone(formModel.phone)));

const {
  loading: smsLoading,
  startLoading: startSmsLoading,
  endLoading: endSmsLoading,
} = useLoading(false);

const {
  isCounting,
  sendCodeBtnLabel,
  startCounting,
} = countSendingSmsCode();

function handleSmsCode() {
  startSmsLoading();
  useTimeoutFn(() => {
    $message.success('验证码发送成功')
    ;[endSmsLoading, startCounting, getSmsCode]
      .forEach(fn => fn());
  }, 1000);
}

defineExpose({
  focusFirstInput,
});
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
    <n-form-item path="phone">
      <n-input
        ref="refInputPhone"
        v-model:value="formModel.phone"
        clearable
        placeholder="手机号"
        @keydown.enter.prevent
      >
        <template #clear-icon>
          <n-icon :component="TrashBinOutlineIcon" />
        </template>
      </n-input>
    </n-form-item>
    <n-form-item path="code">
      <div class="flex-y-center w-full">
        <n-input
          v-model:value="formModel.code"
          :disabled="codeInputDisabled"
          placeholder="验证码"
          clearable
        >
          <template #clear-icon>
            <n-icon :component="TrashBinOutlineIcon" />
          </template>
        </n-input>
        <div class="w-18px" />
        <n-button
          size="large" type="primary" text-color="white"
          :disabled="codeInputDisabled || isCounting"
          :loading="!codeInputDisabled && smsLoading"
          @click="handleSmsCode"
        >
          {{ sendCodeBtnLabel }}
        </n-button>
      </div>
    </n-form-item>
    <n-button
      block type="primary" :loading="loading"
      mt-3 text-color="white" @click="onSubmit"
    >
      <span font-bold text-lag>登录</span>
    </n-button>
  </n-form>
  <FuncBar />
</template>
