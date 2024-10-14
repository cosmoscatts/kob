import type { DataTableColumns } from 'naive-ui';
import { Medal, Trophy } from '@vicons/ionicons5';
import { NAvatar, NEllipsis, NIcon } from 'naive-ui';
import defaultAvatar from '~/assets/default-avatar.png';
import type { Rank } from '~/types';

const TOP_RANKS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
const MEDAL_COLORS = ['#F7BA1E', '#8E8E8E', '#774B04', '#3491FA'];

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
      render: (_row, rowIndex) => createRowNumber?.(rowIndex),
    },
    {
      title: '玩家',
      key: 'id',
      align: 'center',
      render: ({ avatar, name, rankNum }) => renderPlayer(avatar, name, rankNum),
    },
    {
      title: '天梯分',
      key: 'rating',
      align: 'center',
      render: ({ rating }) => `${rating} 分`,
    },
    {
      title: '创建时间',
      key: 'createTime',
      align: 'center',
      render: ({ createTime }) => formatDate({ date: createTime }),
    },
  ];
}

function renderPlayer(avatar?: string, name?: string, rankNum?: number) {
  const reward = renderReward(rankNum);

  return h('div', {
    class: 'flex justify-center items-center',
  }, [
    h(NAvatar, {
      size: 'small',
      round: true,
      src: avatar ?? defaultAvatar,
    }),
    h(NEllipsis, {
      maxWidth: '200px',
      class: 'ml-4',
    }, () => name),
    reward,
  ]);
}

function renderReward(rankNum?: number) {
  if (!rankNum || !TOP_RANKS.includes(rankNum))
    return null;

  const color = rankNum <= 3 ? MEDAL_COLORS[rankNum - 1] : MEDAL_COLORS[3];
  const component = rankNum <= 3 ? Trophy : Medal;

  return h(NIcon, {
    component,
    color,
    size: 16,
    class: 'ml-4',
  });
}