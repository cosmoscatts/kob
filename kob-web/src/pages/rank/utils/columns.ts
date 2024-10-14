import type { DataTableColumns } from 'naive-ui';
import { Medal, Trophy } from '@vicons/ionicons5';
import { NAvatar, NEllipsis, NIcon } from 'naive-ui';
import defaultAvatar from '~/assets/default-avatar.png';
import type { Rank } from '~/types';

const TOP_RANKS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

const MEDAL_COLORS = computed(() => [
  isDark.value ? '#FFD700' : '#DAA520', // 金色
  isDark.value ? '#E6E8FA' : '#A9A9A9', // 银色
  isDark.value ? '#FFA500' : '#CD7F32', // 铜色
  isDark.value ? '#3491FA' : '#1E90FF', // 其他排名的颜色
]);

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
      render: ({ rating, rankNum }) => renderRating(rating || 0, rankNum),
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
  const color = rankNum && rankNum <= 3 ? MEDAL_COLORS.value[rankNum - 1] : undefined;

  return h('div', {
    style: {
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
    },
  }, [
    h(NAvatar, {
      size: 'small',
      round: true,
      src: avatar ?? defaultAvatar,
    }),
    h(NEllipsis, {
      maxWidth: '200px',
      style: {
        marginLeft: '15px',
        color,
        fontWeight: rankNum && rankNum <= 3 ? '800' : 'normal',
      },
    }, () => name),
    reward,
  ]);
}

function renderRating(rating: number, rankNum?: number) {
  const color = rankNum && rankNum <= 3 ? MEDAL_COLORS.value[rankNum - 1] : undefined;

  return h('span', {
    style: {
      color,
      fontWeight: rankNum && rankNum <= 3 ? '800' : 'normal',
    },
  }, `${rating} 分`);
}

function renderReward(rankNum?: number) {
  if (!rankNum || !TOP_RANKS.includes(rankNum))
    return null;

  const color = rankNum <= 3 ? MEDAL_COLORS.value[rankNum - 1] : MEDAL_COLORS.value[3];
  const component = rankNum <= 3 ? Trophy : Medal;

  return h(NIcon, {
    component,
    color,
    size: 16,
    style: { marginLeft: '15px' },
  });
}
