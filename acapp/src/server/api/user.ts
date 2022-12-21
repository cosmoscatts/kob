import type { AcCode, AnyObject, User } from '~/types'

export class UserApi {
  /**
   * 获取 `token`
   */
  static getToken(params?: { username: string; password: string }) {
    return useRequest.post<{ token: string }>('/api/user/account/token', { body: params })
  }

  /**
   * 获取登录用户的信息
   */
  static getLoginUserInfo() {
    return useRequest.get<User>('/api/user/account/info', {})
  }

  /**
   * 修改登录用户的信息
   */
  static updateLoginUserInfo(user: User) {
    return useRequest.put('/api/user/account/info', { body: user as AnyObject })
  }

  /**
   * 申请 `AcWing` `code`
   */
  static applyAcCode() {
    return useRequest.get<AcCode>('/api/user/account/acwing/acapp/apply_code/', {})
  }
}
