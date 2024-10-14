<script setup lang="ts">
import PasswordChangeForm from './PasswordChangeForm.vue';

const props = withDefaults(defineProps<{
  formIndex?: number
  title?: string
}>(), {
  formIndex: 0,
  title: '安全设置',
});
const modalVisible = defineModel('modalVisible', { default: false });

const bodyStyle = { width: '390px' };

const router = useRouter();
const userStore = useUserStore();
const { setAuthModalVisibility, logout } = userStore;

const securityActionCallback = () => {
  modalVisible.value = false;
  logout();
  router.push('/home');
  setAuthModalVisibility(true);
};

provide('securityActionCallback', securityActionCallback);

const currentForm = computed(() => {
  switch (props.formIndex) {
    case 0:
      return PasswordChangeForm;
    default:
      return null;
  }
});
</script>

<template>
  <n-modal
    :show="modalVisible"
    size="huge"
    preset="card"
    :style="bodyStyle"
    :bordered="true"
    @close="modalVisible = false"
  >
    <template #header>
      <span text-2xl font-bold>{{ title }}</span>
    </template>
    <component :is="currentForm" v-if="currentForm" />
  </n-modal>
</template>
