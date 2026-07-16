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
 * MySql database dialect
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class MySqlDialect extends AbstractDbDialect {


    // Define a set containing common MySQL reserved keywords (all converted to uppercase for easier comparison)
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
            // Generate CREATE TABLE statement
            sql.append("CREATE TABLE ").append(tableName).append(" (\n");

            for (DbColumn column : dbColumnList) {
                String columnType = column.getDataType();
                String colName = column.getColName();

                sql.append("  ").append(this.escapeReservedKeyword(colName)).append(" ");

                // Convert data types to types supported by MySQL
                switch (columnType) {
                    case "varchar":
                    case "varchar2":
                    case "VARCHAR":
                    case "VARCHAR2":  // MySQL does not support VARCHAR2, mapping to VARCHAR
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
                        sql.append(columnType); // Handle unknown types by default
                        break;
                }

                // Check if required
                if (!column.getNullable()) {
                    sql.append(" NOT NULL");
                }

                // Default value handling
                if (StringUtils.isNotEmpty(column.getDataDefault())) {
                    sql.append(" DEFAULT ").append(column.getDataDefault());
//                    if (columnType.equals("VARCHAR") || columnType.equals("CHAR") || columnType.equals("TEXT")) {
//                        sql.append(" DEFAULT '").append(column.getDataDefault()).append("'");
//                    } else {
//                        sql.append(" DEFAULT ").append(column.getDataDefault());
//                    }
                } else if (column.getNullable() && !column.getColKey()) {//There is no default value and NULL is allowed
                    sql.append(" DEFAULT NULL");
                }

                // Add field comments (COMMENT)
                if (StringUtils.isNotEmpty(column.getColComment())) {
                    sql.append(" COMMENT '").append(DatabaseUtil.escapeSingleQuotes(column.getColComment())).append("'");
                }

                // Add the field to the primary key list, if it is a primary key
                if (column.getColKey()) {
                    primaryKeys.add(column.getColName());
                }

                sql.append(",\n");
            }

            // Remove final comma and newline
            sql.setLength(sql.length() - 2);
            sql.append("\n");

            // Add primary key constraints
            if (!primaryKeys.isEmpty()) {
                sql.append(", PRIMARY KEY (");
                for (String pk : primaryKeys) {
                    sql.append(pk).append(", ");
                }
                sql.setLength(sql.length() - 2); // Remove final comma and space
                sql.append(")");
            }

            sql.append("\n) ENGINE=InnoDB ");

            // Add table notes
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

        // Handle length only if it is a type that requires length and number of decimal places
        if (columnType.equalsIgnoreCase("DECIMAL") || columnType.equalsIgnoreCase("FLOAT")) {
            if (StringUtils.isNotEmpty(columnLength)) {
                int length = Integer.parseInt(columnLength);
                // Limit the length to no more than the maximum length
                if (length > maxLength) {
                    length = maxLength;
                }
                sql.append("(").append(length);

                // If the column type is DECIMAL and the number of decimal places is provided, append the decimal places
                if (columnType.equalsIgnoreCase("DECIMAL") && StringUtils.isNotEmpty(columnScale)) {
                    int scale = Integer.parseInt(columnScale);
                    // Limit the number of decimal places to the maximum
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
        // If no fields are passed in, * will be used by default to query all fields.
        if (columns == null || columns.isEmpty()) {
            return "SELECT * FROM " + tableName;
        }
        // Get all field names based on the passed in DbColumn list, separated by commas
        String fields = columns.stream()
                .map(column -> escapeReservedKeyword(column.getColName()))
                .collect(Collectors.joining(", "));

        // Construct the final SQL query statement
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
        // Only one level is used: database
        if (level == 1) {
            return "SELECT schema_name AS DBNAME, 1 AS TOTALLEVELS \n" +
                    "FROM information_schema.schemata\n" +
                    "WHERE schema_name NOT IN ('information_schema','mysql','performance_schema','sys')";
        }
        // No second layer
        throw new UnsupportedOperationException("MySQL only has one level");
    }


    /**
     * Function description: Statistics of physical space usage information of each database (Schema) in MySQL.
     * Data source: information_schema.tables.
     * <p>
     * Query result field description:
     * -------------------------------------------------------------------------
     * dbName: Database name (Schema name).
     * tableCount: The number of tables in the database (only counts BASE TABLE type, excluding views).
     * viewCount: The number of views in the database (only counts VIEW type).
     * dataSizeMB: The total size of the data file (in MB), which is the sum of data_length of all tables.
     * indexSizeMB: The total size of the index file (unit MB), that is, the sum of index_length of all tables.
     * totalSizeMB: The total space occupied by data + index (in MB).
     * rowCountApprox: The approximate sum of the number of records in each table (InnoDB is an estimate).
     * collectedAt: Data collection time (timestamp of query execution).
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
            throw new DataQueryException("db.error.invalid.dbtype", "无效数据库类型");
        }
        url = url.replace("${host}", property.getHost());
        url = url.replace("${port}", String.valueOf(property.getPort()));
        url = url.replace("${dbName}", property.getDbName());
        //Determine whether to enable ssl
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

        // First delete the existing primary key constraint
        sqlList.add("ALTER TABLE " + fullTableName + " DROP PRIMARY KEY");

        // If a new primary key field list is provided, add a new primary key constraint
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
            case "VARCHAR2":  // MySQL does not support VARCHAR2, mapping to VARCHAR
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
                return columnType; // Handle unknown types by default
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
