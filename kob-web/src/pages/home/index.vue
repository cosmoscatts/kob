<script setup lang="ts">
const { contentStyle } = useLayoutStyle({ additionalOffset: 8 });

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
];

const currentEmoji = ref(getRandomEmoji());
const isAnimating = ref(false);

function getRandomEmoji(): string {
  return emojiArray[Math.floor(Math.random() * emojiArray.length)];
}

function changeEmoji() {
  if (isAnimating.value)
    return;
  isAnimating.value = true;
  setTimeout(() => {
    currentEmoji.value = getRandomEmoji();
    isAnimating.value = false;
  }, 500);
}

const animationClass = computed(() => isAnimating.value ? 'animate-bounce' : '');

onMounted(() => useLottie({
  containerId: '#lottie-car',
  path: '/lottie/home.json',
}));
</script>

<template>
  <div w-70vw mx-a flex="col center" :style="contentStyle">
    <div id="lottie-car" w400px h300px mt10px />
    <div text-center font-bold mt="[-20px]">
      <h1
        text="3xl"
        m="t-2 b-2"
        transition="all duration-500"
        :class="animationClass"
        cursor-pointer
        @click="changeEmoji"
      >
        Hi@Everyone, <span class="emoji">{{ currentEmoji }}</span>. Welcome to our Amazing Game!
      </h1>
      <p text-xl m="t-3 b-6" w-50vw mx-a leading-relaxed>
        Embark on an exciting journey of strategy and skill in this captivating two-player game.
        Challenge yourself against our advanced AI or enjoy friendly matches with your buddies.
        With intuitive gameplay and stunning visuals, this game offers endless hours of fun for players of all levels.
      </p>
    </div>
  </div>
</template>

<style scoped>
.emoji {
  @apply inline-block transition-transform duration-500;
}

.animate-bounce {
  animation: bounce 0.5s;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}
</style>
