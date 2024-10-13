<script setup lang="ts">
import type { MenuOption } from 'naive-ui';
import type { Component } from 'vue';
import { NEllipsis, NIcon } from 'naive-ui';
import { RouterLink } from 'vue-router';
import { appMenuIconMap, appMenus } from '~/config';
import type { Menu } from '~/types';

const renderIcon = (icon: Component) => () => h(NIcon, null, { default: () => h(icon) });
const renderLabel = (label: string, path?: string) => (path
  ? () => h(RouterLink, { to: { path } }, { default: () => label })
  : () => h(NEllipsis, null, { default: () => label }));

const generateMenuOption: (menu: Menu) => MenuOption = ({ id, label, icon, path, children }: Menu) => ({
  key: id,
  path,
  label: renderLabel(label, path),
  icon: icon && appMenuIconMap[icon]
    ? renderIcon(appMenuIconMap[icon])
    : undefined,
  children: children?.map((child: Menu) => generateMenuOption(child)),
});

const options = computed<MenuOption[]>(() => appMenus.map(i => generateMenuOption(i)));

const route = useRoute();
const selectedOptionValues = computed(() => { // 默认选中的 menu option
  const allMenuOptions = options.value.flatMap(i => (i.children
    ? [i, ...i.children]
    : [i]));
  return allMenuOptions.find(i => i.path === route.path)?.key || null;
});
</script>

<template>
  <n-menu
    mode="horizontal"
    :value="selectedOptionValues"
    :options="options"
  />
</template>
