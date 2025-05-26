package tech.qiantong.qdata.common.enums;

import lombok.Getter;

/**
 * 达梦数据库字段类型枚举
 */
@Getter
public enum DmColumnTypeEnum {
    TINYINT("TINYINT"),
    INTEGER("INTEGER"),
    BIGINT("BIGINT"),
    DECIMAL("DECIMAL"),
    NUMERIC("NUMERIC"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    NUMBER("NUMBER"),
    CHAR("CHAR"),
    VARCHAR("VARCHAR"),
    VARCHAR2("VARCHAR2"),
    TEXT("TEXT"),
    DATE("DATE"),
    TIMESTAMP("TIMESTAMP"),
    DATETIME("DATETIME");

    private final String type;

    DmColumnTypeEnum(String type) {
        this.type = type;
    }
}
