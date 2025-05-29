package tech.qiantong.qdata.common.database.dialect;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.utils.MD5Util;

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
            entity.setColName(rs.getString("COLNAME"));
            entity.setDataType(rs.getString("DATATYPE"));
            entity.setDataLength(rs.getString("DATALENGTH"));
            entity.setDataPrecision(rs.getString("DATAPRECISION"));
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
                "character_maximum_length AS DATALENGTH, numeric_precision AS DATAPRECISION, numeric_scale AS DATASCALE, column_key AS COLKEY, column_comment AS COLCOMMENT " +
                "from information_schema.columns where table_schema = '" + dbQueryProperty.getDbName() + "' and table_name = '" + tableName + "' order by ordinal_position ";
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '" + dbQueryProperty.getDbName() + "' AND table_name = '" + tableName + "';";
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
                    case "datetime":
                    case "DATETIME":
                        sql.append("DATETIME");
                        break;
                    case "timestamp":
                    case "TIMESTAMP":
                        sql.append("TIMESTAMP");
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
                }

                // 添加字段备注（COMMENT）
                if (StringUtils.isNotEmpty(column.getColComment())) {
                    sql.append(" COMMENT '").append(MD5Util.escapeSingleQuotes(column.getColComment())).append("'");
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
                sql.append("COMMENT='").append(MD5Util.escapeSingleQuotes(tableComment));
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
        return "SELECT " + fields + " FROM " +dbQueryProperty.getDbName()+"."+ tableName;
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
