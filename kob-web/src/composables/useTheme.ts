import type { GlobalThemeOverrides } from 'naive-ui'
import { commonDark, commonLight } from 'naive-ui'
import type { ComputedRef } from 'vue'
import { generatePrimaryColor } from '~/utils'
import { appLayout } from '~/config'

export const isDark = useDark()
export const toggleDark = useToggle(isDark)

export function useThemeOverrides(): ComputedRef<GlobalThemeOverrides> {
  const primaryColorOverrides = generatePrimaryColor(appLayout.primaryColor)

  const themeOverrides = computed<GlobalThemeOverrides>(() => {
    const bodyColor = isDark.value
      ? '#121212'
      : '#ffffff'
    const cardColor = isDark.value
      ? '#131313'
      : '#fefefe'
    const modalColor = isDark.value
      ? '#262626'
      : '#ffffff'
    return {
      common: {
        ...primaryColorOverrides,
      },
      LoadingBar: {
        colorLoading: appLayout.primaryColor,
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
    }
  })
  return themeOverrides
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
  const primaryColorOverrides = generatePrimaryColor(appLayout.primaryColor)

  const colors: any = isDark.value
    ? commonDark
    : commonLight

  const mergedColors = {
    ...colors,
    ...primaryColorOverrides,
  }

  Object.entries(colorPropertyMap).forEach(([key, value]) => {
    document.body.style.setProperty(value, mergedColors[key])
  })
}

watch(isDark, writeThemeColorsToBody)
