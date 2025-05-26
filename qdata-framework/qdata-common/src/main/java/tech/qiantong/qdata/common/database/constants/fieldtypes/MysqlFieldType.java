package tech.qiantong.qdata.common.database.constants.fieldtypes;

/**
 * MySQL 数据库支持的字段类型枚举
 */
public enum MysqlFieldType {
    VARCHAR("VARCHAR"),
    CHAR("CHAR"),
    // MySQL 一般采用 INT/DECIMAL 来代替 NUMBER
    INT("INT"),
    DECIMAL("DECIMAL"),
    DATE("DATE"),
    DATETIME("DATETIME"),
    TIMESTAMP("TIMESTAMP"),
    // 使用 TEXT 表示大文本类型
    TEXT("TEXT");

    private final String type;

    MysqlFieldType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
