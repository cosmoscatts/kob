import type { AnyObject, PageQuery, PageResult, Record, Result } from '~/types'

const { get, delete: del } = useRequest
type ListPageRes = Promise<PageResult<Record>>
type VoidRes = Promise<Result<null>>

export const RecordApi = {
  /**
   * 查询对局列表
   */
  getRecordList(query: PageQuery & { name?: string }) {
    return get('/api/record/list', { urlParams: query as AnyObject }) as unknown as ListPageRes
  },

  /**
   * 删除对局
   */
  deleteRecord(id: number) {
    return del('/api/record/delete', { urlParams: { id } }) as unknown as VoidRes
  },
}
