import type { Ref } from 'vue';
import { ref } from 'vue';

interface BooleanControls {
  value: Ref<boolean>
  setValue: (value: boolean) => void
  setTrue: () => void
  setFalse: () => void
  toggle: () => void
}

interface LoadingControls {
  loading: Ref<boolean>
  setLoading: (value: boolean) => void
  startLoading: () => void
  endLoading: () => void
  toggleLoading: () => void
}

export function useBoolean(initialValue = false): BooleanControls {
  const value = ref(initialValue);

  return {
    value,
    setValue: (newValue: boolean) => value.value = newValue,
    setTrue: () => value.value = true,
    setFalse: () => value.value = false,
    toggle: () => value.value = !value.value,
  };
}

export function useLoading(initialValue = false): LoadingControls {
  const { value, setValue, setTrue, setFalse, toggle } = useBoolean(initialValue);

  return {
    loading: value,
    setLoading: setValue,
    startLoading: setTrue,
    endLoading: setFalse,
    toggleLoading: toggle,
  };
}
