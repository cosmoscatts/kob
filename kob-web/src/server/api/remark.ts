import type { AnyObject, Discuss, PageData, PageQuery } from '~/types'

const { get, post } = useRequest

export const DiscussApi = {
  addDiscuss(discuss: Discuss) {
    return post('/api/discuss/remark', { body: discuss as AnyObject })
  },

  getDiscussList(query: PageQuery) {
    return get<PageData<Discuss>>('/api/discuss/remark/list', { urlParams: query as AnyObject })
  },

  getCurrentUserLikes() {
    return get<number[]>('/api/discuss/remark/auth/likes', {})
  },

  likeDiscuss(query: { remarkId?: number }) {
    return get('/api/discuss/remark/like', { urlParams: query })
  },

  dislikeDiscuss(query: { remarkId?: number }) {
    return get('/api/discuss/remark/dislike', { urlParams: query })
  },
}
