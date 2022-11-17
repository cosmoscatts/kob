<script setup lang="ts">
const emits = defineEmits(['refresh'])

const words = ref('')
const { message } = useGlobalNaiveApi()
const { loading, startLoading, endLoading } = useLoading()

const submit = useThrottleFn(async () => {
  const value = words.value.trim()
  if (!value?.length) {
    message.warning('您不能什么也不说哦')
    return
  }
  if (value.length > 1000) {
    message.warning('字数太多了哦')
    return
  }
  startLoading()
  try {
    const { code, msg } = await DiscussApi.addDiscuss({ remark: value })
    if (code !== 0) {
      message.error(msg || '提交失败')
      return
    }
    message.success('提交成功')
    words.value = ''
    emits('refresh')
  }
  finally {
    useTimeoutFn(endLoading, 200)
  }
}, 500)
</script>

<template>
  <div wfull flex="~ col">
    <div text="1.6rem center" font-800 mb10px>
      有什么想说的吗？
    </div>
    <n-input
      v-model:value="words"
      type="textarea"
      clearable
    />
    <div flex-y-center justify-end mt10px>
      <n-button type="primary" :loading="loading" text-color="white" @click="submit">
        提交
      </n-button>
    </div>
  </div>
</template>
