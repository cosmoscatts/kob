<script setup lang="ts">
defineProps(['user']);
</script>

<template>
  <div flex="~ col center" h-full>
    <n-avatar
      :style="{
        width: '100%',
        maxWidth: '150px',
        height: 'auto',
        cursor: 'pointer',
      }"
      lt-md="!max-w-120px"
      round
      :src="user?.avatar"
    />
    <div mt-20px text-24px font-600>
      {{ user?.name }}
    </div>
  </div>
</template>
