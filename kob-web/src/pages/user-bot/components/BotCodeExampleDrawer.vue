<script setup lang="ts">
import { codeExample } from '../utils/code';

const visible = defineModel('visible', { default: false });

const { width } = useWindowSize();
const drawerWidth = computed(() => width.value > 600 ? 600 : 502);

const { copy, isSupported } = useClipboard({ source: codeExample });

function handleCopy() {
  if (!isSupported.value) {
    $message.warning('当前浏览器不支持该操作');
    return;
  }
  copy();
  $message.success('复制成功，快去编写Bot吧~');
}

function closeDrawer() {
  visible.value = false;
}
</script>

<template>
  <n-drawer
    :width="drawerWidth"
    :show="visible"
    placement="right"
    @mask-click="closeDrawer"
    @on-esc="closeDrawer"
    @update:show="closeDrawer"
  >
    <n-drawer-content title="如何快速编写bot代码" closable>
      <n-alert title="关于Bot" type="info">
        你需要实现BotInterface接口的nextMove方法，蛇的每一次移动都会调用nextMove方法，来获得下一步的方向。
        nextMove方法的返回值为0、1、2、3，分别代表上、右、下、左。
      </n-alert>
      <n-card title="需要实现的接口" class="mt-5">
        <n-code
          code="package com.kob.botrunningsystem.utils;

public interface BotInterface {
    Integer nextMove(String input);
}"
          language="java"
        />
      </n-card>
      <n-card title="示例代码" class="mt-5">
        <template #header-extra>
          <n-button type="primary" text-color="white" @click="handleCopy">
            一键复制
          </n-button>
        </template>
        <n-code
          word-wrap
          :code="codeExample"
          language="java"
        />
      </n-card>
    </n-drawer-content>
  </n-drawer>
</template>
