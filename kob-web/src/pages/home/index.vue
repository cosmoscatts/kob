<script setup lang="ts">
const contentHeight = diffHeight

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
  containerId: '#lottie',
  path: 'https://assets7.lottiefiles.com/packages/lf20_znxtcbvh33.json',
}))
</script>

<template>
  <div
    w-70vw mx-a flex="col center"
    :style="{ height: `calc(100vh - ${contentHeight}px)` }"
  >
    <div id="lottie" w400px h300px mt10px />
    <div text-center font-bold mt="[-20px]">
      <h1 text="3xl" m="t-2 b-2">
        Hi@Everyone, {{ getEmoji() }}. this is an astonishing game.
      </h1>
      <p text-2xl m="t-3 b-2" w-40vw mx-a>
        This is a two-player game that you can play yourself or use the AI.
        If you haven't played it yet, give it a try!
      </p>
    </div>
  </div>
</template>
