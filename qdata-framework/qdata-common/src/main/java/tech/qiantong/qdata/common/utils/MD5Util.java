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

package tech.qiantong.qdata.common.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

public class MD5Util {

    /** Vector (you must have both the vector and the key to decrypt), this vector must be 8 bytes, any number will report an error */
    private final byte[] DESIV = new byte[] { 0x22, 0x54, 0x36, 110, 0x40, (byte) 0xac, (byte) 0xad, (byte) 0xdf };
    /** Custom key, the number cannot be too short. If it is too short, an error will be reported. If it is too long, it will only take the first N digits by default (for the specific value of N, you will find the information separately) */
    private final String deSkey = "cloud123456";
    /** Parameter interface of encryption algorithm */
    private AlgorithmParameterSpec iv = null;
    private Key key = null;
    private String charset = "UTF-8";

    private static volatile MD5Util instance;

    /**
     * Constructor
     * @throws Exception
     */
    private MD5Util() throws Exception {
        // Set key parameters
        DESKeySpec keySpec = new DESKeySpec(deSkey.getBytes(this.charset));
        // Set vector
        iv = new IvParameterSpec(DESIV);
        // Get key factory
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // Get key object
        key = keyFactory.generateSecret(keySpec);
    }

    public static MD5Util getInstance() throws Exception {
        if(instance == null) {
            synchronized (MD5Util.class) {
                if(instance == null) {
                    instance = new MD5Util();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        try {
            String value = "1246656415670484994";
            MD5Util mt = new MD5Util();
            System.out.println("加密前的字符：" + value);
            System.out.println("加密后的字符：" + mt.encode(value));
            System.out.println("解密后的字符：" + mt.decode(mt.encode(value)));
            System.out.println("字符串的MD5值："+ getMD5Value(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Encryption
     * @param data
     * @return
     * @throws Exception
     */
    public String encode(String data) throws Exception {
        // Get the encrypted object Cipher
        Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // Set the working mode to encryption mode, give the key and vector
        enCipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] pasByte = enCipher.doFinal(data.getBytes(this.charset));
        return Base64.getEncoder().encodeToString(pasByte);
    }

    /**
     * Decrypt
     * @param data
     * @return
     * @throws Exception
     */
    public String decode(String data) throws Exception {
        Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        deCipher.init(Cipher.DECRYPT_MODE, key, iv);
        //Note here that the number of digits in the parameter of doFinal() must be a multiple of 8, otherwise an error will be reported (the string encrypted by encoding will be read out as a multiple of 8, but if it is written to the file and then read out, the number of digits in the parameter of doFinal() here may not be a multiple of 8 due to the reading method)
        //Base64Decoder must be used here, if data is used. getBytes(), the number of byte arrays of the string obtained is most likely not a multiple of 8, and does not correspond to the above BASE64Encoder (even if the decryption does not report an error, the correct result will not be obtained)
        byte[] pasByte = deCipher.doFinal(Base64.getDecoder().decode(data));
        return new String(pasByte, this.charset);
    }

    /**
     * Get the MD5 value, which can be used for comparison and verification
     * @param sourceStr
     * @return
     */
    private static String getMD5Value(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return result;
    }
}
