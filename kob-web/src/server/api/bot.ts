import type { Bot, PageData, PageQuery } from '~/types'

export const BotApi = {
  /**
   * 查询 bot 列表
   */
  getBotList(params: PageQuery) {
    return R.get<PageData<Bot>>('/api/user/bot/list', { params })
  },

  /**
   * 添加 bot
   */
  addBot(body: Bot) {
    return R.post('/api/user/bot/add', { body })
  },

  /**
   * 更新 bot
   */
  updateBot(body: Bot) {
    return R.put('/api/user/bot/update', { body })
  },

  /**
   * 删除 bot
   */
  deleteBot(id: number) {
    return R.delete('/api/user/bot/delete', { params: { id } })
  },
}
