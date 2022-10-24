<script setup lang="ts">
import type { Ref } from 'vue'

const containerWidth = inject<Ref<number>>('containerWidth')!

const emojiArray = [
  '\\(o_o)/',
  '(o^^)o',
  '(˚Δ˚)b',
  '(^-^*)',
  '(^_^)b',
  '(╯‵□′)╯',
  '(=\'X\'=)',
  '(>_<)',
  '\\(°ˊДˋ°)/',
  'ㄟ(▔▽▔)ㄏ',
]

const getEmoji = (): string =>
  emojiArray[Math.floor(Math.random() * emojiArray.length)]

const menu = [
  ['对战', 'PK', '1', 'i-ri-sword-line'],
  ['排行', 'Rank', '2', 'i-fluent-reward-12-regular'],
  ['历史', 'Record', '3', 'i-mdi-newspaper-variant-outline'],
  ['手下', 'Bot', '4', 'i-carbon-bot'],
]

const { changePage } = usePageStore()

const menuMinHeight = computed(() => {
  const { value: w } = containerWidth
  return w < 640
    ? '240px'
    : w < 768
      ? '280px'
      : w < 1024
        ? '320px'
        : w < 1280
          ? '380px'
          : w < 1536
            ? '450px'
            : w < 2000
              ? '550px'
              : '700px'
})

const iconSize = computed(() => {
  const { value: w } = containerWidth
  return w < 640
    ? '80px'
    : w < 768
      ? '100px'
      : w < 1024
        ? '120px'
        : w < 1280
          ? '120px'
          : w < 1536
            ? '140px'
            : w < 2000
              ? '150px'
              : '200px'
})

const iconLeft = computed(() => {
  const { value: w } = containerWidth
  return w < 640
    ? '16px'
    : w < 768
      ? '20px'
      : w < 1024
        ? '20px'
        : w < 1280
          ? '34px'
          : w < 1536
            ? '42px'
            : w < 2000
              ? '60px'
              : '130px'
})
</script>

<template>
  <div
    min-h-400px
    :style="{
      width: containerWidth < 768 ? '96%' : '80%',
      marginLeft: containerWidth < 768 ? '2%' : '10%',
    }"
  >
    <p text="4xl center" font-bold>
      <span :class="{ hidden: containerWidth < 768 }">怎么，你也玩蛇吗？</span>{{ getEmoji() }}
    </p>
    <div
      grid="~ cols-4 gap-x-3"
      :class="{ 'gap-x-6': containerWidth > 1536 }"
      :style="{
        width: '68%',
        marginLeft: '16%',
        height: '100%',
        minHeight: menuMinHeight,
      }"
    >
      <div
        v-for="(item, index) in menu" :key="`menu-item-${index}`" class="menu"
        @click="changePage(parseInt(item[2]))"
      >
        <div mt-10px text="3vw right" font-bold>
          <span text-4vw>{{ item[0][0] }}</span>{{ item[0][1] }}
        </div>
        <div text="[2.5vw] right">
          {{ item[1] }}
        </div>
        <div
          :class="item[3]" class="icon"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.menu {
  position: relative;
  grid-column: span 1 / span 1;
  height: 98%;
  min-height: 200px;
  color: #CFD2DA;
  vertical-align: top;
  transition-duration: 200ms;
  border: 2px solid #535664;
  border-radius: 5px;
  overflow: hidden;
  cursor: pointer;
  padding-right: 10px;
}

.menu:hover {
  background-color: #377BB5;
  color: white;
  transition-duration: 300ms;
}

.icon {
  position: absolute;
  font-size: v-bind(iconSize);
  top: 5rem;
  left: 5rem;
  opacity: 0;
  z-index: 100;
}

.menu:hover .icon {
  opacity: 1;
  top: 50%;
  left: v-bind(iconLeft);
  transition: 0.3s;
}
</style>
