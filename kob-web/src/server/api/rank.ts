import type { AnyObject, PageQuery, PageResult, Record } from '~/types'

export class RankApi {
  /**
   * 查询排行榜列表
   */
  static getRankList(query: PageQuery & { name?: string }) {
    return useRequest.get('/api/rank/list', { urlParams: query as AnyObject }) as unknown as Promise<PageResult<Record>>
  }
}
