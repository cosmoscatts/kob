<script setup lang="ts">
const { user } = storeToRefs(useUserStore())
</script>

<template>
  <div ha>
    <n-card hoverable>
      <div wfull flex="~ col" justify-center items-center>
        <n-avatar
          :style="{
            width: '100%',
            height: 'auto',
            cursor: 'pointer',
          }"
          lt-md="max-w-150px"
          size="large"
          :src="user?.avatar"
        />
        <n-divider />
        <div font-bold text-lg>
          {{ user?.name || '' }}
        </div>
      </div>
    </n-card>
  </div>
</template>
