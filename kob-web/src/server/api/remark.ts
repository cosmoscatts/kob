import type { Discuss, PageData, PageQuery } from '~/types'

export const DiscussApi = {
  addDiscuss(body: Discuss) {
    return R.post('/api/discuss/remark', { body })
  },

  getDiscussList(params: PageQuery) {
    return R.get<PageData<Discuss>>('/api/discuss/remark/list', { params })
  },

  getCurrentUserLikes() {
    return R.get<number[]>('/api/discuss/remark/auth/likes')
  },

  likeDiscuss(params: { remarkId?: number }) {
    return R.get('/api/discuss/remark/like', { params })
  },

  dislikeDiscuss(params: { remarkId?: number }) {
    return R.get('/api/discuss/remark/dislike', { params })
  },
}
