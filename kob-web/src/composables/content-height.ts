import { layoutConfig } from '~/config';

type HeightProperty = 'height' | 'minHeight' | 'maxHeight';

export function useLayoutStyle(options: {
  heightProperty?: HeightProperty
  additionalOffset?: number
} = {}) {
  const { heightProperty = 'height', additionalOffset = 0 } = options;
  const { navHeight, footHeight, contentPadding } = layoutConfig;

  const layoutOffsetHeight = computed(() =>
    navHeight + footHeight + contentPadding * 2 + additionalOffset,
  );

  const contentHeight = computed(() => `calc(100vh - ${layoutOffsetHeight.value}px)`);

  const contentStyle = computed(() => ({
    [heightProperty]: contentHeight.value,
  }));

  return {
    layoutOffsetHeight,
    contentHeight,
    contentStyle,
  };
}
