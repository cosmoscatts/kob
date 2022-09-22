import type { Result, User } from '~/types'

export class UserApi {
  /**
   * 获取 `token`
   */
  static getToken(params?: { username: string; password: string }) {
    return useRequest.post('/user/account/token', { body: params }) as unknown as Promise<Result<{ token: string }>>
  }

  /**
   * 用户注册
   */
  static register(params?: { username: string; password: string; reenteredPassword: string }) {
    return useRequest.post('/user/account/register', { body: params }) as unknown as Promise<Result<null>>
  }

  /**
   * 获取登录用户的信息
   */
  static getLoginUserInfo() {
    return useRequest.get('/user/account/info', {}) as unknown as Promise<Result<User>>
  }
}
