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
 * For brand customization, please apply for brand customization authorization via official channels.
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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.common.utils;

public class ConversionUtils {

    /**
     * 将字符串转换为 Long 类型。如果字符串为空或无法转换，则返回 0L。
     *
     * @param dataLength 要转换的字符串
     * @return 转换后的 Long 类型值
     */
    public static Long getStringToLong(String dataLength) {
        if (StringUtils.isEmpty(dataLength)) {
            return 0L;
        }
        try {
            return Long.parseLong(dataLength);
        } catch (NumberFormatException e) {
            // 如果转换失败，则返回 0L
            return 0L;
        }
    }

    /**
     * 将字符串转换为 Integer 类型。如果字符串为空或无法转换，则返回 0。
     *
     * @param dataLength 要转换的字符串
     * @return 转换后的 Integer 类型值
     */
    public static Integer getStringToInt(String dataLength) {
        if (StringUtils.isEmpty(dataLength)) {
            return 0;
        }
        try {
            return Integer.parseInt(dataLength);
        } catch (NumberFormatException e) {
            // 如果转换失败，则返回 0
            return 0;
        }
    }

    /**
     * 将字符串转换为 Double 类型。如果字符串为空或无法转换，则返回 0.0。
     *
     * @param dataLength 要转换的字符串
     * @return 转换后的 Double 类型值
     */
    public static Double getStringToDouble(String dataLength) {
        if (StringUtils.isEmpty(dataLength)) {
            return 0.0;
        }
        try {
            return Double.parseDouble(dataLength);
        } catch (NumberFormatException e) {
            // 如果转换失败，则返回 0.0
            return 0.0;
        }
    }

    /**
     * 将字符串转换为 Boolean 类型。如果字符串为空或无法转换，则返回 false。
     *
     * @param value 要转换的字符串
     * @return 转换后的 Boolean 类型值
     */
    public static Boolean getStringToBoolean(String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }


}
