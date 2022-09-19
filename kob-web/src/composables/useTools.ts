import dayjs from 'dayjs'
import { appMeta } from '~/config'

/**
 * 创建页面 `head` 数据
 */
export function useHeadMeta() {
  const { appShortName, description } = appMeta
  useHead({
    title: appShortName,
    meta: [
      { name: 'description', content: description },
      {
        name: 'theme-color',
        content: computed(() => isDark.value ? '#00aba9' : '#ffffff'),
      },
    ],
    link: [
      {
        rel: 'icon',
        type: 'image/svg+xml',
        href: computed(() => isDark.value ? '/favicon-dark.svg' : '/favicon.svg'),
      },
    ],
  })
}

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
