import type { DataTableColumns } from 'naive-ui';
import { NButton } from 'naive-ui';
import type { Bot } from '~/types';

interface ColumnOptions {
  createRowNumber?: (rowIndex: number) => number
  onUpdateBot?: (bot: Bot) => void | Promise<void>
  onRemoveBot?: (bot: Bot) => void | Promise<void>
}

export function createColumns({
  createRowNumber,
  onUpdateBot,
  onRemoveBot,
}: ColumnOptions): DataTableColumns<Bot> {
  return [
    {
      key: 'id',
      title: '序号',
      align: 'center',
      render: (_row, rowIndex) => createRowNumber?.(rowIndex),
    },
    {
      title: '标题',
      key: 'title',
      align: 'center',
    },
    {
      title: '描述',
      key: 'description',
      align: 'center',
    },
    {
      title: '创建时间',
      key: 'createTime',
      align: 'center',
      render: ({ createTime }) => formatDate({ date: createTime }),
    },
    {
      title: '操作',
      key: 'actions',
      align: 'center',
      render: row => h('div', {}, [
        h(NButton, {
          size: 'small',
          tertiary: true,
          type: 'warning',
          style: { marginRight: '10px' },
          onClick: () => onUpdateBot?.(row),
        }, { default: () => '编辑' }),
        h(NButton, {
          size: 'small',
          tertiary: true,
          type: 'error',
          onClick: () => onRemoveBot?.(row),
        }, { default: () => '删除' }),
      ]),
    },
  ];
}
