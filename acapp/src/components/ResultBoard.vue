<script setup lang="ts">
const pkStore = usePkStore()
const { reset } = pkStore
const { players, loser } = storeToRefs(pkStore)
const { user } = storeToRefs(useUserStore())
</script>

<template>
  <div
    w250px h200px flex="~ col" justify-center items-center
    absolute bottom-200px bg="[rgba(50,50,50,0.5)]" rounded-1
    :style="{
      left: 'calc(50% - 125px)',
    }"
  >
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
      <n-button type="primary" text-color="white" @click="reset">
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
