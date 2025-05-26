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
     * 使用私钥解密数据
     * @param encryptedData 用公钥加密过后的数据
     * @return 明文
     */
    public static String decryptData(String encryptedData) {
        try {
            // 从classpath读取私钥文件
            String privateKey = loadPrivateKey("private_key.pem");
            privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "");
            privateKey = privateKey.replace("-----END PRIVATE KEY-----", "");
            // 创建RSA对象，使用私钥
            RSA rsa = new RSA(privateKey, null);

            // 解密数据
            byte[] decryptedBytes = rsa.decrypt(encryptedData, KeyType.PrivateKey);

            // 返回解密后的字符串
            return new String(decryptedBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用私钥加密数据
     * @param data 要加密的明文数据
     * @return 加密后的数据
     */
    public static String encryptData(String data) {
        try {
            if (data == null) {
                return "";
            }
            // 从classpath读取私钥文件
            String privateKey = loadPrivateKey("private_key.pem");
            privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "");
            privateKey = privateKey.replace("-----END PRIVATE KEY-----", "");

            // 创建RSA对象，使用私钥
            RSA rsa = new RSA(privateKey, null);

            // 加密数据
            byte[] encryptedBytes = rsa.encrypt(data.getBytes(), KeyType.PrivateKey);

            // 返回加密后的字符串（通常使用Base64编码以便可读性）
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用公钥解密数据
     * @param encryptedData 使用私钥加密过后的数据
     * @return 解密后的明文
     */
    public static String decryptWithPublicKey(String encryptedData) {
        try {
            if(StringUtils.isBlank(encryptedData)) return null;
            // 从classpath读取公钥文件
            String publicKey = loadPrivateKey("public_key.pem");
            publicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----", "");
            publicKey = publicKey.replace("-----END PUBLIC KEY-----", "");

            // 创建RSA对象，使用公钥
            RSA rsa = new RSA(null, publicKey);

            // 使用Base64解码加密数据
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

            // 解密数据
            byte[] decryptedBytes = rsa.decrypt(encryptedBytes, KeyType.PublicKey);

            // 返回解密后的字符串
            return new String(decryptedBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 解密公钥加密后的数据
     */
//    public static void main(String[] args) {
//        // 替换为您的加密数据
//        String encryptedData = "U/ANHv0/jZLQIKGRq/4syageiHcd93x9mUjaAyBeyWNf4GvVGizZwi1D7VQWHyDO4nbPJCu/bvyJ7ppT0cb4SpxHZN6KpTBW4bLQAF6fdxOAmPFxRS4xBilrawRm9fVJVW91h7mC4gF0V4KKteUoLe2egJisAnrZ6yVYg4uxLP0=";
//
//        // 调用解密方法
//        String decryptedData = decryptData(encryptedData);
//
//        // 输出解密后的数据
//        System.out.println("Decrypted Data: " + decryptedData);
//    }

    /**
     * 主方法，演示使用私钥加密和公钥解密数据
     * 通过 RSA 算法实现数据的加密和解密
     * @param args 参数
     */
    public static void main(String[] args) {
        // 原始数据，待加密的明文
        String data = "这是一个测试数据";

        // 使用私钥加密数据
        String encryptedData = encryptData(data);
        System.out.println("加密后的数据: " + encryptedData);

        // 使用公钥解密数据 【公钥会给到第三方】
        String decryptedData = decryptWithPublicKey(encryptedData);
        System.out.println("解密后的数据: " + decryptedData);
    }

}
