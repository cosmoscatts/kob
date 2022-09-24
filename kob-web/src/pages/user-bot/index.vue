<script setup lang="ts">
import BotTable from './components/BotTable.vue'
import BotPanel from './components/BotPanel.vue'
</script>

<template>
  <div
    grid="~ cols-4"
    gap-x-5
    lt-md="grid-cols-1 gap-y-5"
    w-70vw mx-a ha
  >
    <div col-span-1 ha>
      <BotPanel />
    </div>
    <div col-span-3 lt-md="col-span-1">
      <BotTable />
    </div>
  </div>
</template>
