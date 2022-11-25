import type { AnyObject, PageQuery, PageResult, Record } from '~/types'

const { get } = useRequest
type ListPageRes = Promise<PageResult<Record>>

export const RankApi = {
  /**
   * 查询排行榜列表
   */
  getRankList(query: PageQuery & { name?: string }) {
    return get('/api/rank/list', { urlParams: query as AnyObject }) as unknown as ListPageRes
  },
}
