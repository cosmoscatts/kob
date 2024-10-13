import type { ConfigProviderProps } from 'naive-ui';
import { createDiscreteApi, darkTheme, lightTheme } from 'naive-ui';
import { isDark, useThemeOverrides } from './theme';

const configProviderProps = computed<ConfigProviderProps>(() => {
  const { value: themeOverrides } = useThemeOverrides();
  return {
    theme: isDark.value ? darkTheme : lightTheme,
    themeOverrides,
  };
});

export const $discrete_api = createDiscreteApi(
  ['message', 'dialog', 'notification', 'loadingBar'],
  { configProviderProps },
);
export const $dialog = $discrete_api.dialog;
export const $message = $discrete_api.message;
export const $notification = $discrete_api.notification;
export const $loadingBar = $discrete_api.loadingBar;
