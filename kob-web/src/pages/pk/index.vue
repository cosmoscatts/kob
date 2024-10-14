<script setup lang="ts">
import MachineBattleArena from './components/machine/MachineBattleArena.vue';
import MatchDuelArena from './components/match/MatchDuelArena.vue';

const { layoutOffsetHeight } = useLayoutStyle();
const { contentStyle } = useLayoutStyle({ heightProperty: 'minHeight', additionalOffset: 8 });

const borderColor = computed(() => isDark.value ? 'rgba(255, 255, 255, 0.09)' : 'rgb(239, 239, 245)');
const menuHeight = computed(() => `calc(100vh - ${layoutOffsetHeight.value + 60}px - 24vh)`);

const currentPageIndex = ref(0);
const changePageIndex = (index: number) => currentPageIndex.value = index;
provide('changePageIndex', changePageIndex);
</script>

<template>
  <div>
    <Transition name="fade-slide" mode="out-in" appear>
      <div
        v-if="currentPageIndex === 0"
        flex="col" w60vw mxa rounded-10px
        :style="{
          ...contentStyle,
          border: `1px solid ${borderColor}`,
          padding: '0 8vw',
        }"
      >
        <n-card mt7vh>
          <div
            flex justify-center items-center h60px
            text="2rem center" lt-md="text-[1.6rem]" font-bold
          >
            <div i-emojione-v1-snake />
            ：是兄弟就来玩！
          </div>
        </n-card>

        <div flex-y-center justify-between my5vh>
          <div class="menu" role="button" aria-label="匹配对战" @click="changePageIndex(1)">
            <span text-10vw>匹</span>配
            <div i-ri-sword-line class="icon" />
          </div>
          <div class="menu" role="button" aria-label="人机对战" @click="changePageIndex(2)">
            <span text-10vw>人</span>机
            <div i-carbon-bot class="icon" />
          </div>
        </div>
      </div>
      <MatchDuelArena v-else-if="currentPageIndex === 1" />
      <MachineBattleArena v-else />
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
  height: v-bind(menuHeight);
  min-height: 200px;
  width: 25vw;
  font-size: 6vw;
  font-weight: 800;
  vertical-align: top;
  transition-duration: 200ms;
  border: 1px solid v-bind(borderColor);
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
  transition: 0.3s;
}

.menu:hover .icon {
  opacity: 0.5;
  top: 2vh;
  left: 10vw;
}
</style>
