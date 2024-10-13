import type { ThemeCommonVars } from 'naive-ui';
import { commonDark, commonLight } from 'naive-ui';
import { layoutConfig } from '~/config';
import { createPrimaryColorPalette } from '~/utils/color';
import { isDark } from './theme';

const CSS_VAR_MAP: Partial<Record<keyof ThemeCommonVars, string>> = {
  primaryColor: '--primary-color',
  primaryColorHover: '--primary-color-hover',
  primaryColorPressed: '--primary-color-pressed',
  primaryColorSuppl: '--primary-color-suppl',
  infoColor: '--info-color',
  infoColorHover: '--info-color-hover',
  infoColorPressed: '--info-color-pressed',
  infoColorSuppl: '--info-color-suppl',
  warningColor: '--warning-color',
  warningColorHover: '--warning-color-hover',
  warningColorPressed: '--warning-color-pressed',
  warningColorSuppl: '--warning-color-suppl',
  errorColor: '--error-color',
  errorColorHover: '--error-color-hover',
  errorColorPressed: '--error-color-pressed',
  errorColorSuppl: '--error-color-suppl',
  successColor: '--success-color',
  successColorHover: '--success-color-hover',
  successColorPressed: '--success-color-pressed',
  successColorSuppl: '--success-color-suppl',
};

// 使用 computed 来缓存计算结果
const themeColors = computed(() => {
  const baseColors: ThemeCommonVars = isDark.value ? commonDark : commonLight;
  const overrides = createPrimaryColorPalette(layoutConfig.primaryColor);
  return { ...baseColors, ...overrides };
});

export function updateThemeColors() {
  Object.entries(CSS_VAR_MAP).forEach(([key, cssVar]) => {
    const colorValue = themeColors.value[key as keyof ThemeCommonVars];
    if (colorValue) {
      document.documentElement.style.setProperty(cssVar, colorValue);
    }
  });
}

// 使用 watchEffect 来自动追踪依赖
watchEffect(updateThemeColors);
