<script setup lang="ts">
import type { MenuOption } from 'naive-ui';
import type { Component } from 'vue';
import { NEllipsis, NIcon, NMenu } from 'naive-ui';
import { computed } from 'vue';
import { RouterLink, useRoute } from 'vue-router';

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
  const flattenOptions = (menuOptions: MenuOption[]): MenuOption[] =>
    menuOptions.flatMap(option =>
      option.children
        ? [option, ...flattenOptions(option.children as MenuOption[])]
        : [option],
    );

  const allOptions = flattenOptions(options.value);
  return allOptions.find(option => option.path === route.path)?.key ?? null;
});
</script>

<template>
  <NMenu
    mode="horizontal"
    :value="selectedOptionValues"
    :options="options"
  />
</template>
