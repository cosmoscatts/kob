import type { AnyObject, PageData, PageQuery, Record } from '~/types'

export class RecordApi {
  /**
   * 查询对局列表
   */
  static getRecordList(query: PageQuery & { name?: string }) {
    return useRequest.get<PageData<Record>>('/api/record/list', { urlParams: query as AnyObject })
  }

  /**
   * 删除对局
   */
  static deleteRecord(id: number) {
    return useRequest.delete('/api/record/delete', { urlParams: { id } })
  }
}
