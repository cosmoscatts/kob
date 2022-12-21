import type { AnyObject, Bot, PageData, PageQuery } from '~/types'

export class BotApi {
  /**
   * 查询 `bot` 列表
   */
  static getBotList(query: PageQuery) {
    return useRequest.get<PageData<Bot>>('/api/user/bot/list', { urlParams: query as AnyObject })
  }

  /**
   * 添加 `bot`
   */
  static addBot(bot: Bot) {
    return useRequest.post('/api/user/bot/add', { body: bot as AnyObject })
  }

  /**
   * 更新 `bot`
   */
  static updateBot(bot: Bot) {
    return useRequest.put('/api/user/bot/update', { body: bot as AnyObject })
  }

  /**
   * 删除 `bot`
   */
  static deleteBot(id: number) {
    return useRequest.delete('/api/user/bot/delete', { urlParams: { id } })
  }
}
