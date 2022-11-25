import type { AnyObject, Discuss, PageQuery, PageResult, Result } from '~/types'

const { get, post } = useRequest
type ListPageRes = Promise<PageResult<Discuss>>
type ListRes = Promise<Result<number[]>>
type VoidRes = Promise<Result<null>>

export const DiscussApi = {
  addDiscuss(discuss: Discuss) {
    return post('/api/discuss/remark', { body: discuss as AnyObject }) as unknown as VoidRes
  },

  getDiscussList(query: PageQuery) {
    return get('/api/discuss/remark/list', { urlParams: query as AnyObject }) as unknown as ListPageRes
  },

  getCurrentUserLikes() {
    return get('/api/discuss/remark/auth/likes', {}) as unknown as ListRes
  },

  likeDiscuss(query: { remarkId?: number }) {
    return get('/api/discuss/remark/like', { urlParams: query }) as unknown as VoidRes
  },

  dislikeDiscuss(query: { remarkId?: number }) {
    return get('/api/discuss/remark/dislike', { urlParams: query }) as unknown as VoidRes
  },
}
