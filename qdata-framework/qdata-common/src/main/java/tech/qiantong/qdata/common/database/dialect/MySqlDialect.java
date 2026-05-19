/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.common.database.dialect;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbName;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.database.utils.DatabaseUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MySql 数据库方言
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class MySqlDialect extends AbstractDbDialect {


    // 定义一个包含常见MySQL保留关键字的集合（全部转换为大写便于比较）
    private static final String[] MYSQL_RESERVED_WORDS = {
            "ACCESSIBLE", "ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC", "ASENSITIVE",
            "BEFORE", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE",
            "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN", "CONDITION",
            "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME",
            "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR",
            "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DECLARE", "DEFAULT",
            "DELAYED", "DELETE", "DESC", "DESCRIBE", "DETERMINISTIC", "DISTINCT", "DISTINCTROW",
            "DIV", "DOUBLE", "DROP", "DUAL", "EACH", "ELSE", "ELSEIF", "ENCLOSED", "ESCAPED", "EXISTS",
            "EXIT", "EXPLAIN", "FALSE", "FETCH", "FLOAT", "FLOAT4", "FLOAT8", "FOR", "FORCE",
            "FOREIGN", "FROM", "FULLTEXT", "GENERATED", "GET", "GRANT", "GROUP", "HAVING", "HIGH_PRIORITY",
            "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER", "INOUT", "INSENSITIVE", "INSERT", "INT",
            "INT1", "INT2", "INT3", "INT4", "INT8", "INTEGER", "INTERVAL", "INTO", "IO_AFTER_GTIDS",
            "IO_BEFORE_GTIDS", "IS", "ITERATE", "JOIN", "KEY", "KEYS", "KILL", "LEADING", "LEAVE",
            "LEFT", "LIKE", "LIMIT", "LINEAR", "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP", "LOCK",
            "LONG", "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY", "MASTER_BIND", "MASTER_SSL_VERIFY_SERVER_CERT",
            "MATCH", "MAXVALUE", "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND",
            "MINUTE_SECOND", "MOD", "MODIFIES", "NATURAL", "NOT", "NO_WRITE_TO_BINLOG", "NULL", "NUMERIC",
            "ON", "OPTIMIZE", "OPTION", "OPTIONALLY", "OR", "ORDER", "OUT", "OUTER", "OUTFILE", "PARTITION",
            "PRECISION", "PRIMARY", "PROCEDURE", "PURGE", "RANGE", "READ", "READS", "READ_WRITE", "REAL",
            "RECURSIVE", "REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESIGNAL",
            "RESTRICT", "RETURN", "REVOKE", "RIGHT", "RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND",
            "SELECT", "SENSITIVE", "SEPARATOR", "SET", "SHOW", "SIGNAL", "SMALLINT", "SPATIAL", "SPECIFIC",
            "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT",
            "SSL", "STARTING", "STORED", "STRAIGHT_JOIN", "TABLE", "TERMINATED", "THEN", "TINYBLOB", "TINYINT",
            "TINYTEXT", "TO", "TRAILING", "TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE", "UNLOCK", "UNSIGNED",
            "UPDATE", "USAGE", "USE", "USING", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY",
            "VARCHAR", "VARCHARACTER", "VARYING", "VIRTUAL", "WHEN", "WHERE", "WHILE", "WITH", "WRITE", "XOR",
            "YEAR_MONTH", "ZEROFILL"
    };

    @Override
    public RowMapper<DbColumn> columnMapper() {
        return (ResultSet rs, int rowNum) -> {
            DbColumn entity = new DbColumn();
            if (DatabaseUtil.hasColumn(rs, "TABLENAME")) {
                entity.setTableName(rs.getString("TABLENAME"));
            }
            entity.setColName(rs.getString("COLNAME"));
            entity.setDataType(rs.getString("DATATYPE"));
            entity.setDataLength(rs.getString("DATALENGTH"));
            entity.setDataPrecision(rs.getString("DATAPRECISION"));
            if (rs.getString("DATAPRECISION") != null) {
                entity.setDataLength(rs.getString("DATAPRECISION"));
            }
            entity.setDataScale(rs.getString("DATASCALE"));
            entity.setColKey("PRI".equals(rs.getString("COLKEY")));
            entity.setNullable("YES".equals(rs.getString("NULLABLE")));
            entity.setColPosition(rs.getInt("COLPOSITION"));
            entity.setDataDefault(rs.getString("DATADEFAULT"));
            entity.setColComment(rs.getString("COLCOMMENT"));
            return entity;
        };
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return "select column_name AS COLNAME, ordinal_position AS COLPOSITION, column_default AS DATADEFAULT, is_nullable AS NULLABLE, data_type AS DATATYPE, " +
                "(CASE WHEN character_maximum_length=4294967295 THEN null ELSE character_maximum_length END) AS DATALENGTH, numeric_precision AS DATAPRECISION, numeric_scale AS DATASCALE, column_key AS COLKEY, column_comment AS COLCOMMENT " +
                "from information_schema.columns where table_schema = '" + dbQueryProperty.getDbName() + "' and table_name = '" + tableName + "' order by ordinal_position ";
    }

    @Override
    public String getDbColumns(DbQueryProperty dbQueryProperty) {
        return "SELECT " +
                "table_name              AS TABLENAME, " +
                "column_name             AS COLNAME, " +
                "ordinal_position        AS COLPOSITION, " +
                "column_default          AS DATADEFAULT, " +
                "is_nullable             AS NULLABLE, " +
                "data_type               AS DATATYPE, " +
                "(CASE WHEN character_maximum_length=4294967295 THEN null ELSE character_maximum_length END) AS DATALENGTH, " +
                "numeric_precision       AS DATAPRECISION, " +
                "numeric_scale           AS DATASCALE, " +
                "column_key              AS COLKEY, " +
                "column_comment          AS COLCOMMENT " +
                "FROM information_schema.columns " +
                "WHERE table_schema = '" + dbQueryProperty.getDbName() + "' " +
                "ORDER BY table_name, ordinal_position";
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '" + dbQueryProperty.getDbName() + "' AND table_name = '" + tableName + "';";
    }

    @Override
    public String buildTableNameByDbType(DbQueryProperty dbQueryProperty, String tableName) {
        if (StringUtils.isNotEmpty(dbQueryProperty.getDbName())) {
            return dbQueryProperty.getDbName() + "." + tableName;
        }

        return tableName;
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        List<String> sqlList = new ArrayList<>();

        List<String> primaryKeys = new ArrayList<>();
        {
            StringBuilder sql = new StringBuilder();
            // 生成CREATE TABLE语句
            sql.append("CREATE TABLE ").append(tableName).append(" (\n");

            for (DbColumn column : dbColumnList) {
                String columnType = column.getDataType();
                String colName = column.getColName();

                sql.append("  ").append(this.escapeReservedKeyword(colName)).append(" ");

                // 转换数据类型为MySQL支持的类型
                switch (columnType) {
                    case "varchar":
                    case "varchar2":
                    case "VARCHAR":
                    case "VARCHAR2":  // MySQL不支持VARCHAR2，映射为VARCHAR
                        sql.append("VARCHAR");
                        if (StringUtils.isNotEmpty(column.getDataLength())) {
                            sql.append("(").append(column.getDataLength()).append(")");
                        }
                        break;
                    case "CHAR":
                    case "char":
                        sql.append("CHAR");
                        if (StringUtils.isNotEmpty(column.getDataLength())) {
                            sql.append("(").append(column.getDataLength()).append(")");
                        }
                        break;
                    case "TEXT":
                    case "text":
                        sql.append("TEXT");
                        break;
                    case "INT":
                    case "INTEGER":
                    case "int":
                    case "integer":
                        sql.append("INT");
                        break;
                    case "bigint":
                    case "BIGINT":
                        sql.append("BIGINT");
                        break;
                    case "tinyint":
                    case "TINYINT":
                        sql.append("TINYINT");
                        break;
                    case "NUMERIC":
                    case "NUMBER":
                    case "decimal":
                    case "DECIMAL":
                        sql.append(generateColumnSQLMySql("DECIMAL", column.getDataLength(), column.getDataScale(), 65, 30));
                        break;
                    case "float":
                    case "FLOAT":
                        sql.append("FLOAT");
                        break;
                    case "double":
                    case "DOUBLE":
                        sql.append("DOUBLE");
                        break;
                    case "date":
                    case "DATE":
                        sql.append("DATE");
                        break;
                    case "timestamp":
                    case "TIMESTAMP":
                    case "datetime":
                    case "DATETIME":
                        sql.append("DATETIME");
                        break;
                    case "time":
                    case "TIME":
                        sql.append("TIME");
                        break;
                    case "year":
                    case "YEAR":
                        sql.append("YEAR");
                        break;
                    default:
                        sql.append(columnType); // 默认处理未知类型
                        break;
                }

                // 检查是否必填
                if (!column.getNullable()) {
                    sql.append(" NOT NULL");
                }

                // 默认值处理
                if (StringUtils.isNotEmpty(column.getDataDefault())) {
                    sql.append(" DEFAULT ").append(column.getDataDefault());
//                    if (columnType.equals("VARCHAR") || columnType.equals("CHAR") || columnType.equals("TEXT")) {
//                        sql.append(" DEFAULT '").append(column.getDataDefault()).append("'");
//                    } else {
//                        sql.append(" DEFAULT ").append(column.getDataDefault());
//                    }
                } else if (column.getNullable() && !column.getColKey()) {//不存在默认值并且允许为NULL
                    sql.append(" DEFAULT NULL");
                }

                // 添加字段备注（COMMENT）
                if (StringUtils.isNotEmpty(column.getColComment())) {
                    sql.append(" COMMENT '").append(DatabaseUtil.escapeSingleQuotes(column.getColComment())).append("'");
                }

                // 加入字段到主键列表，如果是主键
                if (column.getColKey()) {
                    primaryKeys.add(column.getColName());
                }

                sql.append(",\n");
            }

            // 移除最后的逗号和换行
            sql.setLength(sql.length() - 2);
            sql.append("\n");

            // 添加主键约束
            if (!primaryKeys.isEmpty()) {
                sql.append(", PRIMARY KEY (");
                for (String pk : primaryKeys) {
                    sql.append(pk).append(", ");
                }
                sql.setLength(sql.length() - 2); // 移除最后的逗号和空格
                sql.append(")");
            }

            sql.append("\n) ENGINE=InnoDB ");

            // 添加表备注
            if (StringUtils.isNotEmpty(tableComment)) {
                sql.append("COMMENT='").append(DatabaseUtil.escapeSingleQuotes(tableComment));
                sql.append("'\n");
            }
            sqlList.add(sql.toString());
        }


        return sqlList;
    }

    public static String escapeReservedKeyword(String colName) {
        if (colName == null || colName.isEmpty()) {
            return colName;
        }
        for (String reserved : MYSQL_RESERVED_WORDS) {
            if (reserved.equalsIgnoreCase(colName)) {
                return "`" + colName + "`";
            }
        }
        return colName;
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return null;
    }


    public static String generateColumnSQLMySql(String columnType, String columnLength, String columnScale, int maxLength, int maxScale) {
        StringBuilder sql = new StringBuilder(columnType);

        // 仅当是需要长度和小数位数的类型时，才处理长度
        if (columnType.equalsIgnoreCase("DECIMAL") || columnType.equalsIgnoreCase("FLOAT")) {
            if (StringUtils.isNotEmpty(columnLength)) {
                int length = Integer.parseInt(columnLength);
                // 限制长度不超过最大长度
                if (length > maxLength) {
                    length = maxLength;
                }
                sql.append("(").append(length);

                // 如果列类型是 DECIMAL 并且提供了小数位数，则附加小数位
                if (columnType.equalsIgnoreCase("DECIMAL") && StringUtils.isNotEmpty(columnScale)) {
                    int scale = Integer.parseInt(columnScale);
                    // 限制小数位数不超过最大值
                    if (scale > maxScale) {
                        scale = maxScale;
                    }
                    sql.append(", ").append(scale);
                }

                sql.append(")");
            }
        }

        return sql.toString();
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return "SELECT table_name AS TABLENAME, table_comment AS TABLECOMMENT FROM information_schema.tables where table_schema = '" + dbQueryProperty.getDbName() + "' ";
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        // 如果没有传入字段，则默认使用 * 查询所有字段
        if (columns == null || columns.isEmpty()) {
            return "SELECT * FROM " + tableName;
        }
        // 根据传入的 DbColumn 列表获取所有字段名，并用逗号分隔
        String fields = columns.stream()
                .map(column -> escapeReservedKeyword(column.getColName()))
                .collect(Collectors.joining(", "));

        // 构造最终的 SQL 查询语句
        return "SELECT " + fields + " FROM " + dbQueryProperty.getDbName() + "." + tableName;
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return "SELECT SUM(data_length) / 1024 / 1024 AS \"usedSizeMb\" FROM information_schema.tables   WHERE table_schema = '" + dbName + "' GROUP BY table_schema";
    }

    @Override
    public String getDbName() {
        return "SELECT DATABASE() AS \"databaseName\"";
    }

    @Override
    public String getDbName(DbName dbName) {
        int level = dbName == null ? 1 : dbName.getLevel() + 1;
        // 只有一个层级：数据库（库）
        if (level == 1) {
            return "SELECT schema_name AS DBNAME, 1 AS TOTALLEVELS \n" +
                    "FROM information_schema.schemata\n" +
                    "WHERE schema_name NOT IN ('information_schema','mysql','performance_schema','sys')";
        }
        // 没有第二层
        throw new UnsupportedOperationException("MySQL only has one level");
    }


    /**
     * 功能说明：统计 MySQL 中各数据库（Schema）的物理空间使用信息。
     * 数据来源：information_schema.tables。
     * <p>
     * 查询结果字段说明：
     * -------------------------------------------------------------------------
     * dbName          : 数据库名称（Schema 名）。
     * tableCount      : 数据库中表的数量（仅统计 BASE TABLE 类型，不含视图）。
     * viewCount       : 数据库中视图的数量（仅统计 VIEW 类型）。
     * dataSizeMB      : 数据文件总大小（单位 MB），即所有表 data_length 之和。
     * indexSizeMB     : 索引文件总大小（单位 MB），即所有表 index_length 之和。
     * totalSizeMB     : 数据 + 索引的总占用空间（单位 MB）。
     * rowCountApprox  : 各表记录数的近似总和（InnoDB 为估算值）。
     * collectedAt     : 数据采集时间（执行查询的时间戳）。
     * -------------------------------------------------------------------------
     */
//    @Override
//    public String getDatabasePhysicalInfo(DbName dbName) {
//        return "\n" +
//                "SELECT\n" +
//                "    table_schema AS dbName,\n" +
//                "    COUNT(CASE WHEN table_type = 'BASE TABLE' THEN 1 END) AS tableCount,\n" +
//                "    COUNT(CASE WHEN table_type = 'VIEW' THEN 1 END) AS viewCount,\n" +
//                "    ROUND(SUM(data_length) / 1024 / 1024, 2) AS dataSizeMB,\n" +
//                "    ROUND(SUM(index_length) / 1024 / 1024, 2) AS indexSizeMB,\n" +
//                "    ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS totalSizeMB,\n" +
//                "    SUM(table_rows) AS rowCountApprox,\n" +
//                "    NOW() AS collectedAt\n" +
//                "FROM information_schema.tables\n" +
//                "WHERE table_schema NOT IN ('information_schema', 'mysql', 'performance_schema', 'sys')\n" +
//                "GROUP BY table_schema\n" +
//                "ORDER BY totalSizeMB DESC;\n";
//    }
    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        String sql = "INSERT INTO {tableName} ({tableFieldName}) values({tableFieldValue}) ON DUPLICATE KEY UPDATE {setValue}";
        sql = StringUtils
                .replace(sql, "{tableName}", tableName)
                .replace("{tableFieldName}", tableFieldName)
                .replace("{tableFieldValue}", tableFieldValue)
                .replace("{setValue}", setValue);
        return sql;
    }

    @Override
    public String getFlinkSQL(DbQueryProperty property, String flinkTableName, String tableName, String tableFieldName) {
        String sql = "CREATE TABLE ${flinkTableName} (${tableFieldName}) " +
                "WITH ( 'connector' = 'jdbc'," +
                "'url' = 'jdbc:mysql://${host}:${port}/${dbName}?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai'," +
                "'table-name' = '${tableName}'," +
                "'username' = '${username}'," +
                "'password' = '${password}')";
        sql = StringUtils
                .replace(sql, "${flinkTableName}", flinkTableName)
                .replace("${tableName}", tableName)
                .replace("${host}", property.getHost())
                .replace("${tableFieldName}", tableFieldName)
                .replace("${port}", String.valueOf(property.getPort()))
                .replace("${dbName}", property.getDbName())
                .replace("${username}", property.getUsername())
                .replace("${password}", property.getPassword());
        return sql;
    }

    @Override
    public String getFlinkCDCSQL(DbQueryProperty property, String flinkTableName, String tableName, String tableFieldName) {
        String sql = "CREATE TABLE ${flinkTableName} (${tableFieldName}) " +
                "WITH ( 'connector' = 'mysql-cdc'," +
                " 'hostname' = '${host}' ," +
                "'port' = '${port}' ," +
                "'username' = '${username}' ," +
                "'password' = '${password}'," +
                "'database-name' = '${dbName}' ," +
                "'table-name' = '${tableName}' ," +
                "'server-time-zone' = 'Asia/Shanghai'," +
                "'scan.incremental.snapshot.enabled' = 'true'," +
                "'debezium.snapshot.mode'='initial'" +
                ")";
        sql = StringUtils
                .replace(sql, "${flinkTableName}", flinkTableName)
                .replace("${tableName}", tableName)
                .replace("${host}", property.getHost())
                .replace("${tableFieldName}", tableFieldName)
                .replace("${port}", String.valueOf(property.getPort()))
                .replace("${dbName}", property.getDbName())
                .replace("${username}", property.getUsername())
                .replace("${password}", property.getPassword());
        return sql;
    }

    @Override
    public String getFlinkSinkSQL(DbQueryProperty property, JSONObject config, String flinkTableName, String tableName, String tableFieldName) {
        String sql = "CREATE TABLE ${flinkTableName} (${tableFieldName}) " +
                "WITH ( 'connector' = 'jdbc'," +
                "'url' = 'jdbc:mysql://${host}:${port}/${dbName}?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai'," +
                "'table-name' = '${tableName}'," +
                "'username' = '${username}'," +
                "'password' = '${password}'," +
                "'sink.buffer-flush.max-rows' = '${batchSize}'," +
                "'sink.buffer-flush.interval' = '1s'" +
                ")";
        sql = StringUtils
                .replace(sql, "${flinkTableName}", flinkTableName)
                .replace("${tableName}", tableName)
                .replace("${host}", property.getHost())
                .replace("${tableFieldName}", tableFieldName)
                .replace("${port}", String.valueOf(property.getPort()))
                .replace("${dbName}", property.getDbName())
                .replace("${username}", property.getUsername())
                .replace("${password}", property.getPassword())
                .replace("${batchSize}", String.valueOf(config.getIntValue("batchSize", 100)));
        return sql;
    }

    @Override
    public RowMapper<DbTable> tableMapper() {
        return (ResultSet rs, int rowNum) -> {
            DbTable entity = new DbTable();
            entity.setTableName(rs.getString("TABLENAME"));
            entity.setTableComment(rs.getString("TABLECOMMENT"));
            return entity;
        };
    }

    @Override
    public String trainToJdbcUrl(DbQueryProperty property) {
        String url = DbType.getDbType(property.getDbType()).getUrl();
        if (org.springframework.util.StringUtils.isEmpty(url)) {
            throw new DataQueryException("无效数据库类型!");
        }
        url = url.replace("${host}", property.getHost());
        url = url.replace("${port}", String.valueOf(property.getPort()));
        url = url.replace("${dbName}", property.getDbName());
        //判断是否开启ssl
        if (checkUseSSL(property)) {
            url = url.replace("useSSL=false", "useSSL=true");
            JSONObject sslConfig = (JSONObject) property.getDatasourceConfig().get("sslConfig");
            String trustCertificateKeyStoreUrl = sslConfig.getString("trustCertificateKeyStoreUrl");
            if (StringUtils.indexOf(trustCertificateKeyStoreUrl, "file:") == -1) {
                trustCertificateKeyStoreUrl = "file:" + trustCertificateKeyStoreUrl;
            }
            StringBuilder urlStrBuilder = new StringBuilder(url);
            urlStrBuilder.append("&requireSSL=true")
                    .append("&verifyServerCertificate=true")
                    .append("&trustCertificateKeyStoreUrl=").append(trustCertificateKeyStoreUrl)
                    .append("&trustCertificateKeyStorePassword=").append(sslConfig.getString("trustCertificateKeyStorePassword"));
            url = urlStrBuilder.toString();
        }
        return url;
    }


    @Override
    public String updateTableComment(DbQueryProperty dbQueryProperty, String tableName, String tableComment) {
        String fullTableName = getTableName(dbQueryProperty, tableName);
        return "ALTER TABLE " + fullTableName + " COMMENT '" + DatabaseUtil.escapeSingleQuotes(tableComment) + "'";
    }

    // ... existing code ...
    @Override
    public String dropColumn(DbQueryProperty dbQueryProperty, String tableName, String colName) {
        String fullTableName = getTableName(dbQueryProperty, tableName);
        return "ALTER TABLE " + fullTableName + " DROP COLUMN " + escapeReservedKeyword(colName);
    }

    @Override
    public List<String> modifyColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column) {
        List<String> sqlList = new ArrayList<>();
        String fullTableName = getTableName(dbQueryProperty, tableName);

        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ").append(fullTableName).append(" MODIFY ");
        sql.append(escapeReservedKeyword(column.getColName())).append(" ");

        String columnType = column.getDataType();
        switch (columnType) {
            case "varchar":
            case "varchar2":
            case "VARCHAR":
            case "VARCHAR2":
                sql.append("VARCHAR");
                if (StringUtils.isNotEmpty(column.getDataLength())) {
                    sql.append("(").append(column.getDataLength()).append(")");
                }
                break;
            case "CHAR":
            case "char":
                sql.append("CHAR");
                if (StringUtils.isNotEmpty(column.getDataLength())) {
                    sql.append("(").append(column.getDataLength()).append(")");
                }
                break;
            case "TEXT":
            case "text":
                sql.append("TEXT");
                break;
            case "INT":
            case "INTEGER":
            case "int":
            case "integer":
                sql.append("INT");
                break;
            case "bigint":
            case "BIGINT":
                sql.append("BIGINT");
                break;
            case "tinyint":
            case "TINYINT":
                sql.append("TINYINT");
                break;
            case "NUMERIC":
            case "NUMBER":
            case "decimal":
            case "DECIMAL":
                sql.append(generateColumnSQLMySql("DECIMAL", column.getDataLength(), column.getDataScale(), 65, 30));
                break;
            case "float":
            case "FLOAT":
                sql.append("FLOAT");
                break;
            case "double":
            case "DOUBLE":
                sql.append("DOUBLE");
                break;
            case "date":
            case "DATE":
                sql.append("DATE");
                break;
            case "timestamp":
            case "TIMESTAMP":
            case "datetime":
            case "DATETIME":
                sql.append("DATETIME");
                break;
            default:
                sql.append(columnType);
                break;
        }

        if (!column.getNullable()) {
            sql.append(" NOT NULL");
        }

        if (StringUtils.isNotEmpty(column.getDataDefault())) {
            sql.append(" DEFAULT ").append(column.getDataDefault());
        } else if (column.getNullable()) {
            sql.append(" DEFAULT NULL");
        }

        if (StringUtils.isNotEmpty(column.getColComment())) {
            sql.append(" COMMENT '").append(DatabaseUtil.escapeSingleQuotes(column.getColComment())).append("'");
        }

        sqlList.add(sql.toString());

        return sqlList;
    }

    @Override
    public List<String> addColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column) {
        List<String> sqlList = new ArrayList<>();
        String fullTableName = getTableName(dbQueryProperty, tableName);

        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ").append(fullTableName).append(" ADD ");
        sql.append(escapeReservedKeyword(column.getColName())).append(" ");

        String columnType = column.getDataType();
        switch (columnType) {
            case "varchar":
            case "varchar2":
            case "VARCHAR":
            case "VARCHAR2":
                sql.append("VARCHAR");
                if (StringUtils.isNotEmpty(column.getDataLength())) {
                    sql.append("(").append(column.getDataLength()).append(")");
                }
                break;
            case "CHAR":
            case "char":
                sql.append("CHAR");
                if (StringUtils.isNotEmpty(column.getDataLength())) {
                    sql.append("(").append(column.getDataLength()).append(")");
                }
                break;
            case "TEXT":
            case "text":
                sql.append("TEXT");
                break;
            case "INT":
            case "INTEGER":
            case "int":
            case "integer":
                sql.append("INT");
                break;
            case "bigint":
            case "BIGINT":
                sql.append("BIGINT");
                break;
            case "tinyint":
            case "TINYINT":
                sql.append("TINYINT");
                break;
            case "NUMERIC":
            case "NUMBER":
            case "decimal":
            case "DECIMAL":
                sql.append(generateColumnSQLMySql("DECIMAL", column.getDataLength(), column.getDataScale(), 65, 30));
                break;
            case "float":
            case "FLOAT":
                sql.append("FLOAT");
                break;
            case "double":
            case "DOUBLE":
                sql.append("DOUBLE");
                break;
            case "date":
            case "DATE":
                sql.append("DATE");
                break;
            case "timestamp":
            case "TIMESTAMP":
            case "datetime":
            case "DATETIME":
                sql.append("DATETIME");
                break;
            default:
                sql.append(columnType);
                break;
        }

        if (!column.getNullable()) {
            sql.append(" NOT NULL");
        }

        if (StringUtils.isNotEmpty(column.getDataDefault())) {
            sql.append(" DEFAULT ").append(column.getDataDefault());
        } else if (column.getNullable()) {
            sql.append(" DEFAULT NULL");
        }

        if (StringUtils.isNotEmpty(column.getColComment())) {
            sql.append(" COMMENT '").append(DatabaseUtil.escapeSingleQuotes(column.getColComment())).append("'");
        }

        sqlList.add(sql.toString());

        return sqlList;
    }

    @Override
    public List<String> updateColKey(DbQueryProperty dbQueryProperty, String tableName, List<DbColumn> colKeyDbColumnList) {
        List<String> sqlList = new ArrayList<>();
        String fullTableName = getTableName(dbQueryProperty, tableName);

        // 首先删除现有的主键约束
        sqlList.add("ALTER TABLE " + fullTableName + " DROP PRIMARY KEY");

        // 如果提供了新的主键字段列表，则添加新的主键约束
        if (colKeyDbColumnList != null && !colKeyDbColumnList.isEmpty()) {
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(fullTableName).append(" ADD PRIMARY KEY (");

            for (int i = 0; i < colKeyDbColumnList.size(); i++) {
                if (i > 0) {
                    sql.append(", ");
                }
                sql.append(escapeReservedKeyword(colKeyDbColumnList.get(i).getColName()));
            }

            sql.append(")");
            sqlList.add(sql.toString());
        }

        return sqlList;
    }

    @Override
    public String getColumnType(DbColumn column) {
        String columnType = column.getDataType();
        switch (column.getDataType()) {
            case "varchar":
            case "varchar2":
            case "VARCHAR":
            case "VARCHAR2":  // MySQL不支持VARCHAR2，映射为VARCHAR
                return "VARCHAR";
            case "CHAR":
            case "char":
                return "CHAR";
            case "TEXT":
            case "text":
                return "TEXT";
            case "INT":
            case "INTEGER":
            case "int":
            case "integer":
                return "INT";
            case "bigint":
            case "BIGINT":
                return "BIGINT";
            case "tinyint":
            case "TINYINT":
                return "TINYINT";
            case "NUMERIC":
            case "NUMBER":
            case "decimal":
            case "DECIMAL":
                return "DECIMAL";
            case "float":
            case "FLOAT":
                return "FLOAT";
            case "double":
            case "DOUBLE":
                return "DOUBLE";
            case "date":
            case "DATE":
                return "DATE";
            case "timestamp":
            case "TIMESTAMP":
            case "datetime":
            case "DATETIME":
                return "DATETIME";
            case "time":
            case "TIME":
                return "TIME";
            case "year":
            case "YEAR":
                return "YEAR";
            default:
                return columnType; // 默认处理未知类型
        }
    }

    @Override
    public String getTableName(DbQueryProperty property, String tableName) {
        if (!org.springframework.util.StringUtils.isEmpty(property.getDbName())) {
            return "`"+property.getDbName() + "`.`" + tableName + "`";
        }
        return tableName;
    }
}
