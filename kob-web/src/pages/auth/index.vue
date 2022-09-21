<script setup lang="ts">
const {
  showAuthModal = false,
} = defineProps<{
  showAuthModal?: boolean
}>()

const bodyStyle = {
  width: '600px',
}
const segmented = {
  content: 'soft',
  footer: 'soft',
}
</script>

<template>
  <n-modal
    :show="showAuthModal"
    preset="card"
    :style="bodyStyle"
    title="卡片预设"
    size="huge"
    :bordered="false"
    :segmented="segmented"
  >
    <template #header-extra>
      噢!
    </template>
    内容
    <template #footer>
      尾部
    </template>
  </n-modal>
</template>
