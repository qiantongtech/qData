import CryptoJS from 'crypto-js'

const aesKey = import.meta.env.VITE_APP_AES_KEY

// 解密
export function  decrypt(data) {
  const key = CryptoJS.enc.Utf8.parse(aesKey);
  const iv = CryptoJS.enc.Utf8.parse(aesKey);
  // 解密数据
  const decryptedBytes = CryptoJS.AES.decrypt({
    ciphertext: CryptoJS.enc.Base64.parse(data)
  }, key, {
    iv: iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.ZeroPadding
  })
  const decryptedData = decryptedBytes.toString(CryptoJS.enc.Utf8)
  return decryptedData
}

// 加密
export function encrypt(data) {
  const key = CryptoJS.enc.Utf8.parse(aesKey);
  const iv = CryptoJS.enc.Utf8.parse(aesKey);
  // 加密数据
  const encryptedBytes = CryptoJS.AES.encrypt(data, key, {
    iv: iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.ZeroPadding
  });
  const encryptedData = encryptedBytes.ciphertext.toString(CryptoJS.enc.Base64);
  return encryptedData;
}

export function isDecrypted(data){
    try {
        decrypt(data)
        return true
    } catch (e) {
        return false
    }
}
