<script setup lang="ts">
import { Machine, Match } from './components'
import { appLayout } from '~/config'

const { navHeight, footHeight, contentPadding } = appLayout
const diffHeight = computed(() => navHeight + footHeight + contentPadding * 2 + 1 + 1 + 6)

const borderColor = computed(() => isDark.value ? 'rgba(255, 255, 255, 0.09)' : 'rgb(239, 239, 245)')

let currentPageIndex = $ref(0)
function changePageIndex(index: number) {
  currentPageIndex = index
}
provide('changePageIndex', changePageIndex)
</script>

<template>
  <div>
    <Transition name="fade-slide" mode="out-in" appear>
      <div
        v-if="currentPageIndex === 0"
        flex-y-center justify-between w60vw mxa rounded-10px
        :style="{
          minHeight: `calc(100vh - ${diffHeight}px)`,
          border: `1px solid ${borderColor}`,
          padding: '0 8vw',
        }"
      >
        <div class="menu" @click="changePageIndex(1)">
          <span text-10vw>匹</span>配
          <div i-ri-sword-line class="icon" />
        </div>
        <div class="menu" @click="changePageIndex(2)">
          <span text-10vw>人</span>机
          <div i-carbon-bot class="icon" />
        </div>
      </div>
      <Match v-else-if="currentPageIndex === 1" />
      <Machine v-else />
    </Transition>
  </div>
</template>

<style scoped>
.menu {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  grid-column: span 1 / span 1;
  height: 40vh;
  width: 25vw;
  font-size: 6vw;
  font-weight: 800;
  vertical-align: top;
  transition-duration: 200ms;
  border: 2px solid v-bind(borderColor);
  border-radius: 5px;
  overflow: hidden;
  cursor: pointer;
}

.menu:hover {
  background-color: #507C59;
  color: white;
  transition-duration: 300ms;
}

.icon {
  position: absolute;
  color: white;
  top: 50px;
  left: 50px;
  opacity: 0;
  font-size: 20vw;
  z-index: 2;
}

.menu:hover .icon {
  opacity: 0.5;
  top: 2vh;
  left: 10vw;
  transition: 0.3s;
}
</style>
