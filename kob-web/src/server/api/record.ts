import type { ApiResponseWithoutData, PageDataResponse, PageQuery, Record } from '~/types';
import api from '~/utils/axios';

export const RecordApi = {
  getRecordList: (params: PageQuery & { name?: string }) =>
    api.get<PageDataResponse<Record>>('/api/record/list', { params }),

  deleteRecord: (id: number) =>
    api.delete<ApiResponseWithoutData>('/api/record/delete', { params: { id } }),
};
