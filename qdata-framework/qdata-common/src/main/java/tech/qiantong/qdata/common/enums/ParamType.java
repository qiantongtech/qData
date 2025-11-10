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


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import tech.qiantong.qdata.common.exception.ServiceException;

import java.math.BigDecimal;

public enum ParamType {

    String("1", "字符串"),
    Integer("2", "整型"),
    Float("3", "浮点型"),
    Date("4", "时间"),
    List("5", "集合");

    private final String key;

    private final String val;

    ParamType(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public String getVal() {
        return val;
    }

    public static Object parse(ParamType paramType, Object obj) {
        if (ObjectUtil.isEmpty(obj)) {
            return null;
        }
        switch (paramType) {
            case String:
                try {
                    return (java.lang.String)obj;
                } catch (Exception e) {
                    throw new ServiceException("参数值[" + obj + "]不是" + String.getVal() + "数据类型]");
                }
            case Float:
                try {
                    return new BigDecimal(obj.toString()).doubleValue();
                } catch (Exception e) {
                    throw new ServiceException("参数值[" + obj + "]不是" + Float.getVal() + "数据类型]");
                }
            case Integer:
                try {
                    if(obj != null){
                        return new Integer(obj.toString());
                    }
                } catch (Exception e) {
                    throw new ServiceException("参数值[" + obj + "]不是" + Integer.getVal() + "数据类型]");
                }
            case List:
                try {
                    return (java.util.List<?>)obj;
                } catch (Exception e) {
                    throw new ServiceException("参数值[" + obj + "]不是" + List.getVal() + "数据类型]");
                }
            case Date:
                try {
                    return DateUtil.parse(obj.toString(), "yyyy-MM-dd HH:mm:ss");
                } catch (Exception e) {
                    try {
                        return DateUtil.parse(obj.toString(), "yyyy-MM-dd");
                    } catch (Exception ex) {
                        throw new ServiceException("参数值[" + obj + "]不是" + Date.getVal() + "数据类型]");
                    }
                }
        }
        return null;
    }

    public static ParamType getParamType(String paramType) {
        for (ParamType type : ParamType.values()) {
            if (type.key.equals(paramType)) {
                return type;
            }
        }
        return String;
    }
}
