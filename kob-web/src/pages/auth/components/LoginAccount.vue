<script setup lang="ts">
import type {
  FormInst,
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

const loginCallback = inject<Function>('loginCallback')

/**
 * 定义表单数据结构
 */
interface ModelType {
  username?: string
  password?: string
}

const { message } = useGlobalNaiveApi()

const refForm = ref<FormInst | null>(null)

// 表单基础数据
const baseFormModel = isDevelopment
  ? {
      username: 'admin',
      password: '123456',
    }
  : {
      username: '',
      password: '',
    }

// 表单数据
const formModel = reactive<ModelType>({
  ...baseFormModel,
})

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
      message: '请输入密码',
    },
  ],
}

const { loading, startLoading, endLoading } = useLoading()

/**
 * 登录
 */
async function onSubmit(e: MouseEvent) {
  e.preventDefault()
  refForm.value?.validate(async (errors?: FormValidationError[]) => {
    if (errors)
      return
    startLoading()
    const { code, data, msg } = await UserApi.getToken(JSON.parse(JSON.stringify(formModel)))
    if (code !== 0) {
      useTimeoutFn(endLoading, 1000)
      message.error(msg ?? '账号或密码错误')
      return
    }

    useTimeoutFn(() => {
      endLoading()
      loginCallback?.(data.token)
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
      <n-input ref="refInputUserName" v-model:value="formModel.username" placeholder="账号" clearable @keydown.enter.prevent>
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
      block type="primary" :loading="loading"
      mt-3 text-color="white" @click="onSubmit"
    >
      <span font-bold text-lag>登录</span>
    </n-button>
  </n-form>
  <FuncBar />
</template>
