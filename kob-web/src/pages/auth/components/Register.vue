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

/**
 * 定义表单数据结构
 */
interface ModelType {
  username?: string
  password?: string
  reenteredPassword?: string
}

const refForm = ref<FormInst | null>(null)

const { message } = useGlobalNaiveApi()

// 表单基础数据
const baseFormModel = {
  username: '',
  password: '',
  reenteredPassword: '',
}
// 表单数据
const formModel = reactive<ModelType>({
  ...baseFormModel,
})

// 重复密码框元素
const refRPasswordFormItem = ref<FormItemInst | null>(null)

/**
 * 处理密码框的输入，当输入密码时，触发重复密码框的校验
 */
function handlePasswordInput() {
  if (formModel.reenteredPassword)
    refRPasswordFormItem.value?.validate({ trigger: 'password-input' })
}

/**
 * 校验重复密码是否以输入的密码为开头
 */
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

/**
 * 校验两次输入的密码是否一致
 */
function validatePasswordSame(_rule: FormItemRule, value: string): boolean {
  return value === formModel.password
}

// 表单校验规则
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

/**
 * 注册
 */
async function onSubmit(e: MouseEvent) {
  e.preventDefault()
  refForm.value?.validate(async (errors?: FormValidationError[]) => {
    if (errors)
      return
    startLoading()
    const { code, msg } = await UserApi.register(JSON.parse(JSON.stringify(formModel)))
    if (code !== 0) {
      message.error(msg ?? '注册失败')
      return
    }

    useTimeoutFn(() => {
      endLoading()
      registerCallback?.()
    }, 1000)
  })
}

// 实现聚焦功能
const refInputUserName = ref()
function focusFirstInput() {
  refInputUserName.value?.focus()
}

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

