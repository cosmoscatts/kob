<script setup lang="ts">
import type { Ref } from 'vue'

const { user } = storeToRefs(useUserStore())
const containerWidth = inject<Ref<number>>('containerWidth')!
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
            maxWidth: containerWidth < 1024 ? '150px' : undefined,
          }"
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
