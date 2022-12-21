import type { AnyObject, PageData, PageQuery, Record } from '~/types'

const { get, delete: del } = useRequest

export const RecordApi = {
  /**
   * 查询对局列表
   */
  getRecordList(query: PageQuery & { name?: string }) {
    return get<PageData<Record>>('/api/record/list', { urlParams: query as AnyObject })
  },

  /**
   * 删除对局
   */
  deleteRecord(id: number) {
    return del('/api/record/delete', { urlParams: { id } })
  },
}
