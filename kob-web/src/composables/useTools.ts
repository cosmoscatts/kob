import dayjs from 'dayjs'

export {
  dayjs,
}

export function formatDate({
  date = new Date(),
  pattern = 'YYYY-MM-DD HH:mm:ss',
}: {
  date?: Date | string | number
  pattern?: string
}) {
  return dayjs(date).format(pattern)
}

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
