package tech.qiantong.qdata.common.database.dialect;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.utils.DatabaseUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DORIS 数据库方言
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class DorisDialect extends AbstractDbDialect {


    // 定义一个包含常见DORIS保留关键字的集合（全部转换为大写便于比较）
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
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        List<String> sqlList = new ArrayList<>();
        List<String> primaryKeys = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE ").append(tableName).append(" (\n");

        for (DbColumn column : dbColumnList) {
            String columnType = column.getDataType();
            String colName = column.getColName();

            sql.append("  ").append(this.escapeReservedKeyword(colName)).append(" ");

            // 映射 Doris 支持的数据类型
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
                    sql.append("VARCHAR(255)"); // fallback 处理
                    break;
            }

            // NOT NULL
            if (!column.getNullable()) {
                sql.append(" NOT NULL");
            }

            String columnTypeResolved = sql.substring(sql.lastIndexOf(" ") + 1); // 获取当前已拼接的数据类型
            String defaultClause = buildDorisDefaultClause(columnTypeResolved, column.getDataDefault());
            sql.append(defaultClause);

//
//            // 默认值（Doris 不允许函数类默认值）
//            if (StringUtils.isNotEmpty(column.getDataDefault()) &&
//                    column.getDataDefault().matches("^[0-9'.-]+$")) {
//                sql.append(" DEFAULT ").append(column.getDataDefault());
//            }

            // 注释
            if (StringUtils.isNotEmpty(column.getColComment())) {
                sql.append(" COMMENT '").append(DatabaseUtil.escapeSingleQuotes(column.getColComment())).append("'");
            }

            if (Boolean.TRUE.equals(column.getColKey())) {
                primaryKeys.add(colName);
            }

            sql.append(",\n");
        }

        // 去掉最后一个逗号
        sql.setLength(sql.length() - 2);
        sql.append("\n)");

        // Doris 必须指定 KEY 类型
        if (!primaryKeys.isEmpty()) {
            sql.append("\nUNIQUE KEY (");
            for (String pk : primaryKeys) {
                sql.append("`").append(pk).append("`, ");
            }
            sql.setLength(sql.length() - 2);
            sql.append(")");
        } else {
            // 无主键则用第一列作 DUPLICATE KEY
            sql.append("\nDUPLICATE KEY (`").append(dbColumnList.get(0).getColName()).append("`)");
        }

        // 分桶策略（必需）
        sql.append("\nDISTRIBUTED BY HASH(`").append(dbColumnList.get(0).getColName()).append("`) BUCKETS AUTO");

        // 表属性（含表注释）
        sql.append("\nPROPERTIES (\n");
        sql.append("  \"replication_num\" = \"1\"");
        sql.append("\n)");

        sqlList.add(sql.toString());
        //表注释
        sqlList.add("ALTER TABLE " + tableName + " MODIFY COMMENT '" + tableComment + "'");
        return sqlList;
    }

    /**
     * 构造 Doris 合法的 DEFAULT 子句（仅允许合法字面量，防止建表失败）
     *
     * @param dataType     字段类型，如 VARCHAR、INT、DECIMAL(10,2) 等
     * @param defaultValue 默认值，如 'abc'、0、1.23 等
     * @return 若合法则返回 DEFAULT xxx 子句，否则返回空字符串
     */
    public static String buildDorisDefaultClause(String dataType, String defaultValue) {
        if (StringUtils.isBlank(defaultValue) || StringUtils.isBlank(dataType)) {
            return "";
        }

        String type = dataType.trim().toUpperCase();
        String def = defaultValue.trim();

        boolean isNumeric = def.matches("^-?\\d+(\\.\\d+)?$");
        boolean isQuoted = def.matches("^'.*'$");

        // CHAR / VARCHAR 必须用引号包裹字符串默认值
        if (type.contains("CHAR") || type.contains("TEXT")) {
            // 没有引号就加上
            if (!isQuoted && isNumeric) {
                return " DEFAULT '" + def + "'";
            } else if (isQuoted) {
                return " DEFAULT " + def;
            }
            return ""; // 其他不合法情况过滤掉
        }

        // 数值类型禁止 DEFAULT
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
        return "SELECT table_name AS TABLENAME, table_comment AS TABLECOMMENT FROM information_schema.tables where table_schema = '" + dbQueryProperty.getDbName() + "' " +
                "  AND table_type = 'BASE TABLE'";
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
        return null;
//        return "SELECT SUM(data_length) / 1024 / 1024 AS \"usedSizeMb\" FROM information_schema.tables   WHERE table_schema = '" + dbName + "' GROUP BY table_schema";
    }

    @Override
    public String getDbName() {
        return "SELECT DATABASE() AS \"databaseName\"";
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
}
