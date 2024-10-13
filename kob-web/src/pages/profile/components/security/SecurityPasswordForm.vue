<script setup lang="ts">
import type {
  FormInst,
  FormItemInst,
  FormItemRule,
  FormRules,
  FormValidationError,
} from 'naive-ui';
import {
  Glasses as GlassesIcon,
  GlassesOutline as GlassesOutlineIcon,
  TrashBinOutline as TrashBinOutlineIcon,
} from '@vicons/ionicons5';

const securityActionCallback = inject<Function>('securityActionCallback');

interface ModelType {
  oldPass?: string
  newPass?: string
  reenteredNewPass?: string
}

const refForm = ref<FormInst | null>(null);
const baseFormModel = {
  oldPass: '',
  newPass: '',
  reenteredNewPass: '',
};
const formModel = reactive<ModelType>({
  ...baseFormModel,
});

const refRPasswordFormItem = ref<FormItemInst | null>(null);
function handlePasswordInput() {
  if (formModel.reenteredNewPass) {
    refRPasswordFormItem.value?.validate({ trigger: 'password-input' });
  }
}

function validatePasswordStartWith(
  _rule: FormItemRule,
  value: string,
): boolean {
  return (
    !!formModel.newPass
    && formModel.newPass.startsWith(value)
    && formModel.newPass.length >= value.length
  );
}

const validatePasswordSame = (_rule: FormItemRule, value: string) => value === formModel.newPass;

const rules: FormRules = {
  oldPass: [
    {
      required: true,
      message: '请输入原密码',
    },
  ],
  newPass: [
    {
      required: true,
      message: '请输入新密码',
    },
  ],
  reenteredNewPass: [
    {
      required: true,
      message: '请再次输入新密码',
      trigger: ['input', 'blur'],
    },
    {
      validator: validatePasswordStartWith,
      message: '两次密码输入不一致',
      trigger: 'input',
    },
    {
      validator: validatePasswordSame,
      message: '两次密码输入不一致',
      trigger: ['blur', 'password-input'],
    },
  ],
};

const { loading, startLoading, endLoading } = useLoading();

function onSubmit(e: MouseEvent) {
  e.preventDefault();
  refForm.value?.validate(async (errors?: FormValidationError[]) => {
    if (errors)
      return;
    startLoading();
    try {
      const result = await UserSecurityApi.updatePassword(useClone(formModel));
      const { code, msg } = result.data;
      if (code !== 0) {
        useTimeoutFn(endLoading, 1000);
        $message.error(msg || '保存失败');
        return;
      }
      $message.success('保存成功');
      useTimeoutFn(() => {
        securityActionCallback?.();
      }, 1000);
    } catch (e) {
      console.error(e);
      $message.error('保存失败');
    } finally {
      endLoading();
    }
  });
}

const refInputOldPass = ref();
const focusFirstInput = () => refInputOldPass.value?.focus();

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
    <n-form-item path="oldPass">
      <n-input
        ref="refInputOldPass"
        v-model:value="formModel.oldPass"
        type="password"
        placeholder="原密码"
        clearable
        show-password-on="click"
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
    <n-form-item path="newPass">
      <n-input
        v-model:value="formModel.newPass"
        type="password"
        clearable
        show-password-on="click"
        placeholder="密码"
        @keydown.enter.prevent
        @input="handlePasswordInput"
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
    <n-form-item
      ref="refRPasswordFormItem"
      first
      path="reenteredNewPass"
    >
      <n-input
        v-model:value="formModel.reenteredNewPass"
        :disabled="!formModel.newPass"
        clearable
        show-password-on="click"
        type="password"
        placeholder="确认密码"
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
      block type="primary"
      :loading="loading"
      mt-3 text-color="white"
      @click="onSubmit"
    >
      <span font-bold text-lag>保存</span>
    </n-button>
  </n-form>
</template>
