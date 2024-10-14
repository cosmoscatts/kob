<script setup lang="ts">
import defaultAvatar from '~/assets/default-avatar.png';
import ChooseLevel from './components/ChooseLevel.vue';

const pkStore = usePkStore();
const userStore = useUserStore();
const { contentStyle } = useLayoutStyle({ heightProperty: 'minHeight', additionalOffset: 8 });

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
  $message.success('人机试炼开始');
  startDuelPrelude.value = true;
  useTimeoutFn(() => startDuelPrelude.value = false, 5000);
};

const handleMove = (data: any) => {
  pkStore.gameMapObject?.snakes.forEach((snake, index) =>
    snake.setDirection([data.aDirection, data.bDirection][index]));
};

const handleResult = (data: any) => {
  const [snake0, snake1] = pkStore.gameMapObject?.snakes || [];
  if (['all', 'A'].includes(data.loser))
    snake0.status = 'die';
  if (['all', 'B'].includes(data.loser))
    snake1.status = 'die';
  pkStore.updateGameState({ gameResult: getGameResult(data.loser) });
};

const socket = useSocket((msg) => {
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

const showConfetti = computed(() => {
  const { gameResult, players } = pkStore;
  const userId = userStore.user?.id;
  return (gameResult === 'playerBWon' && players[1].id === userId)
    || (gameResult === 'playerAWon' && players[0].id === userId);
});
</script>

<template>
  <div flex="col center" :style="contentStyle">
    <ChooseLevel v-if="pkStore.status === 'matching'" />
    <GamePlayground v-if="pkStore.status === 'playing' && !startDuelPrelude" />
    <DuelPreludeScreen v-if="pkStore.status === 'playing' && startDuelPrelude" />
    <ResultBoard v-if="pkStore.gameResult !== 'ongoing'" />
    <Confetti :passed="showConfetti" />

    <div v-if="pkStore.status === 'playing'" mt-15px h-5vh>
      <div text-24px font-bold flex-center>
        <div i-akar-icons-face-wink mr-2 />
        您在左下角
      </div>
    </div>
  </div>
</template>
