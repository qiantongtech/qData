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
