<script setup lang="ts">
import BaseInformation from './components/BaseInformation.vue';
import SecuritySettings from './components/SecuritySettings.vue';
import UserPanel from './components/UserPanel.vue';
</script>

<template>
  <div
    grid="~ cols-4"
    gap-x-5
    lt-md="grid-cols-1 gap-y-5"
    w-70vw mx-a ha
  >
    <div col-span-1 ha>
      <UserPanel />
    </div>
    <div col-span-3 lt-md="col-span-1">
      <div flex="~ col" w-full gap-y-5>
        <BaseInformation />
        <SecuritySettings />
      </div>
    </div>
  </div>
</template>
