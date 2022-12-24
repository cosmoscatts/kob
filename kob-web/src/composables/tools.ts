import dayjs from 'dayjs'
import type { ConfigProviderProps } from 'naive-ui'
import {
  createDiscreteApi,
  darkTheme,
  lightTheme,
} from 'naive-ui'

import { R } from '~/utils'

const isDevelopment = import.meta.env.MODE === 'development'

export {
  dayjs,
  isDevelopment,
  R,
}

// ----- Naive Ui Global API -----

const configProviderProps = computed<ConfigProviderProps>(() => {
  const { value: themeOverrides } = useThemeOverrides()
  return {
    theme: isDark.value
      ? darkTheme
      : lightTheme,
    themeOverrides,
  }
})
export const $discrete_api = createDiscreteApi(
  ['message', 'dialog', 'notification', 'loadingBar'],
  { configProviderProps },
)
export const $dialog = $discrete_api.dialog
export const $message = $discrete_api.message
export const $notification = $discrete_api.notification
export const $loadingBar = $discrete_api.loadingBar

// ----- 格式化时间 -----

export const formatDate = ({
  date = new Date(),
  pattern = 'YYYY-MM-DD HH:mm:ss',
}: {
  date?: Date | string | number
  pattern?: string
}) => dayjs(date).format(pattern)

// ----- Confirm 确认框 -----

export function useConfirm(content: string, onPositiveClick: (e: MouseEvent) => unknown) {
  $dialog.warning({
    title: '警告',
    content,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick,
  })
}

// ----- Clone -----

export const useClone = <T = any>(data: T) => JSON.parse(JSON.stringify(data))
