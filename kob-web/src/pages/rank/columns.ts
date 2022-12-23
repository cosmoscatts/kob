import type { DataTableColumns } from 'naive-ui'
import { NAvatar, NEllipsis, NIcon } from 'naive-ui'
import { Medal, Trophy } from '@vicons/ionicons5'
import type { Rank } from '~/types'
import defaultAvatar from '~/assets/default-avatar.png'

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
      render: ({ avatar, name, rankNum }) => renderPlayer(avatar, name, rankNum),
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
      render: ({ createTime }) => formatDate({ date: createTime }),
    },
  ]
}

function renderPlayer(avatar?: string, name?: string, rankNum?: number) {
  const reward = []
  if (rankNum && [1, 2, 3, 4, 5, 6, 7, 8, 9, 10].includes(rankNum)) {
    const colors = ['#F7BA1E', '#8E8E8E', '#774B04', '#3491FA']
    const color = rankNum <= 3 ? colors[rankNum - 1] : colors[3]
    const component = rankNum <= 3 ? Trophy : Medal
    reward.push(h(
      NIcon,
      {
        component,
        color,
        size: 16,
        style: {
          marginLeft: '15px',
        },
      },
    ))
  }
  return h('div', {
    style: {
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
    },
  },
  [
    h(NAvatar, {
      size: 'small',
      round: true,
      src: avatar ?? defaultAvatar,
    }),
    h(NEllipsis, {
      maxWidth: '200px',
      style: {
        marginLeft: '15px',
      },
    },
    () => name),
    ...reward,
  ])
}
