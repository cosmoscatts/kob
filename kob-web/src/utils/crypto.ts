import CryptoJS from 'crypto-js'

const KEY = 'mt'
const isString = (p: any): p is string => typeof p === 'string'

export const cryptoEncode = (data: string): string => {
  if (!isString(data)) return ''
  const encryptedData = CryptoJS.AES.encrypt(data, KEY, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7,
  }).toString()
  return encryptedData
}

export const cryptoDecode = (data: string): string => {
  if (!isString(data)) return ''
  const decryptedData = CryptoJS.AES.decrypt(data, KEY, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7,
  })
  return decryptedData.toString(CryptoJS.enc.Utf8)
}
