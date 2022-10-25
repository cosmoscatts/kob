<script setup lang="ts">
const { user } = storeToRefs(useUserStore())
</script>

<template>
  <div ha>
    <n-card hoverable>
      <div wfull flex="~ col" justify-center items-center>
        <n-avatar
          :style="{
            width: '150px',
            height: 'auto',
            cursor: 'pointer',
          }"
          size="large"
          :src="user?.avatar"
        />
        <n-divider />
        <div font-bold text-24px>
          {{ user?.name || '' }}
        </div>
      </div>
    </n-card>
  </div>
</template>
