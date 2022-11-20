<script setup lang="ts">
import type { FormInst, FormValidationError } from 'naive-ui'
import {
  TrashBinOutline as TrashBinOutlineIcon,
} from '@vicons/ionicons5'
import { Codemirror } from 'vue-codemirror'
import { java } from '@codemirror/lang-java'
import { oneDark } from '@codemirror/theme-one-dark'
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

const extensions = [java(), oneDark]
</script>

<template>
  <n-modal
    :show="modalVisible"
    :title="title"
    size="small"
    :style="{
      width: '500px',
      height: '100%',
      overflow: 'hidden',
    }"
    preset="card" :bordered="false"
    :segmented="segmented"
    :mask-closable="false"
    transform-origin="center"
    :on-esc="onCloseModal"
    :on-close="onCloseModal"
    to="#container"
  >
    <n-scrollbar style="max-height: 100%">
      <n-form
        ref="refForm"
        :model="formModel"
        :rules="rules"
        label-placement="left"
        label-width="auto"
        :show-require-mark="false"
        :style="{
          maxWidth: '450px',
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
          <n-scrollbar style="max-height: 260px">
            <Codemirror
              v-model="formModel.content"
              :style="{ height: '260px', width: '100%', overflow: 'hidden' }"
              :autofocus="false"
              :indent-with-tab="true"
              :tab-size="2"
              :extensions="extensions"
            />
          </n-scrollbar>
        </n-form-item>
      </n-form>
    </n-scrollbar>

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

<style>
.ͼ1 .cm-scroller {
  font-family: 'Consolas';
}

.ͼo {
  background-color: #393D46;
}

.ͼo .cm-gutters {
  background-color: #393D46;
}
</style>
