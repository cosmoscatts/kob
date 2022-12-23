export function useBoolean(initValue = false) {
  const bool = ref(initValue)
  const setBool = (value: boolean) => bool.value = value
  const setTrue = () => setBool(true)
  const setFalse = () => setBool(false)
  const toggle = () => setBool(!bool.value)
  return {
    bool,
    setBool,
    setTrue,
    setFalse,
    toggle,
  }
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
