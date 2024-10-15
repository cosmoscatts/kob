<script setup lang="ts">
import MachineBattleArena from './components/machine/MachineBattleArena.vue';
import MatchDuelArena from './components/match/MatchDuelArena.vue';

const currentPageIndex = ref(0);
const changePageIndex = (index: number) => currentPageIndex.value = index;
provide('changePageIndex', changePageIndex);

const { contentStyle } = useLayoutStyle({ additionalOffset: 5 });
</script>

<template>
  <div class="main-container theme" :class="[{ dark: isDark }]" :style="contentStyle">
    <Transition name="fade" mode="out-in" appear>
      <div v-if="currentPageIndex === 0" class="content-container">
        <div class="title">
          选择对战模式
        </div>
        <div class="options-container">
          <div class="option" @click="changePageIndex(1)">
            <div class="option-content">
              <div class="icon-wrapper match">
                <div class="icon" i-ri-sword-line />
              </div>
              <h2>匹配对战</h2>
              <p>与其他玩家一较高下，体验紧张刺激的实时对决</p>
            </div>
            <div class="option-overlay">
              <span text-hex-8B4513 dark:text-hex-FFE4B5>开始匹配</span>
            </div>
          </div>
          <div class="option" @click="changePageIndex(2)">
            <div class="option-content">
              <div class="icon-wrapper machine">
                <div class="icon" i-carbon-bot />
              </div>
              <h2>人机对战</h2>
              <p>挑战智能AI对手，提升你的策略和技巧</p>
            </div>
            <div class="option-overlay">
              <span text-hex-1B4B49 dark:text-hex-B2E6E2>开始挑战</span>
            </div>
          </div>
        </div>
      </div>
      <MatchDuelArena v-else-if="currentPageIndex === 1" />
      <MachineBattleArena v-else />
    </Transition>
  </div>
</template>

<style scoped>
.theme {
  --color-heading-darker: #000000;
  --color-heading: #1D2129;
  --color-background-soft: #FFFFFF;
  --color-background-option-overlay: linear-gradient(to top, rgba(255, 255, 255, 0), rgba(0, 0, 0, 0.1));
  --color-border: rgb(239, 239, 245);
  --color-text: #4E5969;
}

.theme.dark {
  --color-heading-darker: #FFFFFF;
  --color-heading: #F6F6F6;
  --color-background-soft: #121212;
  --color-background-option-overlay: linear-gradient(to top, rgba(255, 255, 255, 0.1), rgba(0, 0, 0, 0));
  --color-border: rgba(255, 255, 255, 0.09);
  --color-text: #C5C5C5;
}

.content-container {
  width: 100%;
  max-width: 900px;
  margin: 0 auto;
  padding: 10px 2rem;
}

.title {
  font-size: 2.5rem;
  margin-bottom: 1.5rem;
  text-align: center;
  color: var(--color-heading);
  font-weight: bold;
}

.options-container {
  display: flex;
  justify-content: center;
  gap: 3rem;
}

.option {
  width: 300px;
  height: 400px;
  border-radius: 20px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  background: var(--color-background-soft);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  position: relative;
  border: 1px solid var(--color-border);
}

.option-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 2rem;
  text-align: center;
  transition: all 0.3s ease;
}

.icon-wrapper {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 2rem;
  transition: all 0.3s ease;
}

.icon-wrapper.match {
  background: linear-gradient(135deg, #F0C987, #E6A157);
}

.dark .icon-wrapper.match {
  background: linear-gradient(135deg, #B07D62, #8C5D3F);
}

.icon-wrapper.machine {
  background: linear-gradient(135deg, #7ECECA, #4DAFAA);
}

.dark .icon-wrapper.machine {
  background: linear-gradient(135deg, #3D7A76, #265E5A);
}

.icon {
  font-size: 3rem;
  color: white;
}

h2 {
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: 1rem;
  color: var(--color-heading);
  transition: all 0.3s ease;
}

p {
  font-size: 1rem;
  color: var(--color-text);
  line-height: 1.6;
  transition: all 0.3s ease;
}

.option-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 0;
  background: var(--color-background-option-overlay);
  display: flex;
  justify-content: center;
  align-items: flex-end;
  opacity: 0;
  transition: all 0.3s ease;
  padding-bottom: 2rem;
  z-index: -1;
}

.option-overlay span {
  font-size: 1.5rem;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 2px;
  transform: translateY(20px);
  transition: all 0.3s ease;
}

.option:hover {
  transform: translateY(-10px);
}

.option:hover .icon-wrapper {
  transform: scale(1.1);
}

.option:hover .option-content {
  transform: translateY(-40px);
}

.option:hover h2 {
  color: var(--color-heading-darker);
}

.option:hover p {
  opacity: 0.75;
}

.option:hover .option-overlay {
  opacity: 1;
  height: 100%;
}

.option:hover .option-overlay span {
  transform: translateY(0);
}

@media (max-width: 768px) {
  .options-container {
    flex-direction: column;
    align-items: center;
  }

  .option {
    width: 100%;
    max-width: 300px;
    height: 350px;
    margin-bottom: 2rem;
  }
}
</style>
