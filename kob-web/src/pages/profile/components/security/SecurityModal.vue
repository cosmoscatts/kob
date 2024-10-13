<script setup lang="ts">
import SecurityPasswordForm from './SecurityPasswordForm.vue';

const {
  modalVisible = false,
  formIndex = 0,
  title = '安全设置',
} = defineProps<{
  modalVisible?: boolean
  formIndex?: number
  title?: string
}>();

const emit = defineEmits(['update:modalVisible']);

const bodyStyle = {
  width: '390px',
};
const router = useRouter();
const { setAuthModalVisible, logout } = useUserStore();

const securityActionCallback = () => {
  emit('update:modalVisible', false);
  logout();
  router.push('/home');
  setAuthModalVisible(true);
};

provide('securityActionCallback', securityActionCallback);
</script>

<template>
  <n-modal
    :show="modalVisible"
    size="huge"
    preset="card"
    :style="bodyStyle"
    :bordered="true"
    @close="$emit('update:modalVisible', false)"
  >
    <template #header>
      <span text="1.5rem" font-bold>{{ title }}</span>
    </template>
    <SecurityPasswordForm v-if="formIndex === 0" />
  </n-modal>
</template>
