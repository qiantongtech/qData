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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbName;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.database.utils.DatabaseUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handle JDBC SQL execution.
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
@Slf4j
public class SQLServer2008Dialect extends AbstractDbDialect {

    @Override
    public String columns(String dbName, String tableName) {
        return "select columns.name AS colName, columns.column_id AS COLPOSITION, columns.max_length AS DATALENGTH, columns.precision AS DATAPRECISION, columns.scale AS DATASCALE, " +
                "columns.is_nullable AS NULLABLE, types.name AS DATATYPE, CAST(ep.value  AS NVARCHAR(128)) AS COLCOMMENT, (CASE WHEN e.text LIKE '((%)' AND e.text NOT LIKE '%)%''%' THEN SUBSTRING(e.text, 3, LEN(e.text) - 4) WHEN e.text LIKE '(%' THEN SUBSTRING(e.text, 2, LEN(e.text) - 2) ELSE e.text END) AS DATADEFAULT, " +
                "(CASE WHEN (SELECT ic.column_id FROM sys.indexes idx INNER JOIN sys.index_columns ic ON idx.object_id = ic.object_id AND idx.index_id = ic.index_id WHERE idx.is_primary_key = 1 AND columns.column_id = ic.column_id AND columns.object_id = ic.object_id)  IS NOT NULL THEN '1' ELSE '0' END) AS COLKEY " +
                "from sys.tables tables " +
                "JOIN sys.columns columns ON tables.object_id = columns.object_id " +
                "LEFT JOIN sys.types types ON columns.user_type_id = types.user_type_id " +
                "LEFT JOIN syscomments e ON columns.default_object_id= e.id " +
                "LEFT JOIN sys.extended_properties ep ON ep.major_id = columns.object_id AND ep.minor_id = columns.column_id AND ep.name = 'MS_Description' " +
                "where tables.name = '" + tableName + "' " +
                "order by columns.column_id ";
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return "select columns.name AS colName, columns.column_id AS COLPOSITION, columns.max_length AS DATALENGTH, columns.precision AS DATAPRECISION, columns.scale AS DATASCALE, " +
                "columns.is_nullable AS NULLABLE, types.name AS DATATYPE, CAST(ep.value  AS NVARCHAR(128)) AS COLCOMMENT, (CASE WHEN e.text LIKE '((%)' AND e.text NOT LIKE '%)%''%' THEN SUBSTRING(e.text, 3, LEN(e.text) - 4) WHEN e.text LIKE '(%' THEN SUBSTRING(e.text, 2, LEN(e.text) - 2) ELSE e.text END) AS DATADEFAULT, " +
                "(CASE WHEN (SELECT ic.column_id FROM sys.indexes idx INNER JOIN sys.index_columns ic ON idx.object_id = ic.object_id AND idx.index_id = ic.index_id WHERE idx.is_primary_key = 1 AND columns.column_id = ic.column_id AND columns.object_id = ic.object_id)  IS NOT NULL THEN '1' ELSE '0' END) AS COLKEY " +
                "from sys.tables tables " +
                "JOIN sys.columns columns ON tables.object_id = columns.object_id " +
                "LEFT JOIN sys.types types ON columns.user_type_id = types.user_type_id " +
                "LEFT JOIN syscomments e ON columns.default_object_id= e.id " +
                "LEFT JOIN sys.extended_properties ep ON ep.major_id = columns.object_id AND ep.minor_id = columns.column_id AND ep.name = 'MS_Description' " +
                "where tables.name = '" + tableName + "' " +
                "AND SCHEMA_NAME(tables.schema_id) = '" + dbQueryProperty.getSid() + "' " +
                "order by columns.column_id ";
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_CATALOG = '" + dbQueryProperty.getDbName() + "' AND TABLE_SCHEMA = '" + dbQueryProperty.getSid() + "' AND TABLE_NAME = '" + tableName + "';";
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        List<String> sqlList = new ArrayList<>();


        List<String> primaryKeys = new ArrayList<>();
        {
            StringBuilder sql = new StringBuilder();
            // Implementation details.
            sql.append("CREATE TABLE ").append(tableName).append(" (\n");

            for (DbColumn column : dbColumnList) {
                String columnType = column.getDataType().toUpperCase();
                sql.append("  ").append(column.getColName()).append(" ");

                // Handle JDBC SQL execution.
                switch (columnType) {
                    case "VARCHAR":
                    case "VARCHAR2": // Handle JDBC SQL execution.
                        sql.append("VARCHAR");
                        if (StringUtils.isNotEmpty(column.getDataLength())) {
                            sql.append("(").append(column.getDataLength()).append(")");
                        } else {
                            sql.append("(MAX)"); // Handle JDBC SQL execution.
                        }
                        break;
                    case "CHAR":
                        sql.append("CHAR");
                        if (StringUtils.isNotEmpty(column.getDataLength())) {
                            sql.append("(").append(column.getDataLength()).append(")");
                        }
                        break;
                    case "TEXT":
                        sql.append("TEXT");
                        break;
                    case "INT":
                    case "INTEGER":
                        sql.append("INT");
                        break;
                    case "BIGINT":
                        sql.append("BIGINT");
                        break;
                    case "TINYINT":
                        sql.append("TINYINT");
                        break;
                    case "DECIMAL":
                        sql.append("DECIMAL");
                        if (StringUtils.isNotEmpty(column.getDataLength())) {
                            sql.append("(").append(column.getDataLength());
                            if (StringUtils.isNotEmpty(column.getDataScale())) {
                                sql.append(", ").append(column.getDataScale());
                            }
                            sql.append(")");
                        }
                        break;
                    case "FLOAT":
                        sql.append("FLOAT");
                        break;
                    case "DOUBLE":
                        sql.append("FLOAT"); // Handle JDBC SQL execution.
                        break;
                    case "DATE":
                        sql.append("DATE");
                        break;
                    case "DATETIME":
                        sql.append("DATETIME");
                        break;
                    case "TIME":
                        sql.append("TIME");
                        break;
                    default:
                        sql.append(columnType); // Implementation details.
                        break;
                }

                // Validate the input and configuration.
                if (!column.getNullable()) {
                    sql.append(" NOT NULL");
                }

                // Implementation details.
                if (StringUtils.isNotEmpty(column.getDataDefault())) {
                    if (columnType.equals("VARCHAR") || columnType.equals("CHAR") || columnType.equals("TEXT")) {
                        sql.append(" DEFAULT '").append(column.getDataDefault()).append("'");
                    } else {
                        sql.append(" DEFAULT ").append(column.getDataDefault());
                    }
                }

                // Implementation details.
                if (column.getColKey()) {
                    primaryKeys.add(column.getColName());
                }

                sql.append(",\n");
            }

            // Implementation details.
            sql.setLength(sql.length() - 2);
            sql.append("\n");

            // Implementation details.
            if (!primaryKeys.isEmpty()) {
                sql.append(", PRIMARY KEY (");
                for (String pk : primaryKeys) {
                    sql.append(pk).append(", ");
                }
                sql.setLength(sql.length() - 2); // Implementation details.
                sql.append(")");
            }

            sql.append("\n)\n");
            sqlList.add(sql.toString());
        }


        // Handle JDBC SQL execution.
        if (StringUtils.isNotEmpty(tableComment)) {
            StringBuilder sql = new StringBuilder();
            sql.append("EXEC sys.sp_addextendedproperty @name = N'MS_Description', @value = N'")
                    .append(DatabaseUtil.escapeSingleQuotes(tableComment)).append("', @level0type = N'SCHEMA', @level0name = N'dbo', @level1type = N'TABLE', @level1name = N'")
                    .append(tableName).append("'\n");
            sqlList.add(sql.toString());
        }

        // Implementation details.
        for (DbColumn column : dbColumnList) {
            if (StringUtils.isNotEmpty(column.getColComment())) {
                StringBuilder sql = new StringBuilder();
                sql.append("EXEC sys.sp_addextendedproperty @name = N'MS_Description', @value = N'")
                        .append(DatabaseUtil.escapeSingleQuotes(column.getColComment())).append("', @level0type = N'SCHEMA', @level0name = N'dbo', @level1type = N'TABLE', @level1name = N'")
                        .append(tableName).append("', @level2type = N'COLUMN', @level2name = N'")
                        .append(column.getColName()).append("'\n");
                sqlList.add(sql.toString());
            }
        }

        return sqlList;
    }


    @Override
    public List<String> someInternalSqlDorisGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList, String partitionRule, String bucketRule, Integer replica) {
        List<String> sqlList = new ArrayList<>();
        List<String> primaryKeys = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE ").append(tableName).append(" (\n");

        for (DbColumn column : dbColumnList) {
            String columnType = column.getDataType();
            String colName = column.getColName();

            sql.append("  ").append(this.escapeReservedKeyword(colName)).append(" ");

            String columnTypeResolved = "";
            // Implementation details.
            switch (columnType.toUpperCase()) {
                case "VARCHAR":
                case "NVARCHAR":
                    sql.append("VARCHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(255)");
                    }
                    columnTypeResolved = "VARCHAR";
                    break;
                case "CHAR":
                    sql.append("VARCHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(1)");
                    }
                    columnTypeResolved = "VARCHAR";
                    break;
                case "VARCHAR(MAX)":
                case "TEXT":
                    sql.append("STRING");
                    columnTypeResolved = "STRING";
                    break;
                case "SMALLINT":
                    sql.append("SMALLINT");
                    columnTypeResolved = "SMALLINT";
                    break;
                case "TINYINT":
                    sql.append("TINYINT");
                    columnTypeResolved = "TINYINT";
                    break;
                case "INT":
                    sql.append("INT");
                    columnTypeResolved = "INT";
                    break;
                case "BIGINT":
                    sql.append("BIGINT");
                    columnTypeResolved = "BIGINT";
                    break;
                case "DECIMAL":
                    sql.append(generateColumnSQLDORIS("DECIMAL", column.getDataLength(), column.getDataScale(), 65, 30));
                    columnTypeResolved = "DECIMAL";
                    break;
                case "REAL":
                    sql.append("FLOAT");
                    columnTypeResolved = "FLOAT";
                    break;
                case "FLOAT":
                    sql.append("DOUBLE");
                    columnTypeResolved = "DOUBLE";
                    break;
                case "DATE":
                case "DATETIMEOFFSET":
                case "DATETIME":
                case "DATETIME2":
                case "TIMESTAMP":
                    sql.append("DATETIME");
                    columnTypeResolved = "DATETIME";
                    break;
                default:
                    sql.append("VARCHAR(255)"); // Implementation details.
                    columnTypeResolved = "VARCHAR";
                    break;
            }
            // NOT NULL
            if (!column.getNullable()) {
                sql.append(" NOT NULL");
            }


            String defaultClause = buildDorisDefaultClause(columnTypeResolved, column.getDataDefault());
            sql.append(defaultClause);

            // Implementation details.
            if (StringUtils.isNotEmpty(column.getColComment())) {
                sql.append(" COMMENT '").append(DatabaseUtil.escapeSingleQuotes(column.getColComment())).append("'");
            }

            if (Boolean.TRUE.equals(column.getColKey())) {
                primaryKeys.add(colName);
            }

            sql.append(",\n");
        }

        // Implementation details.
        sql.setLength(sql.length() - 2);
        sql.append("\n)");

        // Implementation details.
        if (!primaryKeys.isEmpty()) {
            sql.append("\nUNIQUE KEY (");
            for (String pk : primaryKeys) {
                sql.append("`").append(pk).append("`, ");
            }
            sql.setLength(sql.length() - 2);
            sql.append(")");
        } else {
            // Implementation details.
            sql.append("\nDUPLICATE KEY (`").append(dbColumnList.get(0).getColName()).append("`)");
        }

        // Implementation details.
        if (StringUtils.isNotBlank(partitionRule)) {
            sql.append("\n").append(partitionRule);
        }

        // Implementation details.
        if (StringUtils.isBlank(bucketRule)) {
            sql.append("\nDISTRIBUTED BY HASH(`").append(dbColumnList.get(0).getColName()).append("`) BUCKETS AUTO");
        } else {
            sql.append("\n").append(bucketRule);
        }

        // Implementation details.
        sql.append("\nPROPERTIES (\n");
        sql.append("  \"replication_num\" = \"" + replica + "\"");
        sql.append("\n)");
        sqlList.add(sql.toString());
        // Implementation details.
        sqlList.add("ALTER TABLE " + tableName + " MODIFY COMMENT '" + tableComment + "'");
        return sqlList;
    }

    // Implementation details.
    private static final String[] DORIS_RESERVED_WORDS = {
            "ACCESSIBLE", "ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC", "ASENSITIVE",
            "BEFORE", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE",
            "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN", "CONDITION",
            "CONSTRAINT", "CONVERT", "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME",
            "CURRENT_TIMESTAMP", "CURRENT_USER", "DATABASE", "DATABASES", "DAY_HOUR",
            "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DEFAULT",
            "DELETE", "DESC", "DESCRIBE", "DETERMINISTIC", "DISTINCT", "DISTINCTROW",
            "DIV", "DOUBLE", "DROP", "DUAL", "ELSE", "ELSEIF", "EXISTS", "EXPLAIN", "FALSE",
            "FLOAT", "FLOAT4", "FLOAT8", "FOR", "FORCE", "FROM", "GROUP", "HAVING", "HIGH_PRIORITY",
            "IF", "IGNORE", "IN", "INDEX", "INNER", "INSERT", "INT", "INT1", "INT2", "INT3",
            "INT4", "INT8", "INTEGER", "INTERVAL", "INTO", "IS", "JOIN", "KEY", "KEYS",
            "LEADING", "LEFT", "LIKE", "LIMIT", "LINES", "LOAD", "LOCK", "LONG", "LONGBLOB",
            "LONGTEXT", "LOW_PRIORITY", "MATCH", "MAXVALUE", "MEDIUMBLOB", "MEDIUMINT",
            "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES",
            "NATURAL", "NOT", "NULL", "NUMERIC", "ON", "OPTIMIZE", "OPTION", "OR", "ORDER",
            "OUTER", "PARTITION", "PRECISION", "PRIMARY", "RANGE", "READ", "REGEXP",
            "RELEASE", "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESTRICT", "RETURN", "RIGHT",
            "RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT", "SET", "SHOW",
            "SMALLINT", "SQL", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT",
            "STARTING", "STORED", "STRAIGHT_JOIN", "TABLE", "TERMINATED", "THEN", "TINYBLOB",
            "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRUE", "UNION", "UNIQUE", "UNLOCK",
            "UNSIGNED", "UPDATE", "USAGE", "USE", "USING", "UTC_DATE", "UTC_TIME",
            "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER", "VARYING",
            "VIRTUAL", "WHEN", "WHERE", "WITH", "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL"
    };

    /**
     * Implementation details.
     *
     * @param dataType parameter value
     * @param defaultValue parameter value
     * @return the operation result
     */
    public static String buildDorisDefaultClause(String dataType, String defaultValue) {
        if (StringUtils.isBlank(defaultValue) || StringUtils.isBlank(dataType)) {
            return "";
        }

        String type = dataType.trim().toUpperCase();
        String def = defaultValue.trim();

        boolean isNumeric = def.matches("^-?\\d+(\\.\\d+)?$");
        boolean isQuoted = def.matches("^'.*'$");

        // Implementation details.
        if (type.matches(".*(TINYINT|SMALLINT|INT|BIGINT|LARGEINT|FLOAT|DOUBLE|DECIMAL|FLOAT|CHAR|VARCHAR|DATE|DATETIME|BOOLEAN).*")) {
            if (!isQuoted && isNumeric) {
                return " DEFAULT '" + def + "'";
            } else if (isQuoted) {
                return " DEFAULT " + def;
            }
        }
        return ""; // Implementation details.
    }

    public static String escapeReservedKeyword(String colName) {
        if (colName == null || colName.isEmpty()) {
            return colName;
        }
        for (String reserved : DORIS_RESERVED_WORDS) {
            if (reserved.equalsIgnoreCase(colName)) {
                return "`" + colName + "`";
            }
        }
        return colName;
    }

    public static String generateColumnSQLDORIS(String columnType, String columnLength, String columnScale, int maxLength, int maxScale) {
        StringBuilder sql = new StringBuilder(columnType);

        // Implementation details.
        if (columnType.equalsIgnoreCase("DECIMAL") || columnType.equalsIgnoreCase("FLOAT")) {
            if (StringUtils.isNotEmpty(columnLength)) {
                int length = Integer.parseInt(columnLength);
                // Implementation details.
                if (length > maxLength) {
                    length = maxLength;
                }
                sql.append("(").append(length);

                // Implementation details.
                if (columnType.equalsIgnoreCase("DECIMAL") && StringUtils.isNotEmpty(columnScale)) {
                    int scale = Integer.parseInt(columnScale);
                    // Implementation details.
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
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return null;
    }

    @Override
    public String tables(String dbName) {
        return "select tables.name AS TABLENAME, CAST(ep.value AS NVARCHAR(128)) AS TABLECOMMENT " +
                "from sys.tables tables LEFT JOIN sys.extended_properties ep ON ep.major_id = tables.object_id AND ep.minor_id = 0";
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return "select tables.name AS TABLENAME, CAST(ep.value AS NVARCHAR(128)) AS TABLECOMMENT " +
                "from sys.tables tables LEFT JOIN sys.extended_properties ep ON ep.major_id = tables.object_id AND ep.minor_id = 0";
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        // Retrieve the required data.
        if (columns == null || columns.isEmpty()) {
            return "SELECT * FROM " + tableName;
        }

        // Retrieve the required data.
        String fields = columns.stream()
                .map(DbColumn::getColName)
                .collect(Collectors.joining(", "));

        // Handle JDBC SQL execution.
        return "SELECT " + fields + " FROM " + dbQueryProperty.getDbName() + "." + dbQueryProperty.getSid() + "." + tableName;
    }


    private static String getOrderByPart(String sql) {
        String loweredString = sql.toLowerCase();
        int orderByIndex = loweredString.indexOf("order by");
        if (orderByIndex != -1) {
            return sql.substring(orderByIndex);
        } else {
            return "";
        }
    }

    @Override
    public String buildPaginationSql(String originalSql, long offset, long count) {
        StringBuilder pagingBuilder = new StringBuilder();
        String orderby = getOrderByPart(originalSql);
        String distinctStr = "";

        String loweredString = originalSql.toLowerCase();
        String sqlPartString = originalSql;
        if (loweredString.trim().startsWith("select")) {
            int index = 6;
            if (loweredString.startsWith("select distinct")) {
                distinctStr = "DISTINCT ";
                index = 15;
            }
            sqlPartString = sqlPartString.substring(index);
        }
        pagingBuilder.append(sqlPartString);

        // if no ORDER BY is specified use fake ORDER BY field to avoid errors
        if (StringUtils.isEmpty(orderby)) {
            orderby = "ORDER BY CURRENT_TIMESTAMP";
        }
        StringBuilder sql = new StringBuilder();
        sql.append("WITH selectTemp AS (SELECT ").append(distinctStr).append("TOP 100 PERCENT ")
                .append(" ROW_NUMBER() OVER (").append(orderby).append(") as __row_number__, ").append(pagingBuilder)
                .append(") SELECT * FROM selectTemp WHERE __row_number__ BETWEEN ")
                // Implementation details.
                .append(offset + 1)
                .append(" AND ")
                .append(offset + count).append(" ORDER BY __row_number__");
        return sql.toString();
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return "SELECT SUM(size/128.0) AS \"usedSizeMb\" FROM sys.master_files WHERE DB_NAME(database_id) = '" + dbName + "'";
    }

    @Override
    public String getDbName() {
        return "SELECT DB_NAME() AS \"databaseName\"";
    }

    @Override
    public String getDbName(DbName dbNameVO) {
        int level = dbNameVO == null ? 1 : dbNameVO.getLevel() + 1;

        if (level == 1) {
            // Handle database and data source configuration.
            return "SELECT name AS DBNAME, 2 AS TOTALLEVELS " +
                    "FROM sys.databases " +
                    "WHERE name NOT IN ('master','tempdb','model','msdb') " +
                    "ORDER BY name";
        } else if (level == 2) {
            // Handle database and data source configuration.
            String dbName = dbNameVO.getDbName();
            if (dbName == null || dbName.trim().isEmpty()) {
                throw new IllegalArgumentException("SQLServer level=2 需要上级 dbName");
            }
            return "SELECT name AS DBNAME,  2 AS TOTALLEVELS " +
                    "FROM [" + dbName + "].sys.schemas " +
                    "WHERE principal_id <> 1 " +
                    "ORDER BY name";
        }

        throw new UnsupportedOperationException("SQLServer 仅支持 1~2 层级");
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        String sql = "MERGE INTO {tableName} USING (SELECT COUNT(1) count FROM {tableName}  WHERE {where}) c ON (c.count > 0) WHEN MATCHED THEN UPDATE SET {setValue} WHERE {where} WHEN NOT MATCHED THEN INSERT ({tableFieldName}) VALUES ({tableFieldValue})";
        sql = StringUtils
                .replace(sql, "{tableName}", tableName)
                .replace("{where}", where)
                .replace("{tableFieldName}", tableFieldName)
                .replace("{tableFieldValue}", tableFieldValue)
                .replace("{setValue}", setValue);
        return sql;
    }

    @Override
    public RowMapper<DbColumn> columnMapper() {
        return (ResultSet rs, int rowNum) -> {
            DbColumn entity = new DbColumn();
            entity.setColName(rs.getString("COLNAME"));
            entity.setDataType(rs.getString("DATATYPE"));
            entity.setDataLength(rs.getString("DATALENGTH"));
            entity.setDataPrecision(rs.getString("DATAPRECISION"));
            entity.setDataScale(rs.getString("DATASCALE"));
            entity.setColKey("1".equals(rs.getString("COLKEY")) ? true : false);
            entity.setNullable("1".equals(rs.getString("NULLABLE")) ? true : false);
            entity.setColPosition(rs.getInt("COLPOSITION"));
            entity.setDataDefault(rs.getString("DATADEFAULT"));
            entity.setColComment(rs.getString("COLCOMMENT"));
            return entity;
        };
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
    public String getTableName(DbQueryProperty property, String tableName) {
        return property.getDbName() + "." + property.getSid() + "." + tableName;
    }

    @Override
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(trainToJdbcUrl(dbQueryProperty), dbQueryProperty.getUsername(),
                    dbQueryProperty.getPassword());

            // Retrieve the required data.
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT 1")) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DataQueryException("db.error.connection.retry", "数据库连接失败，请稍后重试");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new DataQueryException("db.error.close.connection", "关闭数据库连接出错");
                }
            }
        }
        return false;
    }

    @Override
    public String updateTableComment(DbQueryProperty dbQueryProperty, String tableName, String tableComment) {
        String schema = StringUtils.isNotEmpty(dbQueryProperty.getSid()) ? dbQueryProperty.getSid() : "dbo";
        return "EXEC sys.sp_updateextendedproperty @name = N'MS_Description', @value = N'" + DatabaseUtil.escapeSingleQuotes(tableComment) + "', @level0type = N'SCHEMA', @level0name = N'" + schema + "', @level1type = N'TABLE', @level1name = N'" + tableName + "'";
    }

    // ... existing code ...
    @Override
    public String dropColumn(DbQueryProperty dbQueryProperty, String tableName, String colName) {
        String fullTableName = getTableName(dbQueryProperty, tableName);
        return "ALTER TABLE " + fullTableName + " DROP COLUMN " + colName;
    }

    @Override
    public List<String> modifyColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column) {
        List<String> sqlList = new ArrayList<>();
        String fullTableName = getTableName(dbQueryProperty, tableName);

        if (Boolean.TRUE.equals(column.getColKey())) {
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(fullTableName).append(" ADD CONSTRAINT PK_").append(tableName).append("_").append(column.getColName());
            sql.append(" PRIMARY KEY (").append(column.getColName()).append(")");
            sqlList.add(sql.toString());
        } else {
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(fullTableName).append(" ALTER COLUMN ");
            sql.append(column.getColName()).append(" ");

            String columnType = column.getDataType().toUpperCase();
            switch (columnType) {
                case "VARCHAR":
                case "VARCHAR2":
                    sql.append("VARCHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(MAX)");
                    }
                    break;
                case "CHAR":
                    sql.append("CHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    }
                    break;
                case "TEXT":
                    sql.append("TEXT");
                    break;
                case "INT":
                case "INTEGER":
                    sql.append("INT");
                    break;
                case "BIGINT":
                    sql.append("BIGINT");
                    break;
                case "TINYINT":
                    sql.append("TINYINT");
                    break;
                case "DECIMAL":
                    sql.append("DECIMAL");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength());
                        if (StringUtils.isNotEmpty(column.getDataScale())) {
                            sql.append(", ").append(column.getDataScale());
                        }
                        sql.append(")");
                    }
                    break;
                case "FLOAT":
                    sql.append("FLOAT");
                    break;
                case "DOUBLE":
                    sql.append("FLOAT");
                    break;
                case "DATE":
                    sql.append("DATE");
                    break;
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

            sqlList.add(sql.toString());
        }

        if (StringUtils.isNotEmpty(column.getDataDefault())) {
            String defaultSql = "ALTER TABLE " + fullTableName + " ADD CONSTRAINT DF_" + tableName + "_" + column.getColName()
                    + " DEFAULT " + column.getDataDefault() + " FOR " + column.getColName();
            sqlList.add(defaultSql);
        }

        if (StringUtils.isNotEmpty(column.getColComment())) {
            String schema = StringUtils.isNotEmpty(dbQueryProperty.getSid()) ? dbQueryProperty.getSid() : "dbo";
            sqlList.add("EXEC sys.sp_addextendedproperty @name = N'MS_Description', @value = N'"
                    + DatabaseUtil.escapeSingleQuotes(column.getColComment())
                    + "', @level0type = N'SCHEMA', @level0name = N'" + schema
                    + "', @level1type = N'TABLE', @level1name = N'" + tableName
                    + "', @level2type = N'COLUMN', @level2name = N'" + column.getColName() + "'");
        }

        return sqlList;
    }

    @Override
    public List<String> addColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column) {
        List<String> sqlList = new ArrayList<>();
        String fullTableName = getTableName(dbQueryProperty, tableName);

        if (Boolean.TRUE.equals(column.getColKey())) {
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(fullTableName).append(" ADD ");
            sql.append(column.getColName()).append(" ");

            String columnType = column.getDataType().toUpperCase();
            switch (columnType) {
                case "VARCHAR":
                case "VARCHAR2":
                    sql.append("VARCHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(MAX)");
                    }
                    break;
                case "CHAR":
                    sql.append("CHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    }
                    break;
                case "TEXT":
                    sql.append("TEXT");
                    break;
                case "INT":
                case "INTEGER":
                    sql.append("INT");
                    break;
                case "BIGINT":
                    sql.append("BIGINT");
                    break;
                case "TINYINT":
                    sql.append("TINYINT");
                    break;
                case "DECIMAL":
                    sql.append("DECIMAL");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength());
                        if (StringUtils.isNotEmpty(column.getDataScale())) {
                            sql.append(", ").append(column.getDataScale());
                        }
                        sql.append(")");
                    }
                    break;
                case "FLOAT":
                    sql.append("FLOAT");
                    break;
                case "DOUBLE":
                    sql.append("FLOAT");
                    break;
                case "DATE":
                    sql.append("DATE");
                    break;
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
            }

            sqlList.add(sql.toString());

            sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(fullTableName).append(" ADD CONSTRAINT PK_").append(tableName).append("_").append(column.getColName());
            sql.append(" PRIMARY KEY (").append(column.getColName()).append(")");
            sqlList.add(sql.toString());
        } else {
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(fullTableName).append(" ADD ");
            sql.append(column.getColName()).append(" ");

            String columnType = column.getDataType().toUpperCase();
            switch (columnType) {
                case "VARCHAR":
                case "VARCHAR2":
                    sql.append("VARCHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(MAX)");
                    }
                    break;
                case "CHAR":
                    sql.append("CHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    }
                    break;
                case "TEXT":
                    sql.append("TEXT");
                    break;
                case "INT":
                case "INTEGER":
                    sql.append("INT");
                    break;
                case "BIGINT":
                    sql.append("BIGINT");
                    break;
                case "TINYINT":
                    sql.append("TINYINT");
                    break;
                case "DECIMAL":
                    sql.append("DECIMAL");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength());
                        if (StringUtils.isNotEmpty(column.getDataScale())) {
                            sql.append(", ").append(column.getDataScale());
                        }
                        sql.append(")");
                    }
                    break;
                case "FLOAT":
                    sql.append("FLOAT");
                    break;
                case "DOUBLE":
                    sql.append("FLOAT");
                    break;
                case "DATE":
                    sql.append("DATE");
                    break;
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
            }

            sqlList.add(sql.toString());
        }

        if (StringUtils.isNotEmpty(column.getColComment())) {
            String schema = StringUtils.isNotEmpty(dbQueryProperty.getSid()) ? dbQueryProperty.getSid() : "dbo";
            sqlList.add("EXEC sys.sp_addextendedproperty @name = N'MS_Description', @value = N'"
                    + DatabaseUtil.escapeSingleQuotes(column.getColComment())
                    + "', @level0type = N'SCHEMA', @level0name = N'" + schema
                    + "', @level1type = N'TABLE', @level1name = N'" + tableName
                    + "', @level2type = N'COLUMN', @level2name = N'" + column.getColName() + "'");
        }

        return sqlList;
    }
}
