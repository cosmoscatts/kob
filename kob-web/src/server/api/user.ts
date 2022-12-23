import type { User } from '~/types'

export const UserApi = {
  getToken: (body?: { username: string; password: string }) =>
    R.post<{ token: string }>('/api/user/account/token', { body }),
  register: (body?: {
    username: string
    password: string
    reenteredPassword: string
  }) => R.post('/api/user/account/register', { body }),
  getLoginUserInfo: () => R.get<User>('/api/user/account/info'),
  updateLoginUserInfo: (body: User) => R.put('/api/user/account/info', { body }),
}
