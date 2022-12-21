import type { AnyObject, PageData, PageQuery, Record } from '~/types'

export class RankApi {
  /**
   * 查询排行榜列表
   */
  static getRankList(query: PageQuery & { name?: string }) {
    return useRequest.get<PageData<Record>>('/api/rank/list', { urlParams: query as AnyObject })
  }
}
