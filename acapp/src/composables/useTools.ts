import dayjs from 'dayjs'
import type { ConfigProviderProps } from 'naive-ui'
import {
  createDiscreteApi,
  darkTheme,
} from 'naive-ui'

export {
  dayjs,
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

/**
 * 封装 `loading` 通用方法
 */
export function useLoading(initValue = false) {
  const {
    bool: loading,
    setBool: setLoading,
    setTrue: startLoading,
    setFalse: endLoading,
    toggle: toggleLoading,
  } = useBoolean(initValue)

  return {
    loading,
    setLoading,
    startLoading,
    endLoading,
    toggleLoading,
  }
}

export function useGlobalNaiveApi() {
  const configProviderProps = computed<ConfigProviderProps>(() => {
    const { value: themeOverrides } = useThemeOverrides()
    return {
      theme: darkTheme,
      themeOverrides: {
        ...themeOverrides,
      },
    }
  })

  const {
    dialog,
    message,
    notification,
    loadingBar,
  } = createDiscreteApi(
    ['message', 'dialog', 'notification', 'loadingBar'],
    {
      configProviderProps,
      dialogProviderProps: { to: '#container' },
      messageProviderProps: { to: '#container' },
      notificationProviderProps: { to: '#container' },
    },
  )

  return {
    dialog,
    message,
    notification,
    loadingBar,
  }
}

