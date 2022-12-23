import type { UserSecurity } from '~/types'

export const UserSecurityApi = {
  checkSecurity: () => R.get<UserSecurity>('/api/user/security/check'),
  updatePassword: (body: {
    oldPass: string
    newPass: string
    reenteredNewPass: string
  }) => R.post('/api/user/security/update/password', { body }),
}
