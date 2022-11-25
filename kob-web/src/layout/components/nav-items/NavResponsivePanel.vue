<script setup lang="ts">
import NavMenu from './NavMenu.vue'
import NavAvatar from './NavAvatar.vue'
import { appMeta } from '~/config'

let showNavPanel = $ref(false)

const { hasLogin } = storeToRefs(useUserStore())

const panelBodyColor = computed(() => ['#FFFFFF', '#121212'][Number(isDark.value)])

const toggle = () => showNavPanel = !showNavPanel
</script>

<template>
  <div flex-y-center h-full mr-10>
    <button
      v-if="hasLogin"
      icon-btn text-lg
      i-carbon-menu
      @click="toggle"
    />
    <NavAvatar v-else />
  </div>

  <n-drawer
    :style="{
      backgroundColor: panelBodyColor,
    }"
    :width="240"
    :auto-focus="false"
    :show="showNavPanel"
    placement="right"
    display-directive="show"
    @mask-click="showNavPanel = false"
  >
    <div flex-y-center h-50px ml-3>
      <NavAvatar />
      <div flex-auto />
      <a
        icon-btn text-lg i-carbon-logo-github mx-3
        :href="appMeta.github"
        target="_blank" title="GitHub"
      />
      <DarkToggle mr-3 />
    </div>
    <NavMenu mode="vertical" />
  </n-drawer>
</template>
