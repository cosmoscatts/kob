<script setup lang="ts">
import defaultAvatar from '~/assets/default-avatar.png';

const pkStore = usePkStore();
const userStore = useUserStore();
const { contentStyle } = useLayoutStyle({ heightProperty: 'minHeight', additionalOffset: 8 });

// 重置对手信息
pkStore.updateGameState({ opponent: undefined });

const startDuelPrelude = ref(false);

const handleMatchSuccess = (data: any) => {
  pkStore.updateGameState({
    opponent: {
      name: data?.opponentName || '-',
      avatar: data?.opponentAvatar ?? defaultAvatar,
    },
    status: 'playing',
  });
  pkStore.updateGame(data.game);
  $message.success('您的对手已找到');
  startDuelPrelude.value = true;
  useTimeoutFn(() => { startDuelPrelude.value = false; }, 5000);
};

const handleMove = (data: any) => {
  pkStore.gameMapObject?.snakes.forEach((snake, index) =>
    snake.setDirection([data.aDirection, data.bDirection][index]),
  );
};

const handleResult = (data: any) => {
  const [snake0, snake1] = pkStore.gameMapObject?.snakes || [];
  if (['all', 'A'].includes(data.loser))
    snake0.status = 'die';
  if (['all', 'B'].includes(data.loser))
    snake1.status = 'die';

  pkStore.updateGameState({ gameResult: getGameResult(data.loser) });
};

const socket = useSocket((msg: any) => {
  const data = JSON.parse(msg.data);
  const fns: [boolean, () => void][] = [
    [data.event === 'match-success', () => handleMatchSuccess(data)],
    [data.event === 'move', () => handleMove(data)],
    [data.event === 'result', () => handleResult(data)],
  ];
  ConditionalExecutor.executeFirst(fns);
});

onUnmounted(() => {
  socket.close();
  pkStore.resetGame();
});

const showConfetti = computed(() =>
  (pkStore.gameResult === 'playerBWon' && pkStore.players[1]?.id === userStore.user?.id)
  || (pkStore.gameResult === 'playerAWon' && pkStore.players[0]?.id === userStore.user?.id),
);

const playerPosition = computed(() => {
  if (userStore.user?.id === pkStore.players[0]?.id)
    return '左下角';
  if (userStore.user?.id === pkStore.players[1]?.id)
    return '右上角';
  return null;
});
</script>

<template>
  <div :style="contentStyle" flex="col center">
    <GameMatchGround v-if="pkStore.status === 'matching'" />
    <GamePlayground v-if="pkStore.status === 'playing' && !startDuelPrelude" />
    <DuelPreludeScreen v-if="pkStore.status === 'playing' && startDuelPrelude" />
    <ResultBoard v-if="pkStore.gameResult !== 'ongoing'" />
    <Confetti :passed="showConfetti" />

    <div v-if="pkStore.status === 'playing' && playerPosition" mt-15px h-5vh>
      <div text-24px font-bold flex-center>
        <div i-akar-icons-face-wink mr-2 />
        您在{{ playerPosition }}
      </div>
    </div>
  </div>
</template>
