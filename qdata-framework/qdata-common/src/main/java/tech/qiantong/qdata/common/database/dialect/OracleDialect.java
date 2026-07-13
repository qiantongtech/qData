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
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Oracle Oracle11g and below database dialect
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class OracleDialect extends AbstractDbDialect {

    @Override
    public String columns(String dbName, String tableName) {
        if (StringUtils.isNotBlank(dbName)) {
            return "SELECT\n" +
                    "\tc.table_name,\n" +
                    "\tc.column_name AS colName,\n" +
                    "\tc.data_type AS DATATYPE,\n" +
                    "\tc.data_length AS DATALENGTH,\n" +
                    "\tc.data_precision AS DATAPRECISION,\n" +
                    "\tc.data_scale AS DATASCALE,\n" +
                    "\tc.nullable AS NULLABLE,\n" +
                    "\tc.column_id AS COLPOSITION,\n" +
                    "\tc.data_default AS DATADEFAULT,\n" +
                    "\tcm.comments AS COLCOMMENT,\n" +
                    "CASE WHEN t.column_name IS NULL THEN 0 ELSE 1 END AS COLKEY " +
                    "FROM\n" +
                    "\tall_tab_columns c\n" +
                    "\tLEFT JOIN all_col_comments cm ON cm.OWNER = '" + dbName + "' AND c.table_name = cm.table_name AND c.column_name = cm.column_name\n" +
                    "\tLEFT JOIN ( " +
                    "SELECT a.table_name, a.column_name FROM all_constraints b JOIN all_cons_columns a ON b.owner = a.owner AND b.constraint_name = a.constraint_name\n" +
                    "WHERE b.owner ='" + dbName + "' AND b.constraint_type = 'P' AND b.table_name ='" + tableName + "' " +
                    ") t on t.table_name = c.table_name and c.column_name = t.column_name " +
                    "WHERE\n" +
                    "\t c.OWNER = '" + dbName + "' \n" +
                    "\tAND c.Table_Name = '" + tableName + "'";
        } else {
            return "select columns.column_name AS colName, columns.data_type AS DATATYPE, columns.data_length AS DATALENGTH, columns.data_precision AS DATAPRECISION, " +
                    "columns.data_scale AS DATASCALE, columns.nullable AS NULLABLE, columns.column_id AS COLPOSITION, columns.data_default AS DATADEFAULT, comments.comments AS COLCOMMENT," +
                    "case when t.column_name is null then 0 else 1 end as COLKEY " +
                    "from sys.user_tab_columns columns LEFT JOIN sys.user_col_comments comments ON columns.table_name = comments.table_name AND columns.column_name = comments.column_name " +
                    "left join ( " +
                    "select col.column_name as column_name, con.table_name as table_name from user_constraints con, user_cons_columns col " +
                    "where con.constraint_name = col.constraint_name and con.constraint_type = 'P' " +
                    ") t on t.table_name = columns.table_name and columns.column_name = t.column_name " +
                    "where columns.table_name = UPPER('" + tableName + "') order by columns.column_id ";
        }
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        if (StringUtils.isNotBlank(dbQueryProperty.getDbName())) {
            return "SELECT\n" +
                    "\tc.table_name,\n" +
                    "\tc.column_name AS colName,\n" +
                    "\tc.data_type AS DATATYPE,\n" +
                    "\tc.data_length AS DATALENGTH,\n" +
                    "\tc.data_precision AS DATAPRECISION,\n" +
                    "\tc.data_scale AS DATASCALE,\n" +
                    "\tc.nullable AS NULLABLE,\n" +
                    "\tc.column_id AS COLPOSITION,\n" +
                    "\tc.data_default AS DATADEFAULT,\n" +
                    "\tcm.comments AS COLCOMMENT,\n" +
                    "CASE WHEN t.column_name IS NULL THEN 0 ELSE 1 END AS COLKEY " +
                    "FROM\n" +
                    "\tall_tab_columns c\n" +
                    "\tLEFT JOIN all_col_comments cm ON cm.OWNER = '" + dbQueryProperty.getDbName() + "' AND c.table_name = cm.table_name AND c.column_name = cm.column_name\n" +
                    "\tLEFT JOIN ( " +
                    "SELECT a.table_name, a.column_name FROM all_constraints b JOIN all_cons_columns a ON b.owner = a.owner AND b.constraint_name = a.constraint_name\n" +
                    "WHERE b.owner ='" + dbQueryProperty.getDbName() + "' AND b.constraint_type = 'P' AND b.table_name ='" + tableName + "' " +
                    ") t on t.table_name = c.table_name and c.column_name = t.column_name " +
                    "WHERE\n" +
                    "\t c.OWNER = '" + dbQueryProperty.getDbName() + "' \n" +
                    "\tAND c.Table_Name = '" + tableName + "'";
        } else {
            return "select columns.column_name AS colName, columns.data_type AS DATATYPE, columns.data_length AS DATALENGTH, columns.data_precision AS DATAPRECISION, " +
                    "columns.data_scale AS DATASCALE, columns.nullable AS NULLABLE, columns.column_id AS COLPOSITION, columns.data_default AS DATADEFAULT, comments.comments AS COLCOMMENT," +
                    "case when t.column_name is null then 0 else 1 end as COLKEY " +
                    "from sys.user_tab_columns columns LEFT JOIN sys.user_col_comments comments ON columns.table_name = comments.table_name AND columns.column_name = comments.column_name " +
                    "left join ( " +
                    "select col.column_name as column_name, con.table_name as table_name from user_constraints con, user_cons_columns col " +
                    "where con.constraint_name = col.constraint_name and con.constraint_type = 'P' " +
                    ") t on t.table_name = columns.table_name and columns.column_name = t.column_name " +
                    "where columns.table_name = UPPER('" + tableName + "') order by columns.column_id ";
        }
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(*) FROM all_tables WHERE owner = '" + dbQueryProperty.getDbName() + "' AND table_name = '" + tableName.toUpperCase() + "'";
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
        String dbName = dbQueryProperty.getDbName();

        if (StringUtils.isNotEmpty(dbName)) {
            tableName = dbName + "." + tableName;
        }

        List<String> sqlList = generateOracleCreateSql(tableName, tableComment, dbColumnList);

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
            // Mapping the data types supported by Doris
            switch (columnType.toUpperCase()) {
                case "VARCHAR2":
                case "NVARCHAR2":
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
                case "LONG":
                case "CLOB":
                    sql.append("STRING");
                    columnTypeResolved = "STRING";
                    break;
                case "NUMBER":
                    //NUMBER(no parameters) → DOUBLE
                    if (StringUtils.isBlank(column.getDataLength()) && StringUtils.isBlank(column.getDataPrecision())) {
                        sql.append("DOUBLE");
                        columnTypeResolved = "DOUBLE";
                        break;
                    }
                    //Determine whether there are decimal places
                    if (StringUtils.isBlank(column.getDataScale())) {
                        Integer dataLength = Integer.parseInt(column.getDataLength());
                        if (dataLength < 3) {
                            sql.append("TINYINT");
                            columnTypeResolved = "TINYINT";
                        } else if (dataLength < 5) {
                            sql.append("SMALLINT");
                            columnTypeResolved = "SMALLINT";
                        } else if (dataLength < 10) {
                            sql.append("INT");
                            columnTypeResolved = "INT";
                        } else if (dataLength < 19) {
                            sql.append("BIGINT");
                            columnTypeResolved = "BIGINT";
                        } else if (dataLength <= 38) {
                            sql.append("LARGEINT");
                            columnTypeResolved = "LARGEINT";
                        }
                    } else {
                        sql.append(generateColumnSQLDORIS("DECIMAL", column.getDataPrecision(), column.getDataScale(), 65, 30));
                        columnTypeResolved = "DECIMAL";
                    }
                    break;
                case "BINARY_FLOAT":
                    sql.append("FLOAT");
                    columnTypeResolved = "FLOAT";
                    break;
                case "BINARY_DOUBLE":
                    sql.append("DOUBLE");
                    columnTypeResolved = "DOUBLE";
                    break;
                case "DATE":
                    sql.append("DATE");
                    columnTypeResolved = "DATE";
                    break;
                case "TIMESTAMP":
                case "TIMESTAMP WITH TIME ZONE":
                    sql.append("DATETIME");
                    columnTypeResolved = "DATETIME";
                    break;
                default:
                    sql.append("VARCHAR(255)"); // fallback processing
                    columnTypeResolved = "VARCHAR";
                    break;
            }
            // NOT NULL
            if (!column.getNullable()) {
                sql.append(" NOT NULL");
            }

            String defaultClause = buildDorisDefaultClause(columnTypeResolved, column.getDataDefault());
            sql.append(defaultClause);

            // Comment
            if (StringUtils.isNotEmpty(column.getColComment())) {
                sql.append(" COMMENT '").append(DatabaseUtil.escapeSingleQuotes(column.getColComment())).append("'");
            }

            if (Boolean.TRUE.equals(column.getColKey())) {
                primaryKeys.add(colName);
            }

            sql.append(",\n");
        }

        // Remove the last comma
        sql.setLength(sql.length() - 2);
        sql.append("\n)");

        // Doris must specify the KEY type
        if (!primaryKeys.isEmpty()) {
            sql.append("\nUNIQUE KEY (");
            for (String pk : primaryKeys) {
                sql.append("`").append(pk).append("`, ");
            }
            sql.setLength(sql.length() - 2);
            sql.append(")");
        } else {
            // If there is no primary key, use the first column as DUPLICATE KEY
            sql.append("\nDUPLICATE KEY (`").append(dbColumnList.get(0).getColName()).append("`)");
        }

        //Determine whether to add a partition
        if (StringUtils.isNotBlank(partitionRule)) {
            sql.append("\n").append(partitionRule);
        }

        // Bucketing strategy (required)
        if (StringUtils.isBlank(bucketRule)) {
            sql.append("\nDISTRIBUTED BY HASH(`").append(dbColumnList.get(0).getColName()).append("`) BUCKETS AUTO");
        } else {
            sql.append("\n").append(bucketRule);
        }

        // Table properties (including table comments)
        sql.append("\nPROPERTIES (\n");
        sql.append("  \"replication_num\" = \"" + replica + "\"");
        sql.append("\n)");
        sqlList.add(sql.toString());
        //Table annotation
        sqlList.add("ALTER TABLE " + tableName + " MODIFY COMMENT '" + tableComment + "'");
        return sqlList;
    }

    // Define a set containing common DORIS reserved keywords (all converted to uppercase for easier comparison)
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
     * Construct a legal DEFAULT clause for Doris (only legal literals are allowed to prevent table creation failure)
     *
     * @param dataType field type, such as VARCHAR, INT, DECIMAL(10,2), etc.
     * @param defaultValue default value, such as 'abc', 0, 1.23, etc.
     * @return If legal, return the DEFAULT xxx clause, otherwise return an empty string
     */
    public static String buildDorisDefaultClause(String dataType, String defaultValue) {
        if (StringUtils.isBlank(defaultValue) || StringUtils.isBlank(dataType)) {
            return "";
        }

        String type = dataType.trim().toUpperCase();
        String def = defaultValue.trim();

        boolean isNumeric = def.matches("^-?\\d+(\\.\\d+)?$");
        boolean isQuoted = def.matches("^'.*'$");

        // Default values cannot be added to numeric types other than the following
        if (type.matches(".*(TINYINT|SMALLINT|INT|BIGINT|LARGEINT|FLOAT|DOUBLE|DECIMAL|FLOAT|CHAR|VARCHAR|DATE|DATETIME|BOOLEAN).*")) {
            if (!isQuoted && isNumeric) {
                return " DEFAULT '" + def + "'";
            } else if (isQuoted) {
                return " DEFAULT " + def;
            }
        }
        return ""; // Other illegal situations are filtered out
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
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return null;
    }

    private List<String> generateOracleCreateSql(String tableName, String tableComment, List<DbColumn> columns) {
        List<String> sqlList = new ArrayList<>();
        StringBuilder createSql = new StringBuilder();

        createSql.append("CREATE TABLE ").append(tableName).append(" (");
        List<String> pkList = new ArrayList<>();
        for (DbColumn col : columns) {
            createSql.append("\n  ").append(col.getColName()).append(" ");
            createSql.append(mapOracleColumnType(col));

            if (!col.getNullable()) {
                String columnType = col.getDataType();
                if (isStringTypeSwitchNullableFlag(columnType)) {
                    createSql.append(" NOT NULL");
                }
            }
            if (tech.qiantong.qdata.common.utils.StringUtils.hasText(col.getDataDefault())) {
                createSql.append(" DEFAULT ").append(col.getDataDefault());
//                String columnType = col.getDataType();
//                if (isStringTypeSwitchDEFAULT(columnType)) {
//                    createSql.append(" DEFAULT '").append(DatabaseUtil.escapeSingleQuotes(col.getDataDefault())).append("'");
//                } else {
//                    createSql.append(" DEFAULT ").append(col.getDataDefault());
//                }
            }
            if (col.getColKey()) {
                pkList.add(col.getColName());
            }
            createSql.append(",");
        }
        // Remove commas
        if (createSql.lastIndexOf(",") == createSql.length() - 1) {
            createSql.deleteCharAt(createSql.length() - 1);
        }
        // Primary key
        if (!pkList.isEmpty()) {
            createSql.append(",\n  PRIMARY KEY(");
            for (String pk : pkList) {
                createSql.append(pk).append(",");
            }
            createSql.deleteCharAt(createSql.length() - 1);
            createSql.append(")");
        }
        createSql.append("\n)");
        sqlList.add(createSql.toString());

        // Table annotation
        if (tech.qiantong.qdata.common.utils.StringUtils.hasText(tableComment)) {
            String tableCmt = "COMMENT ON TABLE " + tableName + " IS '" + DatabaseUtil.escapeSingleQuotes(tableComment) + "'";
            sqlList.add(tableCmt);
        }
        // Field annotation
        for (DbColumn col : columns) {
            if (tech.qiantong.qdata.common.utils.StringUtils.hasText(col.getColComment())) {
                String colCmt = "COMMENT ON COLUMN " + tableName + "." + col.getColName()
                        + " IS '" + DatabaseUtil.escapeSingleQuotes(col.getColComment()) + "'";
                sqlList.add(colCmt);
            }
        }

        return sqlList;
    }

    private static boolean isStringTypeSwitchDEFAULT(String columnType) {
        switch (columnType) {
            case "VARCHAR":
            case "VARCHAR2":
            case "CHAR":
            case "CLOB":
            case "TEXT":
                return true;
            default:
                return false;
        }
    }

    private static boolean isStringTypeSwitchNullableFlag(String columnType) {
        switch (columnType) {
            case "CLOB":
            case "BLOB":
            case "NCLOB":
            case "BFILE":
            case "NUMBER":
                return true;
            default:
                return false;
        }
    }

    private static String mapOracleColumnType(DbColumn col) {
        // Similar to Oracle
        String type = col.getDataType();
        Long length = DatabaseUtil.getStringToLong(col.getDataLength());
        Long scale = DatabaseUtil.getStringToLong(col.getDataScale());

        switch (type) {
            case "varchar":
            case "varchar2":
            case "VARCHAR":
            case "VARCHAR2":
                return "VARCHAR2(" + (length != null ? length : 255) + ")";
            case "CHAR":
                return "CHAR(" + (length != null ? length : 1) + ")";
            case "INT":
            case "INTEGER":
                String resultINT = generateColumnDefinitionOracle(
                        length
                        , 10
                        , false
                        , scale
                );
                return new StringBuilder("NUMBER").append(resultINT).toString();
            case "BIGINT":
                String resultBIGINT = generateColumnDefinitionOracle(
                        length
                        , 19
                        , false
                        , scale
                );
                return new StringBuilder("NUMBER").append(resultBIGINT).toString();
            case "DECIMAL":
                return "NUMBER(" + (length != null ? length : 10) + "," + (scale != null ? scale : 0) + ")";
            case "DATE":
                return "DATE";
            case "DATETIME":
                return "TIMESTAMP";
            case "TEXT":
            case "CLOB":
                return "CLOB";
            default:
                return type;
        }
    }

    /**
     * Generate SQL string for concatenation based on column length and scale
     *
     * @param columnLength column length (string representation)
     * @param maxLength The maximum value of the length limit (e.g. 38)
     * @param includeScale Whether to splice decimal places
     * @param columnScale The number of decimal places in the column (string representation, may be empty)
     * @return generated SQL string for concatenation
     */
    public static String generateColumnDefinitionOracle(Long columnLength, long maxLength, boolean includeScale, Long columnScale) {
        StringBuilder sql = new StringBuilder("");

        if (columnLength == null) {
            throw new UnsupportedOperationException("属性类型：格式错误，数字类型长度未填充");
        }

        // If columnLength is empty, maxLength is used as the default value
        long length = columnLength;

        if (length > maxLength) {
            length = maxLength;
        }

        // Splicing length
        sql.append("(").append(length);

        // Determine whether the number of decimal places needs to be spliced based on includeScale and columnScale
        if (includeScale && columnScale != 0) {
            sql.append(", ").append(columnScale);
        }

        sql.append(")");

        return sql.toString();
    }


    @Override
    public String tables(String dbName) {
        if (StringUtils.isNotBlank(dbName)) {
            return "SELECT DISTINCT t.TABLE_NAME AS TABLENAME,c.COMMENTS AS TABLECOMMENT FROM ALL_TAB_COMMENTS c JOIN ALL_TABLES t ON c.TABLE_NAME = t.TABLE_NAME WHERE t.OWNER = '" + dbName + "' AND c.OWNER = '" + dbName + "'";
        } else {
            return "select tables.table_name AS TABLENAME, comments.comments AS TABLECOMMENT from sys.user_tables tables " +
                    "LEFT JOIN sys.user_tab_comments comments ON tables.table_name = comments.table_name ";
        }
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        if (StringUtils.isNotBlank(dbQueryProperty.getDbName())) {
            return "SELECT DISTINCT t.TABLE_NAME AS TABLENAME,c.COMMENTS AS TABLECOMMENT FROM ALL_TAB_COMMENTS c JOIN ALL_TABLES t ON c.TABLE_NAME = t.TABLE_NAME WHERE t.OWNER = '" + dbQueryProperty.getDbName() + "' AND c.OWNER = '" + dbQueryProperty.getDbName() + "'";
        } else {
            return "select tables.table_name AS TABLENAME, comments.comments AS TABLECOMMENT from sys.user_tables tables " +
                    "LEFT JOIN sys.user_tab_comments comments ON tables.table_name = comments.table_name ";
        }
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        // If no fields are passed in, * will be used by default to query all fields.
        if (columns == null || columns.isEmpty()) {
            return "SELECT * FROM " + tableName;
        }

        // Get all field names based on the passed in DbColumn list, separated by commas
        String fields = columns.stream()
                .map(DbColumn::getColName)
                .collect(Collectors.joining(", "));

        // Construct the final SQL query statement
        return "SELECT " + fields + " FROM " + dbQueryProperty.getDbName() + "." + tableName;
    }

    @Override
    public String buildPaginationSql(String originalSql, long offset, long count) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM ( ");
        sqlBuilder.append(originalSql).append(" ) TMP WHERE ROWNUM <=").append((offset >= 1) ? (offset + count) : count);
        sqlBuilder.append(") WHERE ROW_ID > ").append(offset);
        return sqlBuilder.toString();
    }

    @Override
    public RowMapper<DbColumn> columnLongMapper() {
        return (ResultSet rs, int rowNum) -> {
            DbColumn entity = new DbColumn();
            entity.setDataDefault(rs.getString("DATADEFAULT"));
            return entity;
        };
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return "SELECT ROUND(SUM(bytes) / 1024 / 1024, 2) AS \"usedSizeMb\" FROM dba_segments WHERE owner = '" + dbName + "' GROUP BY owner";
    }

    @Override
    public String getDbName() {
        return "SELECT SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA')  AS \"databaseName\" FROM DUAL";
    }

    @Override
    public String getDbName(DbName dbName) {
        int level = dbName == null ? 1 : dbName.getLevel() + 1;
        // Only one level is used: database
        if (level == 1) {
            return "SELECT USERNAME AS DBNAME,1 AS TOTALLEVELS \n" +
                    "FROM ALL_USERS\n" +
                    "WHERE USERNAME NOT IN (\n" +
                    "  'SYS','SYSTEM','OUTLN','MDSYS','XDB','WMSYS','CTXSYS','DBSNMP',\n" +
                    "  'APPQOSSYS','OLAPSYS','OWBSYS','ORDSYS','ORDDATA','ORDPLUGINS',\n" +
                    "  'SI_INFORMTN_SCHEMA','ORACLE_OCM','SYSMAN','MDDATA','ANONYMOUS',\n" +
                    "  'XS$NULL','DIP','MGMT_VIEW','APEX_PUBLIC_USER'\n" +
                    ")\n" +
                    "ORDER BY DBNAME";
        }
        // No second layer
        throw new UnsupportedOperationException("Oracle11g only has one level");
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
            if (rs.getString("DATAPRECISION") != null) {
                entity.setDataLength(rs.getString("DATAPRECISION"));
            }
            entity.setDataScale(rs.getString("DATASCALE"));
            entity.setColKey("1".equals(rs.getString("COLKEY")));
            entity.setNullable("Y".equals(rs.getString("NULLABLE")));
            //long type, processed separately
            //entity.setDataDefault(rs.getString("DATADEFAULT"));
            entity.setColPosition(rs.getInt("COLPOSITION"));
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
    public String getFlinkCDCSQL(DbQueryProperty property, String flinkTableName, String tableName, String tableFieldName) {
        String sql = "CREATE TABLE ${flinkTableName} (${tableFieldName}) " +
                "WITH ( 'connector' = 'oracle-cdc'," +
                " 'hostname' = '${host}' ," +
                "'port' = '${port}' ," +
                "'username' = '${username}' ," +
                "'password' = '${password}'," +
                "'database-name' = '${sid}' ," +
                "'schema-name' = '${dbName}' ," +
                "'table-name' = '${tableName}' ," +
                "'scan.startup.mode' = 'initial' ," +
                "'scan.incremental.snapshot.enabled' = 'true'," +
                "'debezium.database.connection.adapter'='logminer'," +
                "'debezium.log.mining.strategy'='online_catalog'," +
                "'debezium.log.mining.continuous.mine'='true')";
        sql = StringUtils
                .replace(sql, "${flinkTableName}", flinkTableName)
                .replace("${tableName}", tableName)
                .replace("${host}", property.getHost())
                .replace("${tableFieldName}", tableFieldName)
                .replace("${port}", String.valueOf(property.getPort()))
                .replace("${dbName}", property.getDbName())
                .replace("${sid}", property.getSid().toUpperCase(Locale.ROOT))
                .replace("${username}", property.getUsername())
                .replace("${password}", property.getPassword());
        return sql;
    }

    @Override
    public String getFlinkSQL(DbQueryProperty property, String flinkTableName, String tableName, String tableFieldName) {
        String sql = "CREATE TABLE ${flinkTableName} (${tableFieldName}) " +
                "WITH ( 'connector' = 'jdbc'," +
                "'url' = 'jdbc:oracle:thin:@${host}:${port}:${sid}'," +
                "'table-name' = '${dbName}.${tableName}'," +
                "'username' = '${username}'," +
                "'password' = '${password}')";

        sql = StringUtils
                .replace(sql, "${flinkTableName}", flinkTableName)
                .replace("${tableName}", tableName)
                .replace("${host}", property.getHost())
                .replace("${tableFieldName}", tableFieldName)
                .replace("${port}", String.valueOf(property.getPort()))
                .replace("${dbName}", property.getDbName())
                .replace("${sid}", property.getSid())
                .replace("${username}", property.getUsername())
                .replace("${password}", property.getPassword());
        return sql;
    }

    @Override
    public String getFlinkSinkSQL(DbQueryProperty property, JSONObject config, String flinkTableName, String tableName, String tableFieldName) {
        String sql = "CREATE TABLE ${flinkTableName} (${tableFieldName}) " +
                "WITH ( 'connector' = 'jdbc'," +
                "'url' = 'jdbc:oracle:thin:@${host}:${port}:${sid}'," +
                "'table-name' = '${dbName}.${tableName}'," +
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
    public String trainToJdbcUrl(DbQueryProperty property) {
        String url = DbType.getDbType(property.getDbType()).getUrl();
        if (org.springframework.util.StringUtils.isEmpty(url)) {
            throw new DataQueryException("db.error.invalid.dbtype", "无效数据库类型");
        }
        url = url.replace("${host}", property.getHost());
        url = url.replace("${port}", String.valueOf(property.getPort()));
        url = url.replace("${sid}", property.getSid());
        return url;
    }

    @Override
    public String updateTableComment(DbQueryProperty dbQueryProperty, String tableName, String tableComment) {
        String fullTableName = getTableName(dbQueryProperty, tableName);
        return "COMMENT ON TABLE " + fullTableName + " IS '" + DatabaseUtil.escapeSingleQuotes(tableComment) + "'";
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

        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ").append(fullTableName).append(" MODIFY ");
        sql.append(column.getColName()).append(" ");
        sql.append(mapOracleColumnType(column));

        if (!column.getNullable()) {
            String columnType = column.getDataType();
            if (isStringTypeSwitchNullableFlag(columnType)) {
                sql.append(" NOT NULL");
            }
        }

        if (tech.qiantong.qdata.common.utils.StringUtils.hasText(column.getDataDefault())) {
            sql.append(" DEFAULT ").append(column.getDataDefault());
        }

        sqlList.add(sql.toString());

        if (tech.qiantong.qdata.common.utils.StringUtils.hasText(column.getColComment())) {
            sqlList.add("COMMENT ON COLUMN " + fullTableName + "." + column.getColName()
                    + " IS '" + DatabaseUtil.escapeSingleQuotes(column.getColComment()) + "'");
        }

        return sqlList;
    }

    @Override
    public List<String> addColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column) {
        List<String> sqlList = new ArrayList<>();
        String fullTableName = getTableName(dbQueryProperty, tableName);

        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ").append(fullTableName).append(" ADD ");
        sql.append(column.getColName()).append(" ");
        sql.append(mapOracleColumnType(column));

        if (!column.getNullable()) {
            String columnType = column.getDataType();
            if (isStringTypeSwitchNullableFlag(columnType)) {
                sql.append(" NOT NULL");
            }
        }

        if (tech.qiantong.qdata.common.utils.StringUtils.hasText(column.getDataDefault())) {
            sql.append(" DEFAULT ").append(column.getDataDefault());
        }

        sqlList.add(sql.toString());

        if (tech.qiantong.qdata.common.utils.StringUtils.hasText(column.getColComment())) {
            sqlList.add("COMMENT ON COLUMN " + fullTableName + "." + column.getColName()
                    + " IS '" + DatabaseUtil.escapeSingleQuotes(column.getColComment()) + "'");
        }

        return sqlList;
    }

    @Override
    public List<String> updateColKey(DbQueryProperty dbQueryProperty, String tableName, List<DbColumn> colKeyDbColumnList) {
        List<String> sqlList = new ArrayList<>();
        String fullTableName = getTableName(dbQueryProperty, tableName);

        // First delete the existing primary key constraint
        sqlList.add("ALTER TABLE " + fullTableName + " DROP PRIMARY KEY CASCADE");

        // If a new primary key field list is provided, add a new primary key constraint
        if (colKeyDbColumnList != null && !colKeyDbColumnList.isEmpty()) {
            StringBuilder addSql = new StringBuilder();
            addSql.append("ALTER TABLE ").append(fullTableName).append(" ADD CONSTRAINT ");

            // Generate constraint name: table name_primary key field combination
            StringBuilder constraintName = new StringBuilder();
            constraintName.append(tableName.toUpperCase());
            for (DbColumn col : colKeyDbColumnList) {
                constraintName.append("_").append(col.getColName().toUpperCase());
            }
            // Dameng constraint name length is limited to 128 characters
            String finalConstraintName = constraintName.length() > 128 ?
                    constraintName.substring(0, 128) : constraintName.toString();

            addSql.append(finalConstraintName);
            addSql.append(" PRIMARY KEY (");
            for (int i = 0; i < colKeyDbColumnList.size(); i++) {
                if (i > 0) {
                    addSql.append(", ");
                }
                addSql.append(colKeyDbColumnList.get(i).getColName());
            }
            addSql.append(")");
            sqlList.add(addSql.toString());
        }

        return sqlList;
    }

    @Override
    public String getColumnType(DbColumn column) {
        String columnType = mapOracleColumnType(column);
        if (columnType.indexOf("(") > 0) {
            return columnType.substring(0, columnType.lastIndexOf("("));
        }
        return columnType;
    }

    @Override
    public String getTableName(DbQueryProperty property, String tableName) {
        if (!org.springframework.util.StringUtils.isEmpty(property.getDbName())) {
            return "\"" + property.getDbName() + "\".\"" + tableName + "\"";
        }
        return tableName;
    }
}
