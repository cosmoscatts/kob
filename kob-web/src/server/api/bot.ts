import type { AnyObject, Bot, PageData, PageQuery } from '~/types'

const { get, post, put, delete: del } = useRequest

export const BotApi = {
  /**
   * 查询 `bot` 列表
   */
  getBotList(query: PageQuery) {
    return get<PageData<Bot>>('/api/user/bot/list', { urlParams: query as AnyObject })
  },

  /**
   * 添加 `bot`
   */
  addBot(bot: Bot) {
    return post('/api/user/bot/add', { body: bot as AnyObject })
  },

  /**
   * 更新 `bot`
   */
  updateBot(bot: Bot) {
    return put('/api/user/bot/update', { body: bot as AnyObject })
  },

  /**
   * 删除 `bot`
   */
  deleteBot(id: number) {
    return del('/api/user/bot/delete', { urlParams: { id } })
  },
}
