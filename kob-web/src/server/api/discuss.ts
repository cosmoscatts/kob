import type { ApiResponse, ApiResponseWithoutData, Discuss, PageDataResponse, PageQuery } from '~/types';
import api from '~/utils/axios';

export const DiscussApi = {
  addDiscuss: (body: Discuss) =>
    api.post<ApiResponseWithoutData>('/api/discuss/remark', body),

  getDiscussList: (params: PageQuery) =>
    api.get<PageDataResponse<Discuss>>('/api/discuss/remark/list', { params }),

  getCurrentUserLikes: () =>
    api.get<ApiResponse<number[]>>('/api/discuss/remark/auth/likes'),

  likeDiscuss: (params: { remarkId?: number }) =>
    api.get<ApiResponseWithoutData>('/api/discuss/remark/like', { params }),

  dislikeDiscuss: (params: { remarkId?: number }) =>
    api.get<ApiResponseWithoutData>('/api/discuss/remark/dislike', { params }),
};
