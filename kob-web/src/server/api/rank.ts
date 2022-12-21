import type { AnyObject, PageData, PageQuery, Record } from '~/types'

const { get } = useRequest

export const RankApi = {
  /**
   * 查询排行榜列表
   */
  getRankList(query: PageQuery & { name?: string }) {
    return get<PageData<Record>>('/api/rank/list', { urlParams: query as AnyObject })
  },
}
