import type { AnyObject, Result, User } from '~/types'

const { get, post, put } = useRequest
type TokenRes = Promise<Result<{ token: string }>>
type UserRes = Promise<Result<User>>
type VoidRes = Promise<Result<null>>

export const UserApi = {
  /**
   * 获取 `token`
   */
  getToken(params?: { username: string; password: string }) {
    return post('/api/user/account/token', { body: params }) as unknown as TokenRes
  },

  /**
   * 用户注册
   */
  register(params?: { username: string; password: string; reenteredPassword: string }) {
    return post('/api/user/account/register', { body: params }) as unknown as VoidRes
  },

  /**
   * 获取登录用户的信息
   */
  getLoginUserInfo() {
    return get('/api/user/account/info', {}) as unknown as UserRes
  },

  /**
   * 修改登录用户的信息
   */
  updateLoginUserInfo(user: User) {
    return put('/api/user/account/info', { body: user as AnyObject }) as unknown as VoidRes
  },
}
