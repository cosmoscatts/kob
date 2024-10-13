<script setup lang="ts">
import defaultAvatar from '~/assets/default-avatar.png';

const contentHeight = diffHeight;

const pkStore = usePkStore();
const userStore = useUserStore();

pkStore.updateOpponent(); // 更新对手信息

let showFightAnimation = $ref(false);
const socket = useSocket((msg) => {
  const data = JSON.parse(msg.data);
  const fns: [boolean, Function][] = [
    [data.event === 'match-success', () => { // 匹配成功
      pkStore.updateOpponent({
        name: data?.opponentName || '-',
        avatar: data?.opponentAvatar ?? defaultAvatar,
      });
      pkStore.updateGame(data.game);
      pkStore.updateStatus('play');
      $message.success('您的对手已找到');
      showFightAnimation = true;
      useTimeoutFn(() => showFightAnimation = false, 5000);
    }],
    [data.event === 'move', () => pkStore.gameMapObject!.snakes.forEach((snake, index) =>
      snake.setDirection([data.aDirection, data.bDirection][index]))],
    [data.event === 'result', () => {
      const [snake0, snake1] = pkStore.gameMapObject!.snakes;
      if (['all', 'A'].includes(data.loser))
        snake0.status = 'die';
      if (['all', 'B'].includes(data.loser))
        snake1.status = 'die';
      pkStore.updateLoser(data.loser);
    }],
  ];
  Conditional.some(fns);
});

const clear = () => [
  () => socket.close(),
  () => pkStore.reset(),
].forEach(fn => fn());
onUnmounted(clear);

const showConfetti = computed(() => ((pkStore.loser === 'A' && pkStore.players[1].id === userStore.user?.id)
  || (pkStore.loser === 'B' && pkStore.players[0].id === userStore.user?.id)));
</script>

<template>
  <div :style="{ minHeight: `calc(100vh - ${contentHeight}px)` }" flex="col center">
    <GameMatchGround v-if="pkStore.status === 'match'" />
    <GamePlayground v-if="(pkStore.status === 'play' && !showFightAnimation)" />
    <FightAnimation v-if="(pkStore.status === 'play' && showFightAnimation)" />
    <ResultBoard v-if="pkStore.loser !== 'none'" />
    <Confetti :passed="showConfetti" />

    <div v-if="pkStore.status === 'play'" mt-15px h-5vh>
      <div v-if="userStore.user!.id === pkStore.players[0]?.id" text-24px font-bold flex-center>
        <div i-akar-icons-face-wink mr-2 />
        您在左下角
      </div>
      <div v-if="userStore.user!.id === pkStore.players[1]?.id" text-24px font-bold flex-center>
        <div i-akar-icons-face-wink mr-2 />
        您在右上角
      </div>
    </div>
  </div>
</template>
