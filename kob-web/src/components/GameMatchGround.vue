<script setup lang="ts">
const { user } = storeToRefs(useUserStore())
</script>

<template>
  <div w-60vw h-70vh mx-a class="container" op-50>
    <div grid="~ cols-2">
      <div col-span-1 border="1 red" h-50vh>
        <div flex="~ col center" h-full>
          <n-avatar
            :style="{
              width: '100%',
              maxWidth: '250px',
              height: 'auto',
              cursor: 'pointer',
            }"
            lt-md="max-w-150px"
            size="large"
            :src="user?.avatar"
            @click="$router.push('/profile')"
          />
          <div mt-20px>
            {{ user?.name }}
          </div>
        </div>
      </div>
      <div col-span-1 border="1 red">
        <div flex="~ col center" h-full>
          <n-avatar
            :style="{
              width: '100%',
              maxWidth: '250px',
              height: 'auto',
              cursor: 'pointer',
            }"
            lt-md="max-w-150px"
            size="large"
            :src="user?.avatar"
            @click="$router.push('/profile')"
          />
          <div mt-20px>
            {{ user?.name }}
          </div>
        </div>
      </div>
      <div col-span-2 border="1 red" h-20vh>
        1
      </div>
    </div>
  </div>
</template>

<style scoped>
  .container {
    background-color: rgba(50,50,50,.5);
  }
</style>
