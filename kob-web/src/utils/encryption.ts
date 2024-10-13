import CryptoJS from 'crypto-js';

const ENCRYPTION_KEY = 'cosmoscatts';

const isString = (value: unknown): value is string => typeof value === 'string';

export const encrypt = (data: string): string => {
  if (!isString(data)) {
    throw new Error('Encryption input must be a string');
  }

  return CryptoJS.AES.encrypt(data, ENCRYPTION_KEY, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7,
  }).toString();
};

export const decrypt = (encryptedData: string): string => {
  if (!isString(encryptedData)) {
    throw new Error('Decryption input must be a string');
  }

  const decryptedBytes = CryptoJS.AES.decrypt(encryptedData, ENCRYPTION_KEY, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7,
  });

  return decryptedBytes.toString(CryptoJS.enc.Utf8);
};
