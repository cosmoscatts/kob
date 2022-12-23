import type { UserSecurity } from '~/types'

export const UserSecurityApi = {
  /**
   * 判断用户是否设置密码
   */
  checkSecurity() {
    return R.get<UserSecurity>('/api/user/security/check')
  },

  /**
   * 修改用户密码
   */
  updatePassword(body: {
    oldPass: string
    newPass: string
    reenteredNewPass: string
  }) {
    return R.post('/api/user/security/update/password', { body })
  },
}
