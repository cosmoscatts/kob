import type { ApiResponse, ApiResponseWithoutData, UserSecurity } from '~/types';
import api from '~/utils/axios';

export const UserSecurityApi = {
  checkSecurity: () =>
    api.get<ApiResponse<UserSecurity>>('/api/user/security/check'),

  updatePassword: (body: {
    oldPass: string
    newPass: string
    reenteredNewPass: string
  }) => api.post<ApiResponseWithoutData>('/api/user/security/update/password', body),
};
