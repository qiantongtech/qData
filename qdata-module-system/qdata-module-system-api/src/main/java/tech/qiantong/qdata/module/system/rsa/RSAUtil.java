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

package tech.qiantong.qdata.module.system.rsa;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import tech.qiantong.qdata.common.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.stream.Collectors;

public class RSAUtil {

    private static String loadPrivateKey(String fileName) throws IOException {
        InputStream inputStream = RSAUtil.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IOException("Private key file not found in classpath: " + fileName);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Decrypt data using private key
     * @param encryptedData data encrypted with public key
     * @return plaintext
     */
    public static String decryptData(String encryptedData) {
        try {
            // Read private key file from classpath
            String privateKey = loadPrivateKey("private_key.pem");
            privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "");
            privateKey = privateKey.replace("-----END PRIVATE KEY-----", "");
            // Create RSA object using private key
            RSA rsa = new RSA(privateKey, null);

            // Decrypt data
            byte[] decryptedBytes = rsa.decrypt(encryptedData, KeyType.PrivateKey);

            // Return decrypted string
            return new String(decryptedBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Encrypt data using private key
     * @param data plaintext data to encrypt
     * @return encrypted data
     */
    public static String encryptData(String data) {
        try {
            if (data == null) {
                return "";
            }
            // Read private key file from classpath
            String privateKey = loadPrivateKey("private_key.pem");
            privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "");
            privateKey = privateKey.replace("-----END PRIVATE KEY-----", "");

            // Create RSA object using private key
            RSA rsa = new RSA(privateKey, null);

            // Encrypt data
            byte[] encryptedBytes = rsa.encrypt(data.getBytes(), KeyType.PrivateKey);

            // Return encrypted string (usually Base64 encoded for readability)
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Decrypt data using public key
     * @param encryptedData data encrypted with private key
     * @return decrypted plaintext
     */
    public static String decryptWithPublicKey(String encryptedData) {
        try {
            if(StringUtils.isBlank(encryptedData)) return null;
            // Read public key file from classpath
            String publicKey = loadPrivateKey("public_key.pem");
            publicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----", "");
            publicKey = publicKey.replace("-----END PUBLIC KEY-----", "");

            // Create RSA object using public key
            RSA rsa = new RSA(null, publicKey);

            // Decode encrypted data using Base64
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

            // Decrypt data
            byte[] decryptedBytes = rsa.decrypt(encryptedBytes, KeyType.PublicKey);

            // Return decrypted string
            return new String(decryptedBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Decrypt data encrypted with public key
     */
//    public static void main(String[] args) {
//        // Replace with your encrypted data
//        String encryptedData = "U/ANHv0/jZLQIKGRq/4syageiHcd93x9mUjaAyBeyWNf4GvVGizZwi1D7VQWHyDO4nbPJCu/bvyJ7ppT0cb4SpxHZN6KpTBW4bLQAF6fdxOAmPFxRS4xBilrawRm9fVJVW91h7mC4gF0V4KKteUoLe2egJisAnrZ6yVYg4uxLP0=";
//
//        // Call decryption method
//        String decryptedData = decryptData(encryptedData);
//
//        // Output decrypted data
//        System.out.println("Decrypted Data: " + decryptedData);
//    }

    /**
     * Main method, demonstrating private key encryption and public key decryption
     * Implements data encryption and decryption using RSA algorithm
     * @param args arguments
     */
    public static void main(String[] args) {
        // Original data, plaintext to be encrypted
        String data = "This is test data";

        // Encrypt data using private key
        String encryptedData = encryptData(data);
        System.out.println("Encrypted data: " + encryptedData);

        // Decrypt data using public key [public key will be provided to third parties]
        String decryptedData = decryptWithPublicKey(encryptedData);
        System.out.println("Decrypted data: " + decryptedData);
    }

}
