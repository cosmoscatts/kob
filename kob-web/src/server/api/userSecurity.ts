import type { UserSecurity } from '~/types'

const { get, post } = useRequest

export const UserSecurityApi = {
  /**
   * 判断用户是否设置密码
   */
  checkSecurity() {
    return get<UserSecurity>('/api/user/security/check', {})
  },

  /**
   * 修改用户密码
   */
  updatePassword(data: {
    oldPass: string
    newPass: string
    reenteredNewPass: string
  }) {
    return post('/api/user/security/update/password', { body: data })
  },
}
