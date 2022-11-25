import type { AnyObject, Bot, PageQuery, PageResult, Result } from '~/types'

const { get, post, put, delete: del } = useRequest
type ListPageRes = Promise<PageResult<Bot>>
type VoidRes = Promise<Result<null>>

export const BotApi = {
  /**
   * 查询 `bot` 列表
   */
  getBotList(query: PageQuery) {
    return get('/api/user/bot/list', { urlParams: query as AnyObject }) as unknown as ListPageRes
  },

  /**
   * 添加 `bot`
   */
  addBot(bot: Bot) {
    return post('/api/user/bot/add', { body: bot as AnyObject }) as unknown as VoidRes
  },

  /**
   * 更新 `bot`
   */
  updateBot(bot: Bot) {
    return put('/api/user/bot/update', { body: bot as AnyObject }) as unknown as VoidRes
  },

  /**
   * 删除 `bot`
   */
  deleteBot(id: number) {
    return del('/api/user/bot/delete', { urlParams: { id } }) as unknown as VoidRes
  },
}
