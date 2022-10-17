import type { AnyObject, PageQuery, PageResult, Record, Result } from '~/types'

export class RecordApi {
  /**
   * 查询对局列表
   */
  static getRecordList(query: PageQuery) {
    return useRequest.get('/record/list', { urlParams: query as AnyObject }) as unknown as Promise<PageResult<Record>>
  }

  /**
   * 删除对局
   */
  static deleteRecord(id: number) {
    return useRequest.delete('/record/delete', { urlParams: { id } }) as unknown as Promise<Result<null>>
  }
}
