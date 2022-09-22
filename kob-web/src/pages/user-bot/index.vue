<script setup lang="ts">
import BotTable from './components/BotTable.vue'
import BotPanel from './components/BotPanel.vue'
</script>

<template>
  <div
    grid="~ cols-4" gap-x-5 lt-md="grid-rows-2 grid-cols-1"
    w-70vw mx-a ha
  >
    <div col-span-1>
      <BotPanel />
    </div>
    <div col-span-3 lt-md="col-span-1">
      <BotTable />
    </div>
  </div>
</template>
