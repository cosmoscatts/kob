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
            maxWidth: '250px',
            height: 'auto',
            cursor: 'pointer',
          }"
          lt-md="max-w-150px"
          size="large"
          :src="user?.avatar"
          @click="$router.push('/profile')"
        />
        <n-divider />
        <n-button>
          更换头像
        </n-button>
      </div>
    </n-card>
  </div>
</template>
