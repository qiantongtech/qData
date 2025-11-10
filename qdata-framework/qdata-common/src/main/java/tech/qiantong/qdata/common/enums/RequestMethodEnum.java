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

package tech.qiantong.qdata.common.enums;

public enum RequestMethodEnum {

    GET("1", "get"),
    POST("2", "post");

    private final String key;

    private final String val;

    //根据value获取key

    RequestMethodEnum(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public String getVal() {
        return val;
    }

    /**
     * 根据给定的key查找对应的val。
     * 如果找不到与给定key匹配的枚举实例，则返回null。
     */
    public static String getValByKey(String key) {
        for (RequestMethodEnum method : RequestMethodEnum.values()) {
            if (method.getKey().equals(key)) {
                return method.getVal();
            }
        }
        // 如果没有找到匹配的键，则返回null
        return null;
    }

    //根据key，找value
    public static String getKeyByVal(String val) {
        for (RequestMethodEnum method : RequestMethodEnum.values()) {
            if (method.getVal().equals(val)) {
                return method.getKey();
            }
        }
        // 如果没有找到匹配的键，则返回null
        return null;
    }
}
