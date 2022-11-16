<script setup lang="ts">
import { Machine, Match } from './components'

let currentPageIndex = $ref(0)
function changePageIndex(index: number) {
  currentPageIndex = index
}
provide('changePageIndex', changePageIndex)
</script>

<template>
  <div hfull wfull>
    <Transition name="fade-slide" mode="out-in" appear>
      <div
        v-if="currentPageIndex === 0"
        flex items-center justify-between rounded-10px
        :style="{
          border: '1px solid #535664',
          width: '80%',
          height: '370px',
          marginLeft: '10%',
          padding: '0 5%',
        }"
      >
        <div class="menu" :style="{ marginRight: '10%' }" @click="changePageIndex(1)">
          <span text-60px>匹</span>配
          <div i-ri-sword-line class="icon" />
        </div>
        <div class="menu" @click="changePageIndex(2)">
          <span text-60px>人</span>机
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
  height: 60%;
  width: 40%;
  font-size: 48px;
  font-weight: 800;
  vertical-align: top;
  transition-duration: 200ms;
  border: 2px solid #535664;
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
  top: -50px;
  left: 10px;
  opacity: 0;
  font-size: 150px;
  z-index: 2;
}

.menu:hover .icon {
  opacity: 0.5;
  top: 25px;
  left: 50px;
  transition: 0.3s;
}
</style>
