import type { AnyObject, User } from '~/types'

const { get, post, put } = useRequest

export const UserApi = {
  /**
   * 获取 `token`
   */
  getToken(params?: { username: string; password: string }) {
    return post<{ token: string }>('/api/user/account/token', { body: params })
  },

  /**
   * 用户注册
   */
  register(params?: { username: string; password: string; reenteredPassword: string }) {
    return post('/api/user/account/register', { body: params })
  },

  /**
   * 获取登录用户的信息
   */
  getLoginUserInfo() {
    return get<User>('/api/user/account/info', {})
  },

  /**
   * 修改登录用户的信息
   */
  updateLoginUserInfo(user: User) {
    return put('/api/user/account/info', { body: user as AnyObject })
  },
}
