import type { PageData, PageQuery, Record } from '~/types';

export const RecordApi = {
  getRecordList: (params: PageQuery & { name?: string }) =>
    R.get<PageData<Record>>('/api/record/list', { params }),
  deleteRecord: (id: number) =>
    R.delete('/api/record/delete', { params: { id } }),
};
