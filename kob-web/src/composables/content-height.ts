import { layoutConfig } from '~/config';

const { navHeight, footHeight, contentPadding } = layoutConfig;

export const diffHeight = computed(() => navHeight + footHeight + contentPadding * 2);
