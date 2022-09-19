import type { GlobalThemeOverrides } from 'naive-ui'
import { commonDark, commonLight } from 'naive-ui'

export const isDark = useDark()
export const toggleDark = useToggle(isDark)

export function useThemeOverrides(): GlobalThemeOverrides {
  return {

  }
}

const colorPropertyMap: { [key: string]: string } = {
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
}

/**
 * 将 `naive ui` 的通用颜色，并写入 `body`
 */
export function writeThemeColorsToBody() {
  const colors: any = isDark.value
    ? commonDark
    : commonLight

  Object.entries(colorPropertyMap).forEach(([key, value]) => {
    document.body.style.setProperty(value, colors[key])
  })
}

watch(isDark, writeThemeColorsToBody)
