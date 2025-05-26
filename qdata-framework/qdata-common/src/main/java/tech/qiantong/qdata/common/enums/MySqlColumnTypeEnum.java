package tech.qiantong.qdata.common.enums;

import lombok.Getter;
import tech.qiantong.qdata.common.database.utils.MD5Util;

/**
 * MySQL数据库字段类型枚举
 */
@Getter
public enum MySqlColumnTypeEnum {
    TINYINT("TINYINT", DmColumnTypeEnum.TINYINT),
    SMALLINT("SMALLINT", DmColumnTypeEnum.TINYINT),
    MEDIUMINT("MEDIUMINT", DmColumnTypeEnum.INTEGER),
    INT("INT", DmColumnTypeEnum.INTEGER),
    INTEGER("INTEGER", DmColumnTypeEnum.INTEGER),
    BIGINT("BIGINT", DmColumnTypeEnum.BIGINT),
    DECIMAL("DECIMAL", DmColumnTypeEnum.DECIMAL),
    NUMERIC("NUMERIC", DmColumnTypeEnum.NUMERIC),
    FLOAT("FLOAT", DmColumnTypeEnum.FLOAT),
    DOUBLE("DOUBLE", DmColumnTypeEnum.DOUBLE),
    REAL("REAL", DmColumnTypeEnum.DOUBLE),
    CHAR("CHAR", DmColumnTypeEnum.CHAR),
    VARCHAR("VARCHAR", DmColumnTypeEnum.VARCHAR2),
    TINYTEXT("TINYTEXT", DmColumnTypeEnum.TEXT),
    TEXT("TEXT", DmColumnTypeEnum.TEXT),
    MEDIUMTEXT("MEDIUMTEXT", DmColumnTypeEnum.TEXT),
    LONGTEXT("LONGTEXT", DmColumnTypeEnum.TEXT),
    DATE("DATE", DmColumnTypeEnum.DATE),
    TIME("TIME", DmColumnTypeEnum.TIMESTAMP),
    TIMESTAMP("TIMESTAMP", DmColumnTypeEnum.TIMESTAMP),
    DATETIME("DATETIME", DmColumnTypeEnum.DATETIME);

    private final String type;
    private final DmColumnTypeEnum dmType;

    MySqlColumnTypeEnum(String type, DmColumnTypeEnum dmType) {
        this.type = type;
        this.dmType = dmType;
    }

    /**
     * 将MySQL类型转换为达梦类型
     */
    public static String convertToDmType(String type) {
        String mysqlType = MD5Util.convertIfLowercase(type);
        mysqlType = mysqlType.replaceAll("\\(.*\\)", "").trim().toUpperCase();
        for (MySqlColumnTypeEnum typeEnum : values()) {
            if (typeEnum.getType().equals(mysqlType)) {
                return typeEnum.getDmType().getType();
            }
        }
        return mysqlType;
//        return DmColumnTypeEnum.VARCHAR.getType(); // 默认转为VARCHAR
    }
}
