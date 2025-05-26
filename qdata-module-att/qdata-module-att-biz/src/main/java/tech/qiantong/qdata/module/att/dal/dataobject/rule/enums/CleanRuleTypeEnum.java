package tech.qiantong.qdata.module.att.dal.dataobject.rule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 清洗规则类型枚举
 */
@Getter
@AllArgsConstructor
public enum CleanRuleTypeEnum {

    STRING_TRANSFORM("1", "字符串转化"),
    NUMBER_PROCESS("2", "数值处理"),
    TIME_PROCESS("3", "时间处理"),
    DUPLICATE_PROCESS("4", "重复值处理"),
    NULL_PROCESS("5", "空值处理");

    private final String type;
    private final String name;

    public static String getNameByType(String type) {
        for (CleanRuleTypeEnum value : CleanRuleTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value.getName();
            }
        }
        return type;
    }
}
