<script setup lang="ts">
import { appMeta } from '~/config';
</script>

<template>
  <div flex-center font-bold text-14px>
    <span op30>{{ appMeta.copyRight }}</span><span mx-2 op30> · </span><a href="http://beian.miit.gov.cn/" op-50 target="_blank" text-primary hover="!op-100">苏ICP备2023051139号</a>
  </div>
</template>
