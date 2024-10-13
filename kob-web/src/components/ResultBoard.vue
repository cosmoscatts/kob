<script setup lang="ts">
const pkStore = usePkStore();
const { players, gameResult } = storeToRefs(pkStore);
const userStore = useUserStore();
</script>

<template>
  <div h-30vh w-30vw min-h-150px min-w-150px absolute bottom-30vh left-35vw flex="col center" bg="[rgba(50,50,50,0.5)]" rounded-1>
    <div v-if="gameResult === 'draw'" class="result-board-text">
      Draw
    </div>
    <div v-else-if="gameResult === 'playerBWon' && players[0].id === userStore.user!.id" class="result-board-text">
      Lose
    </div>
    <div v-else-if="gameResult === 'playerAWon' && players[1].id === userStore.user!.id" class="result-board-text">
      Lose
    </div>
    <div v-else class="result-board-text">
      Win
    </div>
    <div mt-2vh>
      <n-button type="primary" text-color="white" @click="pkStore.resetGame">
        <span font-bold>再来亿把</span>
      </n-button>
    </div>
  </div>
</template>

<style scoped>
div.result-board-text {
  text-align: center;
  color: white;
  font-size: 50px;
  font-weight: 600;
  font-style: italic;
}
</style>
