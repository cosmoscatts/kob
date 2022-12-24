<script setup lang="ts">
import type {
  FormInst,
  FormItemInst,
  FormItemRule,
  FormRules,
  FormValidationError,
} from 'naive-ui'
import {
  Glasses as GlassesIcon,
  GlassesOutline as GlassesOutlineIcon,
  TrashBinOutline as TrashBinOutlineIcon,
} from '@vicons/ionicons5'
import FuncBar from './FuncBar.vue'

const registerCallback = inject<Function>('registerCallback')

interface ModelType {
  username?: string
  password?: string
  reenteredPassword?: string
}

const refForm = ref<FormInst | null>(null)
const baseFormModel = {
  username: '',
  password: '',
  reenteredPassword: '',
}
const formModel = reactive<ModelType>({
  ...baseFormModel,
})

const refRPasswordFormItem = ref<FormItemInst | null>(null)

function handlePasswordInput() {
  if (formModel.reenteredPassword) {
    refRPasswordFormItem.value?.validate({ trigger: 'password-input' })
  }
}

function validatePasswordStartWith(
  _rule: FormItemRule,
  value: string,
): boolean {
  return (
    !!formModel.password
    && formModel.password.startsWith(value)
    && formModel.password.length >= value.length
  )
}

const validatePasswordSame = (_rule: FormItemRule, value: string) => value === formModel.password

const rules: FormRules = {
  username: [
    {
      required: true,
      message: '请输入账号',
    },
    {
      validator(_rule: FormItemRule, value: string) {
        return value.length >= 1 && value.length <= 20
      },
      message: '账号的长度为 1 ~ 20',
      trigger: ['input', 'blur'],
    },
  ],
  password: [
    {
      required: true,
      message: '请输入用户密码',
    },
  ],
  reenteredPassword: [
    {
      required: true,
      message: '请再次输入密码',
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
}

const { loading, startLoading, endLoading } = useLoading()

function onSubmit(e: MouseEvent) {
  e.preventDefault()
  refForm.value?.validate((errors?: FormValidationError[]) => {
    if (errors) return
    startLoading()
    UserApi
      .register(useClone(formModel))
      .then(({ code, msg }) => {
        if (code !== 0) {
          $message.error(msg ?? '注册失败')
          return
        }
        $message.success('注册成功')
        registerCallback?.()
      })
      .finally(() => useTimeoutFn(endLoading, 1000))
  })
}

const refInputUserName = ref()
const focusFirstInput = () => refInputUserName.value?.focus()

defineExpose({
  focusFirstInput,
})
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
      path="reenteredPassword"
    >
      <n-input
        v-model:value="formModel.reenteredPassword"
        :disabled="!formModel.password"
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
      <span font-bold text-lag>注册</span>
    </n-button>
  </n-form>
  <FuncBar />
</template>
