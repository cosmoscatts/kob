<script setup lang="ts">
import { ref } from 'vue';

const isHovered = ref(false);
</script>

<template>
  <div
    class="fixed right-6 bottom-6 h-12 w-12 rounded-full bg-gradient-to-r from-violet-500 to-fuchsia-500
           shadow-lg cursor-pointer flex items-center justify-center overflow-hidden
           transition-all duration-300 ease-in-out z-50 hover:w-28 group"
    @mouseenter="isHovered = true"
    @mouseleave="isHovered = false"
  >
    <div class="relative w-full h-full flex items-center justify-center">
      <!-- 主图标 -->
      <i
        class="i-carbon-chat text-xl text-white transition-all duration-300 ease-in-out
                group-hover:scale-90 group-hover:-translate-x-2"
      />

      <!-- 悬浮时显示的文字 -->
      <span
        class="absolute left-10 text-sm text-white font-medium opacity-0 transition-all duration-300 ease-in-out
               transform translate-x-2 group-hover:opacity-100 group-hover:translate-x-0"
      >
        聊天
      </span>

      <!-- 装饰性元素 -->
      <div
        class="absolute inset-0 border border-white/20 rounded-full opacity-0 scale-90
                  group-hover:opacity-100 group-hover:scale-100 transition-all duration-300 ease-out"
      />
      <div
        class="absolute -inset-0.5 bg-white rounded-full blur opacity-0 group-hover:opacity-20
                  transition-opacity duration-300 ease-in-out"
      />

      <!-- 波纹效果 -->
      <div
        class="absolute inset-0 rounded-full bg-white/30 opacity-0 scale-50
                  group-hover:animate-ripple"
      />
    </div>
  </div>
</template>

<style scoped>
@keyframes ripple {
  0% {
    transform: scale(0.5);
    opacity: 0;
  }
  50% {
    opacity: 0.2;
  }
  100% {
    transform: scale(1.5);
    opacity: 0;
  }
}

.group-hover\:animate-ripple {
  animation: ripple 1.5s cubic-bezier(0, 0, 0.2, 1) infinite;
}
</style>
