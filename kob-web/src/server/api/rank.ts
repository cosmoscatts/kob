import type { PageData, PageQuery, Record } from '~/types'

export const RankApi = {
  /**
   * 查询排行榜列表
   */
  getRankList(params: PageQuery & { name?: string }) {
    return R.get<PageData<Record>>('/api/rank/list', { params })
  },
}
