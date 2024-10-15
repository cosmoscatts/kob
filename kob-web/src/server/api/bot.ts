import type { ApiResponse, ApiResponseWithoutData, Bot, PageDataResponse, PageQuery } from '~/types';
import api from '~/utils/axios';

export const BotApi = {
  getBotList: (params: PageQuery) =>
    api.get<PageDataResponse<Bot>>('/api/user/bot/list', { params }),

  getMachineBots: () =>
    api.get<ApiResponse<Bot[]>>('/api/user/bot/machines'),

  addBot: (body: Bot) =>
    api.post<ApiResponseWithoutData>('/api/user/bot/add', body),

  updateBot: (body: Bot) =>
    api.put<ApiResponseWithoutData>('/api/user/bot/update', body),

  deleteBot: (id: number) =>
    api.delete<ApiResponseWithoutData>('/api/user/bot/delete', { params: { id } }),
};
