<script setup lang="ts">
import type { FormInst, FormValidationError } from 'naive-ui'
import {
  TrashBinOutline as TrashBinOutlineIcon,
} from '@vicons/ionicons5'
import { Codemirror } from 'vue-codemirror'
import { java } from '@codemirror/lang-java'
import { oneDark } from '@codemirror/theme-one-dark'
import { createRules } from '../rules'
import type { Bot } from '~/types'

const {
  type = 'add',
  modalVisible = false,
  form = {},
} = defineProps<{
  type?: 'add' | 'edit' // 表单操作类型
  modalVisible?: boolean // 表单是否显示
  form?: Bot // 表单数据
}>()

const emits = defineEmits(['update:modal-visible', 'saveBotData'])

const title = computed(() => type === 'add' ? '添加Bot' : '编辑Bot') // 标题

const segmented = { // card 分级
  content: 'soft',
  footer: 'soft',
} as const

const refForm = ref<FormInst | null>(null) // form 表单元素

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

function onSubmit(e: MouseEvent) {
  e.preventDefault()
  refForm.value?.validate((errors?: FormValidationError[]) => {
    if (errors) return
    startLoading()
    emits('saveBotData', useClone(formModel))
    useTimeoutFn(endLoading, 2000)
  })
}

const onCloseModal = () => emits('update:modal-visible', false)

const rules = createRules() // 生成表单校验规则
const extensions = computed(() => isDark.value ? [java(), oneDark] : [java()])
</script>

<template>
  <n-modal
    :show="modalVisible"
    :title="title"
    size="huge"
    preset="card"
    :bordered="false"
    :segmented="segmented"
    :mask-closable="false"
    transform-origin="center"
    style="width: 650px;"
    :on-esc="onCloseModal"
    :on-close="onCloseModal"
  >
    <n-form
      ref="refForm"
      :model="formModel"
      :rules="rules"
      label-placement="left"
      label-width="auto"
      :show-require-mark="false"
      :style="{
        maxWidth: '600px',
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
        <Codemirror
          v-model="formModel.content"
          :style="{ height: '300px', width: '100%' }"
          :autofocus="false"
          :indent-with-tab="true"
          :tab-size="2"
          :extensions="extensions"
        />
      </n-form-item>
    </n-form>
    <template #footer>
      <div flex-center gap-x-5>
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
  background-color: #3C3C3C;
}

.ͼo .cm-gutters {
  background-color: #3C3C3C;
}

.ͼo .cm-activeLineGutter {
  background-color: #303033;
}
</style>
