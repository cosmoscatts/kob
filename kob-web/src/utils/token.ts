import { cryptoDecode, cryptoEncode } from './crypto';

export const TOKEN_KEY = 'jwt_token';
export const Token = {
  get: () => {
    const token = localStorage.getItem(TOKEN_KEY);
    return token
      ? cryptoDecode(token)
      : null;
  },
  set: (token: string) => localStorage.setItem(TOKEN_KEY, cryptoEncode(token)),
  remove: () => localStorage.removeItem(TOKEN_KEY),
};
