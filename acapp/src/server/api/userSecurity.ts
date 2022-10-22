import type { Result, UserSecurity } from '~/types'

export class UserSecurityApi {
  /**
   * 判断用户是否设置密码
   */
  static checkSecurity() {
    return useRequest.get('/api/user/security/check', {}) as unknown as Promise<Result<UserSecurity>>
  }

  /**
   * 修改用户密码
   */
  static updatePassword(data: {
    oldPass: string
    newPass: string
    reenteredNewPass: string
  }) {
    return useRequest.post('/api/user/security/update/password', { body: data }) as unknown as Promise<Result<null>>
  }
}
