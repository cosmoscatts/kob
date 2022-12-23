import type { User } from '~/types'

export const UserApi = {
  /**
   * 获取 token
   */
  getToken(body?: { username: string; password: string }) {
    return R.post<{ token: string }>('/api/user/account/token', { body })
  },

  /**
   * 用户注册
   */
  register(body?: {
    username: string
    password: string
    reenteredPassword: string
  }) {
    return R.post('/api/user/account/register', { body })
  },

  /**
   * 获取登录用户的信息
   */
  getLoginUserInfo() {
    return R.get<User>('/api/user/account/info')
  },

  /**
   * 修改登录用户的信息
   */
  updateLoginUserInfo(body: User) {
    return R.put('/api/user/account/info', { body })
  },
}
