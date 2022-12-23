import type { PageData, PageQuery, Record } from '~/types'

export const RecordApi = {
  /**
   * 查询对局列表
   */
  getRecordList(params: PageQuery & { name?: string }) {
    return R.get<PageData<Record>>('/api/record/list', { params })
  },

  /**
   * 删除对局
   */
  deleteRecord(id: number) {
    return R.delete('/api/record/delete', { params: { id } })
  },
}
