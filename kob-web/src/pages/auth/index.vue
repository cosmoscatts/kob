<script setup lang="ts">
import type { Ref } from 'vue'
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

const refLoginAccount = ref()
const refLoginPhone = ref()
const refRegister = ref()

/**
 * 实现 `form` 显示时，`input` 自动 `focus`
 */
function inputAutoFocus() {
  const refMap: Record<Tab, Ref> = {
    account: refLoginAccount,
    phone: refLoginPhone,
    register: refRegister,
  }
  refMap[currentTab.value]?.value?.focusFirstInput()
}

watch(() => showAuthModal, (val) => {
  if (val)
    // 等待 `form` 挂载完成
    useTimeoutFn(inputAutoFocus, 200)
})
watch(currentTab, () => {
  // 等待 `form` 挂载完成
  useTimeoutFn(inputAutoFocus, 200)
})

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
    <LoginAccount v-if="currentTab === 'account'" ref="refLoginAccount" />
    <LoginPhone v-if="currentTab === 'phone'" ref="refLoginPhone" />
    <Register v-if="currentTab === 'register'" ref="refRegister" />
  </n-modal>
</template>
