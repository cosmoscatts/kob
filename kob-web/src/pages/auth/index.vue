<script setup lang="ts">
import { LoginAccount, LoginPhone, Register } from './components'
import type { Tab } from './components'

const {
  showAuthModal = false,
} = defineProps<{
  showAuthModal?: boolean
}>()

const emits = defineEmits(['update:showAuthModal'])

const bodyStyle = {
  width: '390px',
}

const currentTab = ref<Tab>('account')

const title = computed(() => {
  const { value: tab } = currentTab
  return {
    account: '账密登录',
    phone: '验证码登录',
    register: '用户注册',
  }[tab]
})

function changeTab(tab: Tab) {
  currentTab.value = tab
}

function close() {
  currentTab.value = 'account'
  emits('update:showAuthModal', false)
}

provide('tab', currentTab)
provide('changeTab', changeTab)
</script>

<template>
  <n-modal
    :show="showAuthModal"
    size="huge"
    preset="card"
    :style="bodyStyle"
    :bordered="true"
    @close="close"
  >
    <template #header>
      <span text="1.5rem" font-bold>{{ title }}</span>
    </template>
    <LoginAccount v-if="currentTab === 'account'" />
    <LoginPhone v-if="currentTab === 'phone'" />
    <Register v-if="currentTab === 'register'" />
  </n-modal>
</template>
