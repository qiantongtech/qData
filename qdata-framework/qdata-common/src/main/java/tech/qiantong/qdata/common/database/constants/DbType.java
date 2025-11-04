package tech.qiantong.qdata.common.database.constants;

import tech.qiantong.qdata.common.database.exception.DataQueryException;

/**
 * 数据库类型
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public enum DbType {

    /**
     * MYSQL
     */
    MYSQL("MySql",
            "MySql数据库",
            "jdbc:mysql://${host}:${port}/${dbName}?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC LIMIT ({pageNo}-1)*{pageSize},{pageSize}"),
    /**
     * MARIADB
     */
    MARIADB("2",
            "MariaDB数据库",
            "jdbc:mariadb://${host}:${port}/${dbName}",
            "CHAR_LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC LIMIT ({pageNo}-1)*{pageSize},{pageSize}"),
    /**
     * ORACLE
     */
    ORACLE("Oracle11",
            "Oracle11g及以下数据库",
            "jdbc:oracle:thin:@${host}:${port}:${sid}",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT *  FROM (SELECT a.*,ROWNUM rnum FROM(SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC) a WHERE ROWNUM <= ( {pageNo}- 1 ) * {pageSize} + {pageSize}) WHERE rnum > ( {pageNo} - 1 ) * {pageSize}"),
    /**
     * oracle12c new pagination
     */
    ORACLE_12C("Oracle",
            "Oracle12c+数据库",
            "jdbc:oracle:thin:@${host}:${port}:${sid}",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC OFFSET {pageNo} ROWS FETCH NEXT {pageSize} ROWS ONLY"),
    /**
     * POSTGRESQL
     */
    POSTGRE_SQL("5",
            "PostgreSql数据库",
            "jdbc:postgresql://${host}:${port}/${dbName}",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC LIMIT {pageSize} OFFSET ({pageNo}-1)*{pageSize}"),
    /**
     * SQLSERVER2005
     */
    SQL_SERVER2008("6",
            "SQLServer2008及以下数据库",
            "jdbc:sqlserver://${host}:${port};DatabaseName=${dbName}",
            "LEN",
            "SELECT COUNT(1) FROM {tableName}",
            ""),
    /**
     * SQLSERVER
     */
    SQL_SERVER("SQL_Server",
            "SQLServer2012+数据库",
            "jdbc:sqlserver://${host}:${port};DatabaseName=${dbName};encrypt=true;trustServerCertificate=true",
            "LEN",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC OFFSET {pageNo} ROWS FETCH NEXT {pageSize} ROWS ONLY"),
    /**
     * UNKONWN DB
     */
    OTHER("8",
            "其他数据库",
            "",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            ""),
    /**
     * 达梦8
     */
    DM8("DM8",
            "达梦8",
            "jdbc:dm://${host}:${port}?schema=${dbName}",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} LIMIT ({pageNo}-1)*{pageSize},{pageSize}"),
    /**
     * 人大金仓数据库
     */
    KINGBASE8("Kingbase8",
            "人大金仓数据库",
            "jdbc:kingbase8://${host}:${port}/${dbName}",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC LIMIT {pageSize} OFFSET ({pageNo}-1)*{pageSize} "),
    /**
     * Kafka
     */
    KAFKA("Kafka",
            "人大金仓数据库",
            "${host}:${port}",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC LIMIT {pageSize} OFFSET ({pageNo}-1)*{pageSize}");


    /**
     * 数据库名称
     */
    private final String db;

    /**
     * 描述
     */
    private final String desc;

    /**
     * url
     */
    private final String url;

    /**
     * lengthFun
     */
    private final String lengthFun;

    /**
     * 统计数量
     */
    private String selectCount;

    /**
     * 分页查询
     */
    private String selectPage;

    public String getLengthFun() {
        return lengthFun;
    }

    public String getDb() {
        return this.db;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getUrl() {
        return this.url;
    }

    public String getSelectCount() {
        return this.selectCount;
    }

    public String getSelectPage() {
        return this.selectPage;
    }

    DbType(String db, String desc, String url, String lengthFun, String selectCount, String selectPage) {
        this.db = db;
        this.desc = desc;
        this.url = url;
        this.lengthFun = lengthFun;
        this.selectCount = selectCount;
        this.selectPage = selectPage;
    }

    /**
     * 获取数据库类型
     *
     * @param dbType 数据库类型字符串
     */
    public static DbType getDbType(String dbType) {
        for (DbType type : DbType.values()) {
            if (type.db.equals(dbType)) {
                return type;
            }
        }
        throw new DataQueryException("不支持的数据库类型");
    }
}
