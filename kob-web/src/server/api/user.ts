import type { ApiResponse, ApiResponseWithoutData, User } from '~/types';
import api from '~/utils/axios';

export const UserApi = {
  getToken: (body?: { username: string, password: string }) =>
    api.post<ApiResponse<{ token: string }>>('/api/user/account/token', body),

  register: (body?: {
    username: string
    password: string
    reenteredPassword: string
  }) => api.post<ApiResponseWithoutData>('/api/user/account/register', body),

  getLoginUserInfo: () =>
    api.get<ApiResponse<User>>('/api/user/account/info'),

  updateLoginUserInfo: (body: User) =>
    api.put<ApiResponseWithoutData>('/api/user/account/info', body),

  getUserInfoById: (id: string | number) =>
    api.get<ApiResponse<User>>('/api/user/account/infoById', { params: { id } }),
};
