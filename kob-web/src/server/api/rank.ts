import type { PageData, PageQuery, Record } from '~/types';

export const RankApi = {
  getRankList: (params: PageQuery & { name?: string }) =>
    R.get<PageData<Record>>('/api/rank/list', { params }),
};
