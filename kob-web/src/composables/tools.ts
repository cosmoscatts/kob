import dayjs from 'dayjs'
import type { ConfigProviderProps } from 'naive-ui'
import {
  createDiscreteApi,
  darkTheme,
  lightTheme,
} from 'naive-ui'
import lottie from 'lottie-web'
import type { RendererType } from 'lottie-web'
import { R } from '~/utils'

const isDevelopment = import.meta.env.MODE === 'development'

export {
  dayjs,
  isDevelopment,
  R,
}

/**
 * 格式化时间
 */
export function formatDate({
  date = new Date(),
  pattern = 'YYYY-MM-DD HH:mm:ss',
}: {
  date?: Date | string | number
  pattern?: string
}) {
  return dayjs(date).format(pattern)
}

export function useGlobalNaiveApi() {
  const configProviderProps = computed<ConfigProviderProps>(() => {
    const { value: themeOverrides } = useThemeOverrides()
    return {
      theme: isDark.value
        ? darkTheme
        : lightTheme,
      themeOverrides,
    }
  })

  const {
    dialog,
    message,
    notification,
    loadingBar,
  } = createDiscreteApi(
    ['message', 'dialog', 'notification', 'loadingBar'],
    { configProviderProps },
  )

  return {
    dialog,
    message,
    notification,
    loadingBar,
  }
}

export function useLottie({
  container,
  path,
  loop = true,
  renderer = 'svg',
}: {
  container: Element
  path: string
  loop?: boolean
  renderer?: RendererType
}) {
  lottie.loadAnimation({
    container,
    path,
    loop,
    renderer,
  })
}
