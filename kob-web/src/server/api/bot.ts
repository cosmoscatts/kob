import type { AnyObject, Bot, Result } from '~/types'

export class BotApi {
  /**
   * 查询 `bot` 列表
   */
  getBotList() {
    return useRequest.get('/user/bot/list', {}) as unknown as Promise<Result<Bot[]>>
  }

  /**
   * 添加 `bot`
   */
  addBot(bot: Bot) {
    return useRequest.post('/user/bot/add', { body: bot as AnyObject }) as unknown as Promise<Result<null>>
  }

  /**
   * 更新 `bot`
   */
  updateBot(bot: Bot) {
    return useRequest.put('/user/bot/update', { body: bot as AnyObject }) as unknown as Promise<Result<null>>
  }

  /**
   * 删除 `bot`
   */
  deleteBot(id: number) {
    return useRequest.delete('/user/bot/delete', { urlParams: { id } }) as unknown as Promise<Result<null>>
  }
}
