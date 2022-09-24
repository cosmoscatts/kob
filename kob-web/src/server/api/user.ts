/*
 * @Author: faitsse 2138889191@qq.com
 * @Date: 2022-09-22 20:30:08
 * @LastEditors: faitsse 2138889191@qq.com
 * @LastEditTime: 2022-09-24 19:18:40
 * @FilePath: \kob-web\src\server\api\user.ts
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import type { AnyObject, Result, User } from '~/types'

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

  /**
   * 修改登录用户的信息
   */
  static updateLoginUserInfo(user: User) {
    return useRequest.put('/user/account/info', { body: user as AnyObject }) as unknown as Promise<Result<null>>
  }
}
