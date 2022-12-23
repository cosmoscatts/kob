<script setup lang="ts">
import { APP_LAYOUT_PARAMS } from '~/config'

const { navHeight, footHeight, contentPadding } = APP_LAYOUT_PARAMS
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

onMounted(() => useLottie({
  container: document.querySelector('#lottie-cat')!,
  path: 'https://assets1.lottiefiles.com/packages/lf20_cSNnXm5euH.json',
}))
</script>

<template>
  <div
    w-70vw mx-a flex="col center"
    :style="{ height: `calc(100vh - ${diffHeight}px)` }"
  >
    <div id="lottie-cat" w400px h200px />
    <div text-center font-bold>
      <h1 text="2xl" m="t-2 b-2">
        Hi@Everyone, {{ getEmoji() }}. this is an astonishing game.
      </h1>
      <p text-xl m="t-3 b-2" w-40vw mx-a>
        This is a two-player game that you can play yourself or use the AI.
        If you haven't played it yet, give it a try!
      </p>
    </div>
  </div>
</template>
