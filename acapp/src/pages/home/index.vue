<script setup lang="ts">
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
</script>

<template>
  <div
    h400px
    :style="{
      width: '96%',
      marginLeft: '2%',
    }"
  >
    <p text="24px center" font-bold my-10px>
      <span>怎么，你也玩蛇吗？</span>{{ getEmoji() }}
    </p>
    <div grid="~ cols-4 gap-x-5" :style="{ width: '68%', marginLeft: '14%' }">
      <div
        v-for="(item, index) in menu" :key="`menu-item-${index}`" class="menu"
        @click="changePage(parseInt(item[2]))"
      >
        <div mt10px text="24px right" font-bold>
          <span text-32px>{{ item[0][0] }}</span>{{ item[0][1] }}
        </div>
        <div text="16px right">
          {{ item[1] }}
        </div>
        <div :class="item[3]" class="icon" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.menu {
  position: relative;
  grid-column: span 1 / span 1;
  height: 300px;
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
  top: 50px;
  left: 50px;
  opacity: 0;
  font-size: 100px;
  z-index: 100;
}

.menu:hover .icon {
  opacity: 1;
  top: 130px;
  left: 16px;
  transition: 0.3s;
}
</style>
