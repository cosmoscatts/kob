import type { AnyObject, Bot, PageQuery, PageResult, Result } from '~/types'

export class BotApi {
  /**
   * 查询 `bot` 列表
   */
  static getBotList(query: PageQuery) {
    return useRequest.get('/user/bot/list', { urlParams: query as AnyObject }) as unknown as Promise<PageResult<Bot>>
  }

  /**
   * 添加 `bot`
   */
  static addBot(bot: Bot) {
    return useRequest.post('/user/bot/add', { body: bot as AnyObject }) as unknown as Promise<Result<null>>
  }

  /**
   * 更新 `bot`
   */
  static updateBot(bot: Bot) {
    return useRequest.put('/user/bot/update', { body: bot as AnyObject }) as unknown as Promise<Result<null>>
  }

  /**
   * 删除 `bot`
   */
  static deleteBot(id: number) {
    return useRequest.delete('/user/bot/delete', { urlParams: { id } }) as unknown as Promise<Result<null>>
  }
}
