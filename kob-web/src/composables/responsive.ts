import { breakpointsTailwind, useBreakpoints } from '@vueuse/core';

export function useResponsive() {
  const breakpoints = useBreakpoints(breakpointsTailwind);
  const isMobile = breakpoints.smaller('sm');
  const isPC = breakpoints.greaterOrEqual('sm');

  return {
    breakpoints,
    isMobile,
    isPC,
  };
}
