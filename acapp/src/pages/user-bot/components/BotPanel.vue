<script setup lang="ts">
const { user } = storeToRefs(useUserStore())
</script>

<template>
  <div ha>
    <n-card hoverable>
      <div flex="~ col" w-full flex-center>
        <n-avatar
          :style="{
            width: '100%',
            height: 'auto',
            cursor: 'pointer',
          }"
          lt-md="max-w-150px"
          size="large"
          :src="user?.avatar"
          @click="$router.push('/profile')"
        />
        <n-divider />
        <div font-bold text-lg>
          {{ user?.name || '-' }}
        </div>
      </div>
    </n-card>
  </div>
</template>
