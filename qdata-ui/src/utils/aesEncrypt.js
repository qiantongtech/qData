/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

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
