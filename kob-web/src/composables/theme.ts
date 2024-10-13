import type { GlobalThemeOverrides } from 'naive-ui';
import type { ComputedRef } from 'vue';
import { layoutConfig } from '~/config';
import { createPrimaryColorPalette } from '~/utils/color';

const THEME_MODE_KEY = 'THEME_MODE';
localStorage.setItem(THEME_MODE_KEY, 'dark'); // 默认暗色
export const isDark = useDark({
  storageKey: THEME_MODE_KEY,
  valueDark: 'dark',
  valueLight: 'light',
});
export const toggleDark = useToggle(isDark);

export function useThemeOverrides(): ComputedRef<GlobalThemeOverrides> {
  const overrides = createPrimaryColorPalette(layoutConfig.primaryColor);
  return computed<GlobalThemeOverrides>(() => {
    const bodyColor = ['#ffffff', '#121212'][Number(isDark.value)];
    const cardColor = ['#fefefe', '#131313'][Number(isDark.value)];
    const modalColor = ['#ffffff', '#262626'][Number(isDark.value)];

    return {
      common: { ...overrides },
      LoadingBar: {
        colorLoading: layoutConfig.primaryColor,
      },
      Layout: {
        color: bodyColor,
        headerColor: bodyColor,
        footerColor: bodyColor,
      },
      Card: {
        color: cardColor,
        colorModal: modalColor,
      },
      Dropdown: {
        color: modalColor,
      },
      DataTable: {
        thColor: 'transparent',
        thColorHover: 'transparent',
        tdColor: 'transparent',
        tdColorHover: 'transparent',
      },
      Dialog: {
        color: modalColor,
      },
      Message: {
        color: modalColor,
        colorInfo: modalColor,
        colorSuccess: modalColor,
        colorWarning: modalColor,
        colorError: modalColor,
        colorLoading: modalColor,
      },
      Notification: {
        color: modalColor,
      },
      InternalSelection: {
        color: modalColor,
      },
      InternalSelectMenu: {
        color: modalColor,
      },
      List: {
        color: cardColor,
      },
      Drawer: {
        color: modalColor,
      },
      Tooltip: {
        color: modalColor,
        textColor: ['rgb(51, 54, 57)', 'rgba(255, 255, 255, 0.82)'][Number(isDark.value)],
      },
    };
  });
}
