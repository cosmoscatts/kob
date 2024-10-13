import type { PageDataResponse, PageQuery, Record } from '~/types';
import api from '~/utils/axios';

export const RankApi = {
  getRankList: (params: PageQuery & { name?: string }) =>
    api.get<PageDataResponse<Record>>('/api/rank/list', { params }),
};
