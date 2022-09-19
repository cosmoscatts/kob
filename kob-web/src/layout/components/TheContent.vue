<script setup lang="ts">

</script>

<template>
  <div>
    <RouterView v-slot="{ Component, route }">
      <Transition name="fade-slide" mode="out-in" appear>
        <KeepAlive>
          <component :is="Component" :key="route.path" />
        </KeepAlive>
      </Transition>
    </RouterView>
  </div>
</template>

