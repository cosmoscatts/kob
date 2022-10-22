import type { AnyObject, Result, User } from '~/types'

export class UserApi {
  /**
   * 获取 `token`
   */
  static getToken(params?: { username: string; password: string }) {
    return useRequest.post('/api/user/account/token', { body: params }) as unknown as Promise<Result<{ token: string }>>
  }

  /**
   * 用户注册
   */
  static register(params?: { username: string; password: string; reenteredPassword: string }) {
    return useRequest.post('/api/user/account/register', { body: params }) as unknown as Promise<Result<null>>
  }

  /**
   * 获取登录用户的信息
   */
  static getLoginUserInfo() {
    return useRequest.get('/api/user/account/info', {}) as unknown as Promise<Result<User>>
  }

  /**
   * 修改登录用户的信息
   */
  static updateLoginUserInfo(user: User) {
    return useRequest.put('/api/user/account/info', { body: user as AnyObject }) as unknown as Promise<Result<null>>
  }
}
