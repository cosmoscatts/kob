<script setup lang="ts">
import type { Component } from 'vue'
import { NEllipsis, NIcon } from 'naive-ui'
import type { MenuOption } from 'naive-ui'
import { RouterLink } from 'vue-router'
import type { Menu } from '~/types'
import { APP_MENUS, APP_MENU_ICON_MAP } from '~/config'

const {
  mode = 'horizontal',
} = defineProps<{
  mode?: 'vertical' | 'horizontal'
}>()

/**
 * 渲染图标
 */
function renderIcon(icon: Component) {
  return () => h(NIcon, null, { default: () => h(icon) })
}

/**
 * 渲染 RouterLink & 可省略的菜单名称
 */
function renderLabel(label: string, path?: string) {
  return path
    ? () =>
        h(
          RouterLink,
          {
            to: {
              path,
            },
          },
          { default: () => label },
        )
    : () => h(NEllipsis, null, { default: () => label })
}

/**
 * 将菜单项转换成 <NMenu> 组件需要的格式
 */
function generateMenuOption(menuItem: Menu): MenuOption {
  const { id, label, icon, path, children } = menuItem
  return {
    key: id,
    label: renderLabel(label, path),
    icon: icon && APP_MENU_ICON_MAP[icon]
      ? renderIcon(APP_MENU_ICON_MAP[icon])
      : undefined,
    path,
    children: children?.map((child: Menu) => generateMenuOption(child)) || undefined,
  }
}

const menuOptions = computed<MenuOption[]>(() => {
  return APP_MENUS.map(i => generateMenuOption(i)) || []
})

const route = useRoute()
const defaultSelectedMenuOptionKey = computed(() => { // 默认选中的 menu option
  const allMenuOptions = menuOptions.value.flatMap((i) => {
    return i.children
      ? [i, ...i.children]
      : [i]
  })
  return allMenuOptions.find(i => i.path === route.path)?.key || null
})
</script>

<template>
  <n-menu
    :mode="mode"
    :value="defaultSelectedMenuOptionKey"
    :options="menuOptions"
  />
</template>
