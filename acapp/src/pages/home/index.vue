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
  <div w70vw hfull mxa pt-30px lt-sm:w96vw>
    <p text="5xl center" font-bold>
      {{ getEmoji() }}
    </p>
    <div w60vw mxa h40vh md:h45vh lg:h50vh xl:h55vh grid="~ cols-4 gap1">
      <div
        v-for="(item, index) in menu"
        :key="`menu-item-${index}`" class="group"
        relative col-span-1 w12vw hfull min-h200px
        text="[#CFD2DA]" align-top duration-200
        pr-1vw border="2 solid [#535664]" rounded-5px
        of-hidden cursor-pointer
        hover="bg-[#377BB5] text-white duration-300"
        @click="changePage(parseInt(item[2]))"
      >
        <div mt-10px text="3vw right" font-bold>
          <span text-4vw>{{ item[0][0] }}</span>{{ item[0][1] }}
        </div>
        <div text="[2.5vw] right">
          {{ item[1] }}
        </div>
        <div
          :class="item[3]"
          absolute text-15vh top-8vh left-9vw op0 z-100
          group-hover="!op100 top-16vh left-2vw duration-300 md:top-20vh md:left-3vw lg:top-23vh lg:left-5vw lg:w25vh xl:top-25vh xl:left-6vw"
        />
      </div>
    </div>
  </div>
</template>

