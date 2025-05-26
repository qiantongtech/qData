package tech.qiantong.qdata.pay.utils;

import java.util.Map;

/**
 * 签名验证
 * @author qdata
 */
public class SignatureUtil {

    /**
     * 生成签名字符串。
     * @param parameters 需要签名的参数集合
     * @param secret 签名密钥
     * @return 生成的签名字符串
     */
    public static String generateSignature(Map<String, String> parameters, String secret) {
        // 实现签名生成逻辑
        return "";
    }

    /**
     * 验证签名的有效性。
     * @param parameters 需要验证的参数集合
     * @param signature 传入的签名字符串
     * @param secret 签名密钥
     * @return 如果签名有效返回true，否则返回false
     */
    public static boolean verifySignature(Map<String, String> parameters, String signature, String secret) {
        // 实现签名验证逻辑
        return true;
    }
}
