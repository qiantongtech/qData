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
                    throw new ServiceException("sys.error.param.type.mismatch",
                            "Parameter value [{0}] is not of type {1}", obj, String.getVal());
                }
            case Float:
                try {
                    return new BigDecimal(obj.toString()).doubleValue();
                } catch (Exception e) {
                    throw new ServiceException("sys.error.param.type.mismatch",
                            "Parameter value [{0}] is not of type {1}", obj, Float.getVal());
                }
            case Integer:
                try {
                    if(obj != null){
                        return new Integer(obj.toString());
                    }
                } catch (Exception e) {
                    throw new ServiceException("sys.error.param.type.mismatch",
                            "Parameter value [{0}] is not of type {1}", obj, Integer.getVal());
                }
            case List:
                try {
                    return (java.util.List<?>)obj;
                } catch (Exception e) {
                    throw new ServiceException("sys.error.param.type.mismatch",
                            "Parameter value [{0}] is not of type {1}", obj, List.getVal());
                }
            case Date:
                try {
                    return DateUtil.parse(obj.toString(), "yyyy-MM-dd HH:mm:ss");
                } catch (Exception e) {
                    try {
                        return DateUtil.parse(obj.toString(), "yyyy-MM-dd");
                    } catch (Exception ex) {
                        throw new ServiceException("sys.error.param.type.mismatch",
                                "Parameter value [{0}] is not of type {1}", obj, Date.getVal());
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
