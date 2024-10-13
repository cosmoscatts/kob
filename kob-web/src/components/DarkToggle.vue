<script setup lang="ts">
const toggle = toggleDark;
</script>

<template>
  <button
    icon-btn text-lg
    i="carbon-light dark:carbon-moon"
    @click="toggle()"
  />
</template>
