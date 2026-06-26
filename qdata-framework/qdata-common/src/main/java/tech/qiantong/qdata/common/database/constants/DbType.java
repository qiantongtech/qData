/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

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
    POSTGRE_SQL("PostgreSQL",
            "PostgreSQL数据库",
            "jdbc:postgresql://${host}:${port}/${dbName}?stringtype=unspecified",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC LIMIT {pageSize} OFFSET ({pageNo}-1)*{pageSize}"),
    /**
     * SQLServer2008及以下数据库
     */
    SQL_SERVER2008("SQL_Server2008",
            "SQLServer2008及以下数据库",
            "jdbc:jtds:sqlserver://${host}:${port}/${dbName};ssl=off",
            "LEN",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC OFFSET {pageNo} ROWS FETCH NEXT {pageSize} ROWS ONLY"),
    /**
     * SQLSERVER
     */
    SQL_SERVER("SQL_Server",
            "SQLServer2012+数据库",
            "jdbc:sqlserver://${host}:${port};DatabaseName=${dbName};encrypt=false;trustServerCertificate=true",
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
            "jdbc:kingbase8://${host}:${port}/${dbName}?stringtype=unspecified",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC LIMIT {pageSize} OFFSET ({pageNo}-1)*{pageSize} "),
    /**
     * 人大金仓数据库
     */
    HIVE("Hive",
            "Hive on HBase",
            "jdbc:hive2://${host}:${port}/${dbName}",
            "LENGTH",
            null,
            null),
    /**
     * 人大金仓数据库
     */
    HDFS("HDFS",
            "HDFS数据库",
            "jdbc:kingbase8://${host}:${port}",
            "LENGTH",
            null,
            null),
    /**
     * Kafka
     */
    KAFKA("Kafka",
            "人大金仓数据库",
            "${host}:${port}",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC LIMIT {pageSize} OFFSET ({pageNo}-1)*{pageSize}"),

    /**
     * Phoenix(HBase)
     */
    PHOENIX("Phoenix",
            "Phoenix数据库",
            "jdbc:phoenix:${host}:${port}:/${dbName}",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC LIMIT ({pageNo}-1)*{pageSize},{pageSize}"),

    /**
     * Doris
     */
    DORIS("Doris",
            "Doris数据库",
            "jdbc:mysql://${host}:${port}/${dbName}?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&rewriteBatchedStatements=true&useServerPrepStmts=true&serverTimezone=GMT%2B8",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC LIMIT ({pageNo}-1)*{pageSize},{pageSize}"),

    /**
     * ClickHouse
     */
    CLICK_HOUSE("ClickHouse",
            "ClickHouse数据库",
            "jdbc:clickhouse://${host}:${port}/${dbName}",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC LIMIT {pageSize} OFFSET ({pageNo}-1)*{pageSize}"),
    /**
     * MongoDB（官方 Java Driver）
     */
    MONGODB("MongoDB",
            "MongoDB数据库",
            "mongodb://${user}:${password}@${host}:${port}/${dbName}?authSource=${authDb}",
            "",
            "",
            ""),

    /**
     * 神通数据库（官方 Java Driver）
     */
    OSCAR("OSCAR",
            "神通数据库",
            "jdbc:oscar://${host}:${port}/${dbName}",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT {tableFieldName} FROM {tableName} ORDER BY {orderBy} DESC LIMIT ({pageNo}-1)*{pageSize},{pageSize}"),

    /**
     * DB2 idm
     */
    DB2("DB2",
            "DB2",
            "jdbc:db2://${host}:${port}/${dbName}",
            "LENGTH",
            "SELECT COUNT(1) FROM {tableName}",
            "SELECT * FROM (SELECT *, ROW_NUMBER() OVER (ORDER BY 1) AS rownum FROM {tableName} ORDER BY {orderBy} DESC) AS temp_table WHERE rownum BETWEEN  ( {pageNo} - 1 ) * {pageSize} AND ( {pageNo} - 1 ) * {pageSize} + {pageSize})"),
    /**
     * Redis
     */
    REDIS("Redis", "Redis", "", "", "", ""),
    /**
     * RabbitMQ
     */
    RABBITMQ("RabbitMQ", "RabbitMQ消息队列", "", "", "", ""),
    /**
     * FTP
     */
    FTP("FTP", "FTP", "", "", "", ""),
    /**
     * FTP
     */
    OSS_ALIYUN("OSS-ALIYUN", "OSS(阿里云)", "", "", "", "");


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
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }
}
