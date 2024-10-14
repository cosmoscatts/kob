<script setup lang="ts">
import type { MenuOption } from 'naive-ui';
import type { Component } from 'vue';
import { NEllipsis, NIcon } from 'naive-ui';
import { RouterLink } from 'vue-router';
import { appMenuIconMap, appMenus } from '~/config';
import type { Menu } from '~/types';

const route = useRoute();

const renderIcon = (icon: Component) => () => h(NIcon, null, { default: () => h(icon) });
const renderLabel = (label: string, path?: string) =>
  path
    ? () => h(RouterLink, { to: { path } }, { default: () => label })
    : () => h(NEllipsis, null, { default: () => label });

const generateMenuOption = ({ id, label, icon, path, children }: Menu): MenuOption => ({
  key: id,
  label: renderLabel(label, path),
  icon: icon && appMenuIconMap[icon] ? renderIcon(appMenuIconMap[icon]) : undefined,
  children: children?.map(generateMenuOption),
});

const options = computed(() => appMenus.map(generateMenuOption));

const selectedOptionValues = computed(() => {
  const flattenMenus = (menus: typeof appMenus): typeof appMenus =>
    menus.flatMap(menu => menu.children
      ? [menu, ...flattenMenus(menu.children)]
      : [menu],
    );
  const allMenuOptions = flattenMenus(appMenus);
  return allMenuOptions.find(menu => menu.path === route.path)?.id ?? null;
});
</script>

<template>
  <n-menu
    mode="horizontal"
    :value="selectedOptionValues"
    :options="options"
  />
</template>
