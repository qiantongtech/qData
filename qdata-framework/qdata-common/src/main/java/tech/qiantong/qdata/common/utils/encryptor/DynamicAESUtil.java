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

package tech.qiantong.qdata.common.utils.encryptor;
import tech.qiantong.qdata.common.exception.ServiceException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class DynamicAESUtil {

    private static final String ALGORITHM = "AES";

    /**
     * Generate random keys
     *
     * @return random key string
     */
    public static String generateRandomKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(128, new SecureRandom());
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new ServiceException("sys.error.encrypt.key", "Failed to generate random key");
        }
    }

    /**
     * AES encryption
     *
     * @param data plain text data
     * @param key key string
     * @return encrypted ciphertext
     */
    public static String encrypt(String data, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new ServiceException("sys.error.encrypt.fail", "Encryption failed");
        }
    }

    /**
     * AES decryption
     *
     * @param encryptedData ciphertext data
     * @param key key string
     * @return decrypted plaintext
     */
    public static String decrypt(String encryptedData, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decrypted);
        } catch (Exception e) {
            throw new ServiceException("sys.error.decrypt.fail", "Decryption failed");
        }
    }

}
