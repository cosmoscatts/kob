import type { Bot, PageData, PageQuery } from '~/types'

export const BotApi = {
  getBotList: (params: PageQuery) => R.get<PageData<Bot>>('/api/user/bot/list', { params }),
  addBot: (body: Bot) => R.post('/api/user/bot/add', { body }),
  updateBot: (body: Bot) => R.put('/api/user/bot/update', { body }),
  deleteBot: (id: number) => R.delete('/api/user/bot/delete', { params: { id } }),
}
