import { decrypt, encrypt } from './encryption';

const TOKEN_STORAGE_KEY = 'auth_token';

export interface TokenStorage {
  get: () => string | null
  set: (token: string) => void
  remove: () => void
}

export const tokenStorage: TokenStorage = {
  get(): string | null {
    const encryptedToken = localStorage.getItem(TOKEN_STORAGE_KEY);
    return encryptedToken ? decrypt(encryptedToken) : null;
  },

  set(token: string): void {
    const encryptedToken = encrypt(token);
    localStorage.setItem(TOKEN_STORAGE_KEY, encryptedToken);
  },

  remove(): void {
    localStorage.removeItem(TOKEN_STORAGE_KEY);
  },
};
