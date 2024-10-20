<script setup lang="ts">
const el = ref();
const isHovered = useElementHover(el);
</script>

<template>
  <div
    ref="el" class="group"
    fixed bottom-60px right-0 rounded-l-32px h-36px px-8px py-0 cursor-pointer
    flex items-center gap-6px select-none box-border bg-white dark:bg-hex-202020
    border-1 border-solid border="#ffffff0a"
    text-hex-663399 shadow-lg dark:text-hex-9880f8 transition-all duration-200 ease-in-out
    hover="bg-#dfd5fe dark:bg-#3f3663"
  >
    <div i-tabler:brand-hipchat size-26px />

    <div
      v-if="isHovered"
      font-1000 text-16px
    >
      畅所欲言！
    </div>
  </div>
</template>
