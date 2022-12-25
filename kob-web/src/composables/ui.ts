import type { GlobalThemeOverrides, ThemeCommonVars } from 'naive-ui'
import { commonDark, commonLight } from 'naive-ui'
import type { ComputedRef } from 'vue'
import { breakpointsTailwind } from '@vueuse/core'
import { createPrimaryColor } from '~/utils'
import { APP_LAYOUT_PARAMS } from '~/config'

// ----- Theme -----

const THEME_MODE_KEY = 'THEME_MODE'
localStorage.setItem(THEME_MODE_KEY, 'dark') // 默认暗色
export const isDark = useDark({
  storageKey: THEME_MODE_KEY,
  valueDark: 'dark',
  valueLight: 'light',
})
export const toggleDark = useToggle(isDark)

export function useThemeOverrides(): ComputedRef<GlobalThemeOverrides> {
  const overrides = createPrimaryColor(APP_LAYOUT_PARAMS.primaryColor)
  return computed<GlobalThemeOverrides>(() => {
    const bodyColor = ['#ffffff', '#121212'][Number(isDark.value)]
    const cardColor = ['#fefefe', '#131313'][Number(isDark.value)]
    const modalColor = ['#ffffff', '#262626'][Number(isDark.value)]

    return {
      common: { ...overrides },
      LoadingBar: {
        colorLoading: APP_LAYOUT_PARAMS.primaryColor,
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
    }
  })
}

// ----- 响应式 -----

export function useResponsive() {
  const breakpoints = useBreakpoints(breakpointsTailwind)
  const isMobile = breakpoints.smaller('sm')
  const isPC = breakpoints.greaterOrEqual('sm')
  const labelHidden = breakpoints.smaller('md') // 隐藏 [form label]
  return {
    breakpoints,
    isMobile,
    isPC,
    labelHidden,
  }
}

// ----- Color -----

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

export function writeThemeColorsToBody() {
  const overrides = createPrimaryColor(APP_LAYOUT_PARAMS.primaryColor)

  const colors: ThemeCommonVars = isDark.value
    ? commonDark
    : commonLight
  const mergedColors = {
    ...colors,
    ...overrides,
  }
  Object.entries(colorPropertyMap).forEach(([key, value]) => {
    document.body.style.setProperty(value, mergedColors[key as keyof ThemeCommonVars])
  })
}

watch(isDark, writeThemeColorsToBody)

// ----- Content Height -----

const { navHeight, footHeight, contentPadding } = APP_LAYOUT_PARAMS
export const diffHeight = computed(() => navHeight + footHeight + contentPadding * 2 + 1 + 1 + 3)
