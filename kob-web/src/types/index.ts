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

export type LoginState = 'hasLogin' | 'notLogin' | 'expire' | 'noNeedLogin';
