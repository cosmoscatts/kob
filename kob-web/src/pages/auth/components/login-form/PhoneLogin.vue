<script setup lang="ts">
import type { FormInst, FormRules } from 'naive-ui';
import { TrashBinOutline as TrashBinOutlineIcon } from '@vicons/ionicons5';
import { computed, reactive, ref } from 'vue';
import { getSmsCode, REGEXP_PHONE, useCountdownTimer, validatePhoneNumber } from '../../utils';
import AuthFunctionBar from '../AuthFunctionBar.vue';

interface ModelType {
  phone: string
  code: string
}

const refForm = ref<FormInst | null>(null);
const refInputPhone = ref<HTMLInputElement | null>(null);

const baseFormModel: ModelType = isDevelopment
  ? { phone: '13650223322', code: '123456' }
  : { phone: '', code: '' };

const formModel = reactive<ModelType>({ ...baseFormModel });

const rules: FormRules = {
  phone: [
    { required: true, message: '请输入手机号' },
    { validator: validatePhoneNumber, message: '请输入正确的手机号', trigger: ['input'] },
  ],
  code: [
    { required: true, message: '请输入验证码' },
  ],
};

const { loading } = useLoading();

function onSubmit(e: MouseEvent) {
  e.preventDefault();
  refForm.value?.validate((errors) => {
    if (errors)
      return;
    if (formModel.code !== '123456') {
      $message.error('验证码错误');
      return;
    }
    $message.warning('此功能暂不支持嘻嘻 (┬┬﹏┬┬)');
  });
}

const focusFirstInput = () => refInputPhone.value?.focus();

const codeInputDisabled = computed(() => !formModel.phone || !REGEXP_PHONE.test(formModel.phone));

const { loading: smsLoading, startLoading: startSmsLoading, endLoading: endSmsLoading } = useLoading(false);

const { isCounting, sendCodeBtnLabel, startCounting } = useCountdownTimer();

function handleSmsCode() {
  startSmsLoading();
  setTimeout(() => {
    $message.success('验证码发送成功');
    endSmsLoading();
    startCounting();
    getSmsCode();
  }, 1000);
}

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
          size="large"
          type="primary"
          text-color="white"
          :disabled="codeInputDisabled || isCounting"
          :loading="!codeInputDisabled && smsLoading"
          @click="handleSmsCode"
        >
          {{ sendCodeBtnLabel }}
        </n-button>
      </div>
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
