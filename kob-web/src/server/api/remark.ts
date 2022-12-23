import type { Discuss, PageData, PageQuery } from '~/types'

export const DiscussApi = {
  addDiscuss: (body: Discuss) => R.post('/api/discuss/remark', { body }),
  getDiscussList: (params: PageQuery) =>
    R.get<PageData<Discuss>>('/api/discuss/remark/list', { params }),
  getCurrentUserLikes: () => R.get<number[]>('/api/discuss/remark/auth/likes'),
  likeDiscuss: (params: { remarkId?: number }) =>
    R.get('/api/discuss/remark/like', { params }),
  dislikeDiscuss: (params: { remarkId?: number }) =>
    R.get('/api/discuss/remark/dislike', { params }),
}
