import type { Result, UserSecurity } from '~/types'

const { get, post } = useRequest
type UserSecurityRes = Promise<Result<UserSecurity>>
type VoidRes = Promise<Result<null>>

export const UserSecurityApi = {
  /**
   * 判断用户是否设置密码
   */
  checkSecurity() {
    return get('/api/user/security/check', {}) as unknown as UserSecurityRes
  },

  /**
   * 修改用户密码
   */
  updatePassword(data: {
    oldPass: string
    newPass: string
    reenteredNewPass: string
  }) {
    return post('/api/user/security/update/password', { body: data }) as unknown as VoidRes
  },
}
