<script setup lang="ts">
import type { Ref } from 'vue'
import BotTable from './components/BotTable.vue'
import BotPanel from './components/BotPanel.vue'

const containerWidth = inject<Ref<number>>('containerWidth')!
</script>

<template>
  <div
    grid="~ cols-4" gap5
    :style="{
      width: '96%',
      marginLeft: '2%',
      height: containerWidth < 768 ? 'auto' : '370px',
    }"
    :class="containerWidth < 768 ? '!grid-cols-1' : ''"
  >
    <div col-span-1 ha>
      <BotPanel />
    </div>
    <div col-span-3 :class="containerWidth < 768 ? '!col-span-1 ha' : ''">
      <BotTable />
    </div>
  </div>
</template>
