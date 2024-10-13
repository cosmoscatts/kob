import type { App } from 'vue';

export * from './api';
export * from './models/base';
export * from './models/bot';
export * from './models/change-log';
export * from './models/discuss';
export * from './models/game';
export * from './models/menu';
export * from './models/rank';
export * from './models/record';
export * from './models/user';

export type UserModule = (app: App) => void;
export type AppContext = App;

export type LoginState =
  | 'authenticated' // 用户已登录且认证有效
  | 'unauthenticated' // 用户未登录
  | 'tokenExpired' // 用户的认证令牌已过期
  | 'authNotRequired'; // 当前操作不需要认证
