/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
