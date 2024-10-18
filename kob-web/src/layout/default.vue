<script  setup lang="ts">
import { layoutConfig } from '~/config';
import TheFoot from './components/TheFooter.vue';
import TheHeader from './components/TheHeader.vue';
import TheMain from './components/TheMain.vue';

const {
  navHeight,
  contentPadding,
  footHeight,
  backTop,
} = layoutConfig;

const refWrapper = ref();
</script>

<template>
  <n-layout hw-screen of-hidden>
    <n-layout-header bordered position="absolute">
      <TheHeader w-full :style="{ height: `${navHeight}px` }" />
    </n-layout-header>
    <n-layout
      ref="refWrapper"
      position="absolute"
      :style="{
        height: 'auto',
        marginTop: `${navHeight + 1}px`,
        marginBottom: `${footHeight + 1}px`,
        minHeight: `calc(100vh - ${navHeight + 1 + footHeight + 1}px)`,
      }"
      :native-scrollbar="true"
    >
      <n-layout-content position="absolute">
        <TheMain
          :style="{
            padding: `${contentPadding}px`,
          }"
        />
      </n-layout-content>
      <n-back-top
        :listen-to="refWrapper"
        :right="backTop.right"
        :bottom="backTop.bottom"
        :visibility-height="backTop.visibilityHeight"
      />
    </n-layout>
    <n-layout-footer
      bordered position="absolute"
      :style="{ height: `${footHeight}px` }"
    >
      <TheFoot hw-full />
    </n-layout-footer>
    <ChatRoom />
  </n-layout>
</template>
