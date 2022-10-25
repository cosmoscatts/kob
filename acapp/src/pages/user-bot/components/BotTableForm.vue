<script setup lang="ts">
import type { FormInst, FormValidationError } from 'naive-ui'
import {
  TrashBinOutline as TrashBinOutlineIcon,
} from '@vicons/ionicons5'
import { VAceEditor } from 'vue3-ace-editor'
import ace from 'ace-builds'
import { createRules } from '../helper'
import type { Bot } from '~/types'

const {
  type = 'add',
  modalVisible = false,
  form = {},
} = defineProps<{
  /** 表单操作类型 */
  type?: 'add' | 'edit'
  /** 表单是否显示 */
  modalVisible?: boolean
  /** 表单数据 */
  form?: Bot
}>()

const emits = defineEmits(['update:modal-visible', 'saveBotData'])

/** 代码编辑器 */
ace.config.set(
  'basePath',
  `https://cdn.jsdelivr.net/npm/ace-builds@${ace.version}/src-noconflict/`)

// 标题
const title = computed(() => type === 'add' ? '添加Bot' : '编辑Bot')

// `card` 分级
const segmented = {
  content: 'soft',
  footer: 'soft',
} as const

// `form` 表单元素
const refForm = ref<FormInst | null>(null)

type FormModel = Omit<Bot, 'rating' | 'createTime' | 'modifyTime' > & { content: string }
const baseFormModel: FormModel = {
  id: undefined,
  userId: undefined,
  title: '',
  description: '',
  content: '',
}
const formModel = reactive<FormModel>({
  ...baseFormModel,
})

const { loading, startLoading, endLoading } = useLoading()

/**
 * 表单赋值
 */
function assign() {
  const target: Bot = modalVisible && type === 'edit'
    ? unref(form)
    : baseFormModel

    type K = keyof Omit<FormModel, 'content'>
    for (const [key, value] of Object.entries(target)) {
      if (!Object.prototype.hasOwnProperty.call(formModel, key))
        continue

      if (key === 'content') {
        formModel[key] = value ?? ''
        continue
      }

      formModel[key as K] = value
    }
}

watch(() => modalVisible, () => {
  assign()
  endLoading()
  refForm.value?.restoreValidation()
})

/**
 * 提交表单
 */
function onSubmit(e: MouseEvent) {
  e.preventDefault()
  refForm.value?.validate((errors?: FormValidationError[]) => {
    if (errors)
      return
    startLoading()
    emits('saveBotData', JSON.parse(JSON.stringify(formModel)))
    useTimeoutFn(endLoading, 2000)
  })
}

/**
 * 关闭 `modal`
 */
function onCloseModal() {
  emits('update:modal-visible', false)
}

// 生成表单校验规则
const rules = createRules()
</script>

<template>
  <n-modal
    :show="modalVisible"
    :title="title" size="huge"
    :style="{ width: '500px' }"
    preset="card" :bordered="false"
    :segmented="segmented"
    :mask-closable="false"
    transform-origin="center"
    :on-esc="onCloseModal"
    :on-close="onCloseModal"
    to="#container"
  >
    <n-form
      ref="refForm"
      :model="formModel"
      :rules="rules"
      label-placement="left"
      label-width="auto"
      :show-require-mark="false"
      :style="{
        maxWidth: '450px',
        minHeight: '400px',
        overflow: 'auto',
      }"
    >
      <n-form-item label="标题" path="title">
        <n-input v-model:value="formModel.title" placeholder="请输入标题" clearable>
          <template #clear-icon>
            <n-icon :component="TrashBinOutlineIcon" />
          </template>
        </n-input>
      </n-form-item>
      <n-form-item label="描述" path="description">
        <n-input v-model:value="formModel.description" type="textarea" placeholder="请输入描述" clearable>
          <template #clear-icon>
            <n-icon :component="TrashBinOutlineIcon" />
          </template>
        </n-input>
      </n-form-item>
      <n-form-item label="代码" path="content">
        <VAceEditor
          v-model:value="formModel.content"
          lang="java"
          theme="monokai"
          :style="{
            height: '300px',
            width: '100%',
          }"
        />
      </n-form-item>
    </n-form>
    <template #footer>
      <div flex justify-center items-center gap-x-5>
        <n-button type="primary" :loading="loading" text-color="white" @click="onSubmit">
          <span font-bold>保存</span>
        </n-button>
        <n-button type="error" text-color="white" @click="onCloseModal">
          <span font-bold>取消</span>
        </n-button>
      </div>
    </template>
  </n-modal>
</template>

