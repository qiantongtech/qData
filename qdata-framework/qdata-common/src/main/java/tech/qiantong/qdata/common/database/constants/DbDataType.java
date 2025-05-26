package tech.qiantong.qdata.common.database.constants;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum DbDataType {

    MYSQL_DATE(DbType.MYSQL.getDb(), "DATE", "DATE_FORMAT('${data}', '%Y-%m-%d')"),
    MYSQL_DATETIME(DbType.MYSQL.getDb(), "DATETIME", "DATE_FORMAT('${data}', '%Y-%m-%d %H:%i:%s')"),
    MYSQL_TIME(DbType.MYSQL.getDb(), "TIME", "DATE_FORMAT('${data}', '%H:%i:%s')"),
    MYSQL_TIMESTAMP(DbType.MYSQL.getDb(), "TIMESTAMP", "DATE_FORMAT('${data}', '%Y-%m-%d %H:%i:%s')"),

    ORACLE11g_DATE(DbType.ORACLE.getDb(), "DATE", "TO_DATE('${data}', 'YYYY-MM-DD HH24:MI:SS')"),
    ORACLE11g_TIMESTAMP(DbType.ORACLE.getDb(), "TIMESTAMP", "TO_TIMESTAMP('${data}', 'YYYY-MM-DD HH24:MI:SS')"),

    ORACLE12g_DATE(DbType.ORACLE_12C.getDb(), "DATE", "TO_DATE('${data}', 'YYYY-MM-DD HH24:MI:SS')"),
    ORACLE12g_TIMESTAMP(DbType.ORACLE_12C.getDb(), "TIMESTAMP", "TO_TIMESTAMP('${data}',  'YYYY-MM-DD HH24:MI:SS')"),

    DM8_DATE(DbType.DM8.getDb(), "DATE", "TO_DATE('${data}', 'YYYY-MM-DD HH24:MI:SS')"),
    DM8_TIMESTAMP(DbType.DM8.getDb(), "TIMESTAMP", "TO_TIMESTAMP('${data}', 'YYYY-MM-DD HH24:MI:SS')"),

    KINGBASE8_DATE(DbType.KINGBASE8.getDb(), "DATE", "TO_DATE('${data}', 'YYYY-MM-DD HH24:MI:SS')"),
    KINGBASE8_TIMESTAMP(DbType.KINGBASE8.getDb(), "TIMESTAMP", "TO_TIMESTAMP('${data}', 'YYYY-MM-DD HH24:MI:SS')"),


    SQL_SERVER2008_DATE(DbType.SQL_SERVER2008.getDb(), "DATE", "CONVERT(DATE, '${data}', 120)"),
    SQL_SERVER2008_TIME(DbType.SQL_SERVER.getDb(), "TIME", "CONVERT(DATE, '${data}', 108)"),
    SQL_SERVER2008_SMALLDATETIME(DbType.SQL_SERVER2008.getDb(), "SMALLDATETIME", "CONVERT(SMALLDATETIME, '${data}', 120)"),
    SQL_SERVER2008_DATETIME(DbType.SQL_SERVER2008.getDb(), "DATETIME", "CONVERT(DATETIME, '${data}', 120)"),
    SQL_SERVER2008_DATETIME2(DbType.SQL_SERVER2008.getDb(), "DATETIME2", "CONVERT(DATETIME2, '${data}', 120)"),
    SQL_SERVER2008_DATETIMEOFFSET(DbType.SQL_SERVER2008.getDb(), "DATETIMEOFFSET", "CONVERT(DATETIMEOFFSET, '${data}', 120))"),

    SQL_SERVER2012_DATE(DbType.SQL_SERVER.getDb(), "DATE", "CONVERT(DATE, '${data}', 120)"),
    SQL_SERVER2012_TIME(DbType.SQL_SERVER.getDb(), "TIME", "CONVERT(DATE, '${data}', 108)"),
    SQL_SERVER2012_SMALLDATETIME(DbType.SQL_SERVER.getDb(), "SMALLDATETIME", "CONVERT(SMALLDATETIME, '${data}', 120)"),
    SQL_SERVER2012_DATETIME(DbType.SQL_SERVER.getDb(), "DATETIME", "CONVERT(DATETIME, '${data}', 120)"),
    SQL_SERVER2012_DATETIME2(DbType.SQL_SERVER.getDb(), "DATETIME2", "CONVERT(DATETIME2, '${data}', 120)"),
    SQL_SERVER2012_DATETIMEOFFSET(DbType.SQL_SERVER.getDb(), "DATETIMEOFFSET", "CONVERT(DATETIMEOFFSET, '${data}', 120))"),
    ;


    /**
     * 数据源类型
     * 1:MySql数据库
     * 2:MariaDB数据库
     * 3:Oracle11g及以下数据库
     * 4:Oracle12c+数据库
     * 5:PostgreSql数据库
     * 6:SQLServer2008及以下数据库
     * 7:SQLServer2012+数据库
     */
    private final String dbType;

    /**
     * 字段类型 DATE DATETIME TIME TIMESTAMP
     */
    private final String fieldType;

    /**
     * sql
     */
    private final String sql;

    DbDataType(String dbType, String fieldType, String sql) {
        this.dbType = dbType;
        this.fieldType = fieldType;
        this.sql = sql;
    }

    /**
     * 通过数据源类型及字段类型获取枚举数据
     *
     * @param dbType
     * @param fieldType
     * @return
     */
    public static DbDataType getByDbTypeAndFieldType(String dbType, String fieldType) {
        if (fieldType.indexOf("(") > -1) {
            fieldType = fieldType.substring(0, fieldType.indexOf("("));
        }
        DbDataType result = null;
        for (DbDataType dbTypeEnum : DbDataType.values()) {
            if (dbTypeEnum.dbType.equals(dbType) && dbTypeEnum.fieldType.equals(fieldType)) {
                result = dbTypeEnum;
            }
        }
        return result;
    }


    public static String checkTime(String dataType) {
        if (dataType.indexOf("(") > -1) {
            dataType = dataType.substring(0, dataType.indexOf("("));
        }
        String result = "";
        switch (dataType.toUpperCase(Locale.ROOT)) {
            case "DATE":
                result = "DATE";
                break;
            case "DATETIME":
                result = "DATETIME";
                break;
            case "DATETIME2":
                result = "DATETIME2";
                break;
            case "DATETIMEOFFSET":
                result = "DATETIMEOFFSET";
                break;
            case "TIME":
                result = "TIME";
                break;
            case "TIMESTAMP":
                result = "TIMESTAMP";
                break;
            case "SMALLDATETIME":
                result = "SMALLDATETIME";
                break;

        }
        return result;
    }
}
