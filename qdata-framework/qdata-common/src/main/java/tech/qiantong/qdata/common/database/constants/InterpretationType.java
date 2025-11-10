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

package tech.qiantong.qdata.common.database.constants;

/**
 * <P>
 * 用途:判断类型
 * </p>
 *
 * @author: FXB
 * @create: 2024-07-31 15:56
 **/
public enum InterpretationType {

    GE("1", "大于等于", ">="),
    GT("2", "大于", ">"),
    LE("3", "小于等于", "<="),
    LT("4", "小于", "<"),
    EQ("5", "等于", "="),
    NE("6", "不等于", "!="),
    SCOPE("9", "范围", ""),
    OUTSIDE_RANGE("10", "范围外", "");

    /**
     * 数据库名称
     */
    private final String value;

    /**
     * 描述
     */
    private final String desc;

    /**
     * url
     */
    private final String symbol;


    public String getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getSymbol() {
        return this.symbol;
    }

    InterpretationType(String value, String desc, String symbol) {
        this.value = value;
        this.desc = desc;
        this.symbol = symbol;
    }

    /**
     * 获取判断类型
     */
    public static InterpretationType getInterpretationType(String dbType) {
        for (InterpretationType type : InterpretationType.values()) {
            if (type.value.equals(dbType)) {
                return type;
            }
        }
        return null;
    }
}
