<script setup lang="ts">
import { appLayout } from '~/config'

const { navHeight, footHeight, contentPadding } = appLayout

const diffHeight = computed(() => {
  return navHeight + footHeight + contentPadding * 2 + 1 + 1 + 3
})

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
</script>

<template>
  <div w-70vw mx-a :style="{ height: `calc(100vh - ${diffHeight}px)` }" flex-center>
    <div text-center font-bold>
      <p text="5xl">
        {{ getEmoji() }}
      </p>
      <h1 text="2xl" m="t-5 b-2">
        Hey, this is an astonishing game.
      </h1>
      <p text-xl m="t-3 b-2" w-40vw mx-a>
        This is a two-player game that you can play yourself or use the AI.
        If you haven't played it yet, give it a try!
      </p>
    </div>
  </div>
</template>
