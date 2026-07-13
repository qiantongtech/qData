package tech.qiantong.qdata.common.database.dialect;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbName;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.utils.DatabaseUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DORIS database dialect
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class DorisDialect extends AbstractDbDialect {


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
            entity.setColKey(false);
            entity.setNullable("YES".equals(rs.getString("NULLABLE")));
            entity.setColPosition(rs.getInt("COLPOSITION"));
            entity.setDataDefault(rs.getString("DATADEFAULT"));
            entity.setColComment(rs.getString("COLCOMMENT"));
            return entity;
        };
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return "select column_name AS COLNAME" +
                ", ordinal_position AS COLPOSITION" +
                ", column_default AS DATADEFAULT" +
                ", is_nullable AS NULLABLE" +
                ", data_type AS DATATYPE" +
                ", character_maximum_length AS DATALENGTH" +
                ", numeric_precision AS DATAPRECISION" +
                ", numeric_scale AS DATASCALE" +
                ", column_comment AS COLCOMMENT " +
                "from information_schema.columns" +
                " where table_schema = '" + dbQueryProperty.getDbName() + "' and table_name = '" + tableName + "' order by ordinal_position ";
    }


    @Override
    public String getDbColumns(DbQueryProperty dbQueryProperty) {
        return "select " +
                "table_name              AS TABLENAME, " +
                "column_name AS COLNAME" +
                ", ordinal_position AS COLPOSITION" +
                ", column_default AS DATADEFAULT" +
                ", is_nullable AS NULLABLE" +
                ", data_type AS DATATYPE" +
                ", character_maximum_length AS DATALENGTH" +
                ", numeric_precision AS DATAPRECISION" +
                ", numeric_scale AS DATASCALE" +
                ", column_comment AS COLCOMMENT " +
                "from information_schema.columns" +
                " where table_schema = '" + dbQueryProperty.getDbName() + "' order by table_name, ordinal_position ";
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT " +
                "COUNT(*)" +
                " FROM information_schema.tables " +
                "WHERE table_schema = '" + dbQueryProperty.getDbName() + "' AND table_name = '" + tableName + "'" +
                "AND table_type = 'BASE TABLE';";
    }

    @Override
    public String buildTableNameByDbType(DbQueryProperty dbQueryProperty, String tableName) {
        if(StringUtils.isNotEmpty(dbQueryProperty.getDbName())){
            return dbQueryProperty.getDbName() + "." + tableName;
        }

        return tableName;
    }


    @Override
    public String getPkColumnNames(DbQueryProperty dbQueryProperty, String tableName) {
        return "SHOW CREATE TABLE " + dbQueryProperty.getDbName() + "." + tableName;
    }
    @Override
    public String getPkColumnNames(DbQueryProperty dbQueryProperty) {
        return "SHOW CREATE TABLE " + dbQueryProperty.getDbName() ;
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        List<String> sqlList = new ArrayList<>();
        List<String> primaryKeys = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE ").append(tableName).append(" (\n");

        for (DbColumn column : dbColumnList) {
            String columnType = column.getDataType();
            String colName = column.getColName();

            sql.append("  ").append(this.escapeReservedKeyword(colName)).append(" ");

            // Mapping the data types supported by Doris
            switch (columnType.toUpperCase()) {
                case "VARCHAR":
                case "VARCHAR2":
                    sql.append("VARCHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(255)");
                    }
                    break;
                case "CHAR":
                    sql.append("CHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(1)");
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
                    sql.append(generateColumnSQLDORIS("DECIMAL", column.getDataLength(), column.getDataScale(), 65, 30));
                    break;
                case "FLOAT":
                    sql.append("FLOAT");
                    break;
                case "DOUBLE":
                    sql.append("DOUBLE");
                    break;
                case "DATE":
                    sql.append("DATE");
                    break;
                case "DATETIME":
                case "TIMESTAMP":
                    sql.append("DATETIME");
                    break;
                default:
                    sql.append("VARCHAR(255)"); // fallback processing
                    break;
            }

            // NOT NULL
            if (!column.getNullable()) {
                sql.append(" NOT NULL");
            }

            String columnTypeResolved = sql.substring(sql.lastIndexOf(" ") + 1); // Get the currently spliced data type
            String defaultClause = buildDorisDefaultClause(columnTypeResolved, column.getDataDefault());
            sql.append(defaultClause);

//
// //Default value (Doris does not allow function class default values)
//            if (StringUtils.isNotEmpty(column.getDataDefault()) &&
//                    column.getDataDefault().matches("^[0-9'.-]+$")) {
//                sql.append(" DEFAULT ").append(column.getDataDefault());
//            }

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

        // Bucketing strategy (required)
        sql.append("\nDISTRIBUTED BY HASH(`").append(dbColumnList.get(0).getColName()).append("`) BUCKETS AUTO");

        // Table properties (including table comments)
        sql.append("\nPROPERTIES (\n");
        sql.append("  \"replication_num\" = \"1\"");
        sql.append("\n)");

        sqlList.add(sql.toString());
        //Table annotation
        sqlList.add("ALTER TABLE " + tableName + " MODIFY COMMENT '" + tableComment + "'");
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

            // Mapping the data types supported by Doris
            switch (columnType.toUpperCase()) {
                case "VARCHAR":
                case "VARCHAR2":
                    sql.append("VARCHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(255)");
                    }
                    break;
                case "CHAR":
                    sql.append("CHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(1)");
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
                    sql.append(generateColumnSQLDORIS("DECIMAL", column.getDataLength(), column.getDataScale(), 65, 30));
                    break;
                case "FLOAT":
                    sql.append("FLOAT");
                    break;
                case "DOUBLE":
                    sql.append("DOUBLE");
                    break;
                case "DATE":
                case "DATETIME":
                case "TIMESTAMP":
                    sql.append("DATETIME");
                    break;
                default:
                    sql.append("VARCHAR(255)"); // fallback processing
                    break;
            }

            // NOT NULL
            if (!column.getNullable()) {
                sql.append(" NOT NULL");
            }

            String columnTypeResolved = sql.substring(sql.lastIndexOf(" ") + 1); // Get the currently spliced data type
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

        // CHAR / VARCHAR string default values must be wrapped in quotes
        if (type.contains("CHAR") || type.contains("TEXT")) {
            // Add without quotation marks
            if (!isQuoted && isNumeric) {
                return " DEFAULT '" + def + "'";
            } else if (isQuoted) {
                return " DEFAULT " + def;
            }
            return ""; // Other illegal situations are filtered out
        }

        // Numeric type prohibition DEFAULT
        if (type.matches(".*(INT|BIGINT|TINYINT|DECIMAL|FLOAT|DOUBLE).*")) {
            return "";
        }

        return "";
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

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return null;
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
    public String tables(DbQueryProperty dbQueryProperty) {
        return "SELECT table_name AS TABLENAME, table_comment AS TABLECOMMENT FROM information_schema.tables where table_schema = '" + dbQueryProperty.getDbName() + "' " +
                "  AND table_type = 'BASE TABLE'";
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
        return null;
//        return "SELECT SUM(data_length) / 1024 / 1024 AS \"usedSizeMb\" FROM information_schema.tables   WHERE table_schema = '" + dbName + "' GROUP BY table_schema";
    }

    @Override
    public String getDbName() {
        return "SELECT DATABASE() AS \"databaseName\"";
    }


    @Override
    public String getDbName(DbName req) {
        int level = req == null ? 1 : req.getLevel() + 1;
        // Doris only has the Database layer by default, and the total level is 1
        if (level == 1) {
            return "SHOW DATABASES";
        }
        throw new UnsupportedOperationException("Doris 默认仅支持 level=1（Database 层）");
    }

    @Override
    public RowMapper<DbName> firstLevelMapper(int level) {
        return (rs, i) -> DbName.builder()
                .dbName(rs.getString(1))  // The first column of Doris is the Database name
                .level(1)
                .totalLevels(1)
                .build();
    }

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
                "WITH ( 'connector' = 'dm-cdc'," +
                " 'hostname' = '${host}' ," +
                "'port' = '${port}' ," +
                "'username' = '${username}' ," +
                "'password' = '${password}'," +
                "'database-name' = '${tableName}' ," +
                "'table-name' = '${dbName}' ," +
                "'server-time-zone' = 'Asia/Shanghai'," +
                "'scan.incremental.snapshot.enabled' = 'true'," +
                "'debezium.snapshot.mode'='latest-offset')";
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
    public String getFlinkSQL(DbQueryProperty property, String flinkTableName, String tableName, String tableFieldName) {
        String sql = "CREATE TABLE ${flinkTableName} (${tableFieldName}) " +
                "WITH ( 'connector' = 'jdbc'," +
                "'url' = 'jdbc:dm://${host}:${port}/${dbName}?STU&zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8&schema=${dbName}&serverTimezone=Asia/Shanghai'," +
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
    public String updateTableComment(DbQueryProperty dbQueryProperty, String tableName, String tableComment) {
        String fullTableName = getTableName(dbQueryProperty, tableName);
        return "ALTER TABLE " + fullTableName + " MODIFY COMMENT '" + DatabaseUtil.escapeSingleQuotes(tableComment) + "'";
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

        if (Boolean.TRUE.equals(column.getColKey())) {
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(fullTableName).append(" ADD UNIQUE KEY (");
            sql.append(escapeReservedKeyword(column.getColName())).append(")");
            sqlList.add(sql.toString());
        } else {
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(fullTableName).append(" MODIFY COLUMN ");
            sql.append(escapeReservedKeyword(column.getColName())).append(" ");

            String columnType = column.getDataType();
            switch (columnType.toUpperCase()) {
                case "VARCHAR":
                case "VARCHAR2":
                    sql.append("VARCHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(255)");
                    }
                    break;
                case "CHAR":
                    sql.append("CHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(1)");
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
                    sql.append(generateColumnSQLDORIS("DECIMAL", column.getDataLength(), column.getDataScale(), 65, 30));
                    break;
                case "FLOAT":
                    sql.append("FLOAT");
                    break;
                case "DOUBLE":
                    sql.append("DOUBLE");
                    break;
                case "DATE":
                    sql.append("DATE");
                    break;
                case "DATETIME":
                case "TIMESTAMP":
                    sql.append("DATETIME");
                    break;
                default:
                    sql.append("VARCHAR(255)");
                    break;
            }

            if (!column.getNullable()) {
                sql.append(" NOT NULL");
            }

            String columnTypeResolved = sql.substring(sql.lastIndexOf(" ") + 1);
            String defaultClause = buildDorisDefaultClause(columnTypeResolved, column.getDataDefault());
            sql.append(defaultClause);

            if (StringUtils.isNotEmpty(column.getColComment())) {
                sql.append(" COMMENT '").append(DatabaseUtil.escapeSingleQuotes(column.getColComment())).append("'");
            }

            sqlList.add(sql.toString());
        }

        return sqlList;
    }

    @Override
    public List<String> addColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column) {
        List<String> sqlList = new ArrayList<>();
        String fullTableName = getTableName(dbQueryProperty, tableName);

        if (Boolean.TRUE.equals(column.getColKey())) {
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(fullTableName).append(" ADD COLUMN ");
            sql.append(escapeReservedKeyword(column.getColName())).append(" ");

            String columnType = column.getDataType();
            switch (columnType.toUpperCase()) {
                case "VARCHAR":
                case "VARCHAR2":
                    sql.append("VARCHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(255)");
                    }
                    break;
                case "CHAR":
                    sql.append("CHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(1)");
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
                    sql.append(generateColumnSQLDORIS("DECIMAL", column.getDataLength(), column.getDataScale(), 65, 30));
                    break;
                case "FLOAT":
                    sql.append("FLOAT");
                    break;
                case "DOUBLE":
                    sql.append("DOUBLE");
                    break;
                case "DATE":
                    sql.append("DATE");
                    break;
                case "DATETIME":
                case "TIMESTAMP":
                    sql.append("DATETIME");
                    break;
                default:
                    sql.append("VARCHAR(255)");
                    break;
            }

            if (!column.getNullable()) {
                sql.append(" NOT NULL");
            }

            String columnTypeResolved = sql.substring(sql.lastIndexOf(" ") + 1);
            String defaultClause = buildDorisDefaultClause(columnTypeResolved, column.getDataDefault());
            sql.append(defaultClause);

            if (StringUtils.isNotEmpty(column.getColComment())) {
                sql.append(" COMMENT '").append(DatabaseUtil.escapeSingleQuotes(column.getColComment())).append("'");
            }

            sqlList.add(sql.toString());

            sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(fullTableName).append(" ADD UNIQUE KEY (");
            sql.append(escapeReservedKeyword(column.getColName())).append(")");
            sqlList.add(sql.toString());
        } else {
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(fullTableName).append(" ADD COLUMN ");
            sql.append(escapeReservedKeyword(column.getColName())).append(" ");

            String columnType = column.getDataType();
            switch (columnType.toUpperCase()) {
                case "VARCHAR":
                case "VARCHAR2":
                    sql.append("VARCHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(255)");
                    }
                    break;
                case "CHAR":
                    sql.append("CHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    } else {
                        sql.append("(1)");
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
                    sql.append(generateColumnSQLDORIS("DECIMAL", column.getDataLength(), column.getDataScale(), 65, 30));
                    break;
                case "FLOAT":
                    sql.append("FLOAT");
                    break;
                case "DOUBLE":
                    sql.append("DOUBLE");
                    break;
                case "DATE":
                    sql.append("DATE");
                    break;
                case "DATETIME":
                case "TIMESTAMP":
                    sql.append("DATETIME");
                    break;
                default:
                    sql.append("VARCHAR(255)");
                    break;
            }

            if (!column.getNullable()) {
                sql.append(" NOT NULL");
            }

            String columnTypeResolved = sql.substring(sql.lastIndexOf(" ") + 1);
            String defaultClause = buildDorisDefaultClause(columnTypeResolved, column.getDataDefault());
            sql.append(defaultClause);

            if (StringUtils.isNotEmpty(column.getColComment())) {
                sql.append(" COMMENT '").append(DatabaseUtil.escapeSingleQuotes(column.getColComment())).append("'");
            }

            sqlList.add(sql.toString());
        }

        return sqlList;
    }
}
