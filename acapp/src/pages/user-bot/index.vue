<script setup lang="ts">
import type { Ref } from 'vue'
import BotTable from './components/BotTable.vue'
import BotPanel from './components/BotPanel.vue'

const containerWidth = inject<Ref<number>>('containerWidth')!
</script>

<template>
  <div
    grid="~ cols-4"
    gap-x-5
    ha pt-20px
    :style="{
      width: containerWidth < 768 ? '96%' : '80%',
      marginLeft: containerWidth < 768 ? '2%' : '10%',
    }"
    :class="containerWidth < 1024 ? '!grid-cols-1 !gap-y-5' : ''"
  >
    <div col-span-1 ha>
      <BotPanel />
    </div>
    <div col-span-3 :class="containerWidth < 1024 ? '!col-span-1' : ''">
      <BotTable />
    </div>
  </div>
</template>
