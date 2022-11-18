<script setup lang="ts">
import { codeExample } from '../code'

const {
  visible = false,
} = defineProps<{
  visible?: boolean
}>()

defineEmits(['update:visible'])

const { width: _width } = useWindowSize()
const width = computed(() => _width.value > 600 ? 600 : 502)

const source = ref(codeExample)
const { copy, isSupported } = useClipboard({ source })

const { message } = useGlobalNaiveApi()
function handleCopy() {
  if (!isSupported.value) {
    message.warning('当前浏览器不支持该操作')
    return
  }
  copy()
  message.success('复制成功，快去编写Bot吧~')
}
</script>

<template>
  <n-drawer
    :width="width"
    :show="visible"
    placement="right"
    to="#container"
    @mask-click="$emit('update:visible', false)"
    @on-esc="$emit('update:visible', false)"
    @update:show="$emit('update:visible', false)"
  >
    <n-drawer-content title="如何快速编写bot代码" closable :native-scrollbar="false">
      <n-alert title="关于Bot" type="info">
        你需要实现BotInterface接口的nextMove方法，蛇的每一次移动都会调用nextMove方法，来获得下一步的方向。
        nextMove方法的返回值为0、1、2、3，分别代表上、右、下、左。
      </n-alert>
      <n-card title="需要实现的接口" mt20px>
        <n-card>
          <n-code
            code="
package com.kob.botrunningsystem.utils;

public interface BotInterface {
    Integer nextMove(String input);
}
          "
            language="java"
          />
        </n-card>
      </n-card>
      <n-card title="示例代码" mt20px>
        <template #header-extra>
          <n-button type="primary" text-color="white" @click="handleCopy">
            一键复制
          </n-button>
        </template>
        <n-card>
          <n-code
            word-wrap
            :code="codeExample"
            language="java"
          />
        </n-card>
      </n-card>
    </n-drawer-content>
  </n-drawer>
</template>
