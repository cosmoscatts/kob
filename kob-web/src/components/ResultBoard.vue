<script setup lang="ts">
const pkStore = usePkStore()
const { updateStatus, updateOpponent, updateLoser } = pkStore
const { players, loser } = storeToRefs(pkStore)
const { user } = storeToRefs(useUserStore())

function restart() {
  updateStatus('match')
  updateOpponent()
  updateLoser('none')
}
</script>

<template>
  <div h-30vh w-30vw min-h-150px min-w-150px absolute top-30vh left-35vw flex="col center" bg="[rgba(50,50,50,0.5)]">
    <div v-if="loser === 'all'" class="result-board-text">
      Draw
    </div>
    <div v-else-if="loser === 'A' && players[0].id === user!.id" class="result-board-text">
      Lose
    </div>
    <div v-else-if="loser === 'B' && players[1].id === user!.id" class="result-board-text">
      Lose
    </div>
    <div v-else class="result-board-text">
      Win
    </div>
    <div mt-2vh>
      <n-button type="primary" text-color="white" @click="restart">
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
