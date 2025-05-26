package tech.qiantong.qdata.common.database.constants.fieldtypes;

/**
 * Oracle 数据库支持的字段类型枚举
 */
public enum OracleFieldType {
    VARCHAR("VARCHAR"),
    VARCHAR2("VARCHAR2"),
    CHAR("CHAR"),
    NUMBER("NUMBER"),
    DATE("DATE"),
    TIMESTAMP("TIMESTAMP"),
    CLOB("CLOB");

    private final String type;

    OracleFieldType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
