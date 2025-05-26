package tech.qiantong.qdata.module.att.dal.dataobject.rule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则类型枚举
 */
@Getter
@AllArgsConstructor
public enum RuleTypeEnum {

    CHARACTER_CHECK("1", "字符校验"),
    NUMBER_CHECK("2", "数值校验"),
    NULL_CHECK("3", "空值校验"),
    LENGTH_CHECK("4", "长度校验"),
    DUPLICATE_CHECK("5", "重复检查"),
    FORMAT_CHECK("6", "格式检查");

    private final String type;
    private final String name;

    public static String getNameByType(String type) {
        for (RuleTypeEnum value : RuleTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value.getName();
            }
        }
        return type;
    }
}
