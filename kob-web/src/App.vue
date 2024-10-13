<script setup lang="ts">
import hljs from 'highlight.js/lib/core';
import java from 'highlight.js/lib/languages/java';
import { darkTheme, dateZhCN, zhCN } from 'naive-ui';
import { checkVersion } from 'version-rocket';
import { version } from '../package.json';

hljs.registerLanguage('java', java);

// 主题和自定义主题覆盖
const theme = computed(() => [null, darkTheme][Number(isDark.value)]);
const themeOverrides = useThemeOverrides();

updateThemeColors();

// 关闭浏览器清空缓存
const beginTime = ref(0);
window.addEventListener('beforeunload', () => beginTime.value = Date.now());
window.addEventListener('unload', () => {
  if (Date.now() - beginTime.value <= 5)
    localStorage.clear();
});

onBeforeMount(() => {
  if (isProduction) {
    checkVersion(
      {
        // 5分钟检测一次版本
        pollingTime: 300000,
        localPackageVersion: version,
        originVersionFileUrl: `${location.origin}version.json`,
      },
      {
        title: '版本更新',
        description: '检测到新版本',
        buttonText: '立即更新',
      },
    );
  }
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
