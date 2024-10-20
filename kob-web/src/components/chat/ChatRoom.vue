<script setup lang="ts">
const el = ref();
const isHovered = useElementHover(el);
</script>

<template>
  <div
    ref="el" class="group"
    fixed bottom-60px right-0 rounded-l-32px h-36px px-8px py-0 text-14px cursor-pointer
    flex items-center gap-6px select-none box-border bg-white dark:bg-hex-202020
    border-1 border-solid border="[rgb(239,239,245)] dark:[rgba(255,255,255,0.09)]"
    text-purple shadow-lg
    hover="border-0 bg-gradient-to-r from-purple-100 to-purple-300 dark:bg-gradient-to-r dark:from-purple-700 dark:to-purple-500"
  >
    <div
      i-tabler:message-chatbot size-26px
      transition-transform duration-300 ease-in-out
      text-purple-600 dark:text-purple-300
    />

    <div
      v-if="isHovered"
      font-800
      op-0 transition-opacity duration-300 ease-in-out
      group-hover="op-100"
    >
      聊聊天
    </div>
  </div>
</template>
