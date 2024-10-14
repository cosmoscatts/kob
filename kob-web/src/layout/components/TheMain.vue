<template>
  <div>
    <RouterView v-slot="{ Component, route }">
      <Transition name="fade-slide" mode="out-in" appear>
        <div>
          <component :is="Component" :key="route.path" />
        </div>
      </Transition>
    </RouterView>
  </div>
</template>
