<script setup lang="ts">
import hljs from 'highlight.js/lib/core';
import java from 'highlight.js/lib/languages/java';
import { darkTheme, dateZhCN, zhCN } from 'naive-ui';

hljs.registerLanguage('java', java);

// 主题和自定义主题覆盖
const theme = computed(() => [null, darkTheme][Number(isDark.value)]);
const themeOverrides = useThemeOverrides();

writeThemeColorsToBody(); // 将 naive-ui 自带颜色写入 body

// 关闭浏览器清空缓存
const beginTime = ref(0);
window.addEventListener('beforeunload', () => beginTime.value = Date.now());
window.addEventListener('unload', () => {
  if (Date.now() - beginTime.value <= 5)
    localStorage.clear();
});
</script>

<template>
  <n-config-provider
    :theme="theme"
    :theme-overrides="themeOverrides"
    :locale="zhCN"
    :date-locale="dateZhCN"
    :hljs="hljs"
  >
    <n-loading-bar-provider>
      <RouterView />
    </n-loading-bar-provider>
  </n-config-provider>
</template>
