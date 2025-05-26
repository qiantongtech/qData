package tech.qiantong.qdata.common.database.constants.fieldtypes;

/**
 * 人大金仓（Kingbase8）数据库支持的字段类型枚举
 */
public enum Kingbase8FieldType {
    VARCHAR("VARCHAR"),
    // 根据Kingbase8实际支持情况，可决定是否包含VARCHAR2
    VARCHAR2("VARCHAR2"),
    CHAR("CHAR"),
    NUMBER("NUMBER"),
    DATE("DATE"),
    TIMESTAMP("TIMESTAMP"),
    CLOB("CLOB");

    private final String type;

    Kingbase8FieldType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
