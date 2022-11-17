import type { AnyObject, Discuss, PageQuery, PageResult, Result } from '~/types'

export class DiscussApi {
  static addDiscuss(discuss: Discuss) {
    return useRequest.post('/api/discuss/remark', { body: discuss as AnyObject }) as unknown as Promise<Result<null>>
  }

  static getDiscussList(query: PageQuery) {
    return useRequest.get('/api/discuss/remark/list', { urlParams: query as AnyObject }) as unknown as Promise<PageResult<Discuss>>
  }

  static getCurrentUserLikes() {
    return useRequest.get('/api/discuss/remark/auth/likes', {}) as unknown as Promise<Result<number[]>>
  }

  static likeDiscuss(query: { remarkId?: number }) {
    return useRequest.get('/api/discuss/remark/like', { urlParams: query }) as unknown as Promise<Result<null>>
  }

  static dislikeDiscuss(query: { remarkId?: number }) {
    return useRequest.get('/api/discuss/remark/dislike', { urlParams: query }) as unknown as Promise<Result<null>>
  }
}
