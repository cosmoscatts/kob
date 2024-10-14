<script setup lang="ts">
const emits = defineEmits(['refresh']);

const words = ref('');
const { loading, startLoading, endLoading } = useLoading();

const validateInput = (remark: string) => {
  if (!remark.length) {
    $message.warning('您不能什么也不说哦');
    return false;
  }
  if (remark.length > 1000) {
    $message.warning('字数太多了哦');
    return false;
  }
  return true;
};

const submit = useThrottleFn(async () => {
  const remark = words.value.trim();
  if (!validateInput(remark))
    return;

  startLoading();
  try {
    const result = await DiscussApi.addDiscuss({ remark });
    const { code, msg } = result.data;
    if (code !== 0) {
      $message.error(msg || '提交失败');
      return;
    }
    $message.success('提交成功');
    words.value = '';
    emits('refresh');
  } catch (e) {
    console.error(e);
    $message.error('提交失败');
  } finally {
    useTimeoutFn(endLoading, 200);
  }
}, 500);
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
