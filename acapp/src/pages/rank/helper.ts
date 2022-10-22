import type { DataTableColumns } from 'naive-ui'
import { NAvatar, NEllipsis } from 'naive-ui'
import type { Rank } from '~/types'

/**
 * 创建表格列
 */
export function createColumns({
  createRowNumber,
}: {
  createRowNumber?: (rowIndex: number) => number
}): DataTableColumns<Rank> {
  return [
    {
      key: 'id',
      title: '序号',
      align: 'center',
      render(_row, rowIndex) {
        return createRowNumber?.(rowIndex)
      },
    },
    {
      title: '玩家',
      key: 'id',
      align: 'center',
      render({ avatar, name }) {
        return renderPlayer(avatar, name)
      },
    },
    {
      title: '天梯分',
      key: 'loser',
      align: 'center',
      render({ rating }) {
        return `${rating} 分`
      },
    },
    {
      title: '创建时间',
      key: 'description',
      align: 'center',
      render({ createTime }) {
        return formatDate({ date: createTime })
      },
    },
  ]
}

/**
 * 渲染玩家单元格内容
 */
function renderPlayer(avatar?: string, name?: string) {
  return h(
    'div',
    {
      style: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
      },
    },
    [
      h(
        NAvatar,
        {
          size: 'small',
          round: true,
          src: avatar,
        },
      ),
      h(
        NEllipsis,
        {
          maxWidth: '200px',
          style: {
            marginLeft: '15px',
          },
        },
        () => name,
      ),
    ],
  )
}
