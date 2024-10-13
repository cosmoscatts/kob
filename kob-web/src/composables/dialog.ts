import { $dialog } from './naive-ui-api';

export function useConfirm(content: string, onPositiveClick: (e: MouseEvent) => unknown) {
  $dialog.warning({
    title: '警告',
    content,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick,
  });
}
