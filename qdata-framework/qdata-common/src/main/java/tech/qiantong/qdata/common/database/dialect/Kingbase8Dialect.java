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
 * Kingbase8 数据库方言
 *
 * @author QianTongDC
 * @date 2024-02-08
 */
public class Kingbase8Dialect extends AbstractDbDialect {

    @Override
    public String columns(String dbName, String tableName) {
        return "SELECT a.attname AS COLNAME,  " +
                " CASE " +
                "        WHEN t.typname = 'int2' THEN 'SMALLINT' " +
                "        WHEN t.typname = 'int4' THEN 'INTEGER' " +
                "        WHEN t.typname = 'int8' THEN 'BIGINT' " +
                "        WHEN t.typname = 'float4' THEN 'REAL' " +
                "        WHEN t.typname = 'float8' THEN 'DOUBLE PRECISION' " +
                "        WHEN t.typname = 'varchar' THEN 'VARCHAR' " +
                "        WHEN t.typname = 'bpchar' THEN 'CHAR' " +
                "        WHEN t.typname = 'text' THEN 'TEXT' " +
                "        ELSE t.typname  " +
                "    END AS DATATYPE," +
                "CASE WHEN t.typname IN ('varchar', 'char','bpchar',  'text') THEN a.atttypmod - 4 ELSE a.attlen END AS DATALENGTH, " +
                "CASE WHEN t.typname IN ('numeric', 'decimal', 'float4', 'float8') THEN (a.atttypmod - 4) >> 16 ELSE NULL END AS DATAPRECISION, " +
                "CASE WHEN t.typname IN ('numeric', 'decimal', 'float4', 'float8') THEN (a.atttypmod - 4) & 65535 ELSE NULL END AS DATASCALE, " +
                "CASE WHEN con.contype = 'p' THEN TRUE ELSE FALSE END AS COLKEY, " +
                "NOT a.attnotnull AS NULLABLE, a.attnum AS COLPOSITION, " +
                "regexp_replace(pg_get_expr(d.adbin, d.adrelid), '(::[a-zA-Z0-9_]+)+$', '') AS DATADEFAULT" +
                "col_description(a.attrelid, a.attnum) AS COLCOMMENT " +
                "FROM sys_attribute a " +
                "JOIN sys_class c ON a.attrelid = c.oid " +
                "JOIN sys_type t ON a.atttypid = t.oid " +
                "LEFT JOIN sys_constraint con ON con.conrelid = c.oid AND a.attnum = ANY(con.conkey) AND con.contype = 'p' " +
                "LEFT JOIN sys_attrdef d ON a.attrelid = d.adrelid AND a.attnum = d.adnum " +
                "WHERE (SELECT current_database()) =  '" + dbName + "'  " +
                "AND c.relname =  '" + tableName + "'  " +
                "AND c.relnamespace = (SELECT oid FROM sys_namespace WHERE nspname =  '" + "public" + "' ) " +
                "AND a.attnum > 0 " +  // 过滤掉系统隐藏列
                "ORDER BY a.attnum";
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT a.attname AS COLNAME," +
                " CASE " +
                "        WHEN t.typname = 'int2' THEN 'SMALLINT' " +
                "        WHEN t.typname = 'int4' THEN 'INTEGER' " +
                "        WHEN t.typname = 'int8' THEN 'BIGINT' " +
                "        WHEN t.typname = 'float4' THEN 'REAL' " +
                "        WHEN t.typname = 'float8' THEN 'DOUBLE PRECISION' " +
                "        WHEN t.typname = 'varchar' THEN 'VARCHAR' " +
                "        WHEN t.typname = 'bpchar' THEN 'CHAR' " +
                "        WHEN t.typname = 'text' THEN 'TEXT' " +
                "        ELSE t.typname  " +
                "    END AS DATATYPE," +
                "CASE WHEN t.typname IN ('varchar', 'char', 'bpchar', 'text') THEN a.atttypmod - 4 ELSE a.attlen END AS DATALENGTH, " +
                "CASE WHEN t.typname IN ('numeric', 'decimal', 'float4', 'float8') THEN (a.atttypmod - 4) >> 16 ELSE NULL END AS DATAPRECISION, " +
                "CASE WHEN t.typname IN ('numeric', 'decimal', 'float4', 'float8') THEN (a.atttypmod - 4) & 65535 ELSE NULL END AS DATASCALE, " +
                "CASE WHEN con.contype = 'p' THEN TRUE ELSE FALSE END AS COLKEY, " +
                "NOT a.attnotnull AS NULLABLE, a.attnum AS COLPOSITION, " +
                "regexp_replace(pg_get_expr(d.adbin, d.adrelid), '(::[a-zA-Z0-9_]+)+$', '') AS DATADEFAULT," +
                "col_description(a.attrelid, a.attnum) AS COLCOMMENT " +
                "FROM sys_attribute a " +
                "JOIN sys_class c ON a.attrelid = c.oid " +
                "JOIN sys_type t ON a.atttypid = t.oid " +
                "LEFT JOIN sys_constraint con ON con.conrelid = c.oid AND a.attnum = ANY(con.conkey) AND con.contype = 'p' " +
                "LEFT JOIN sys_attrdef d ON a.attrelid = d.adrelid AND a.attnum = d.adnum " +
                "WHERE (SELECT current_database()) =  '" + dbQueryProperty.getDbName() + "'  " +
                "AND c.relname =  '" + tableName + "'  " +
                "AND c.relnamespace = (SELECT oid FROM sys_namespace WHERE nspname =  '" + dbQueryProperty.getSid() + "' ) " +
                "AND a.attnum > 0 " +  // 过滤掉系统隐藏列
                "ORDER BY a.attnum";
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(1) FROM pg_tables "
                + "WHERE schemaname = '" + dbQueryProperty.getSid() + "' "
                + "  AND tablename = '" + tableName + "'";
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        String dbName = dbQueryProperty.getDbName();
        String sid = dbQueryProperty.getSid();

        if(StringUtils.isNotEmpty(sid)){
            tableName = sid + "." + tableName;
        }

        List<String> sqlList =  generateKingbaseCreateSql(tableName,tableComment,dbColumnList);

        return sqlList;
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return null;
    }

    private List<String> generateKingbaseCreateSql(String tableName, String tableComment, List<DbColumn> columns) {
        List<String> sqlList = new ArrayList<>();
        StringBuilder createSql = new StringBuilder();

        createSql.append("CREATE TABLE ").append(tableName).append(" (");
        List<String> pkList = new ArrayList<>();
        for (DbColumn col : columns) {
            createSql.append("\n  ").append(col.getColName()).append(" ");
            createSql.append(mapKingbaseColumnType(col));

            if (!col.getNullable()) {
                createSql.append(" NOT NULL");
            }
            if (StringUtils.isNotEmpty(col.getDataDefault())) {
                createSql.append(" DEFAULT ").append(col.getDataDefault());
//                String columnType = col.getDataType();
//                if (isStringTypeSwitchDEFAULT(columnType)) {
//                    createSql.append(" DEFAULT '").append(MD5Util.escapeSingleQuotes(col.getDataDefault())).append("'");
//                } else {
//                    createSql.append(" DEFAULT ").append(col.getDataDefault());
//                }
            }
            if (col.getColKey()) {
                pkList.add(col.getColName());
            }
            createSql.append(",");
        }
        if (createSql.lastIndexOf(",") == createSql.length() - 1) {
            createSql.deleteCharAt(createSql.length() - 1);
        }
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

        if (StringUtils.isNotEmpty(tableComment)) {
            String tableCmt = "COMMENT ON TABLE " + tableName + " IS '" + MD5Util.escapeSingleQuotes(tableComment) + "'";
            sqlList.add(tableCmt);
        }
        for (DbColumn col : columns) {
            if (StringUtils.isNotEmpty(col.getColComment())) {
                String colCmt = "COMMENT ON COLUMN " + tableName + "." + col.getColName()
                        + " IS '" + MD5Util.escapeSingleQuotes(col.getColComment()) + "'";
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

    private static String mapKingbaseColumnType(DbColumn col) {
        // 类似 Oracle
        String type = col.getDataType();
        Long length = MD5Util.getStringToLong(col.getDataLength());
        Long scale = MD5Util.getStringToLong(col.getDataScale());

        switch (type) {
            case "varchar":
            case "varchar2":
            case "VARCHAR":
            case "VARCHAR2":
                return "VARCHAR(" + (length != null ? length : 255) + ")";
            case "CHAR":
                return "CHAR(" + (length != null ? length : 1) + ")";
            case "INT":
            case "INTEGER":
                return "INTEGER";
            case "BIGINT":
                return "BIGINT";
            case "DECIMAL":
                return "DECIMAL(" + (length != null ? length : 10) + "," + (scale != null ? scale : 0) + ")";
            case "DATE":
                return "DATE";
            case "DATETIME":
                return "TIMESTAMP";
            case "TEXT":
                return "TEXT";
            default:
                return type;
        }
    }


    @Override
    public String tables(String dbName) {
        return  "SELECT " +
                "    c.relname AS TABLENAME, " +
                "    d.description AS TABLECOMMENT " +
                "FROM " +
                "    sys_class c " +
                " LEFT JOIN " +
                "    sys_description d ON c.oid = d.objoid AND d.objsubid = 0 " +
                "WHERE " +
                "    c.relkind = 'r' " +
                "    AND c.relnamespace = (SELECT oid FROM sys_namespace WHERE nspname = '" + "public" + "') " +
                "    AND (SELECT current_database()) = '" + dbName + "' " +
                "ORDER BY " +
                "    c.relname;";
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return  "SELECT " +
                "    c.relname AS TABLENAME, " +
                "    d.description AS TABLECOMMENT " +
                "FROM " +
                "    sys_class c " +
                " LEFT JOIN " +
                "    sys_description d ON c.oid = d.objoid AND d.objsubid = 0 " +
                "WHERE " +
                "    c.relkind = 'r' " +
                "    AND c.relnamespace = (SELECT oid FROM sys_namespace WHERE nspname = '" + dbQueryProperty.getSid() + "') " +
                "    AND (SELECT current_database()) = '" + dbQueryProperty.getDbName() + "' " +
                "ORDER BY " +
                "    c.relname;";
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        // 如果没有传入字段，则默认使用 * 查询所有字段
        if (columns == null || columns.isEmpty()) {
            return "SELECT * FROM " + tableName;
        }

        // 根据传入的 DbColumn 列表获取所有字段名，并用逗号分隔
        String fields = columns.stream()
                .map(DbColumn::getColName)
                .collect(Collectors.joining(", "));

        // 构造最终的 SQL 查询语句
        return "SELECT " + fields + " FROM " +dbQueryProperty.getSid()+"."+ tableName;
    }

    @Override
    public String buildPaginationSql(String originalSql, long offset, long count) {
        return originalSql + " LIMIT " + count + " OFFSET " + offset;
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return "SELECT (pg_database_size('" + dbName + "') / 1024 / 1024) AS used_size_mb";
    }

    @Override
    public String getDbName() {
        return "SELECT current_database()";
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
            entity.setColKey("1".equals(rs.getString("COLKEY")));
            entity.setNullable("YES".equals(rs.getString("NULLABLE")));
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
}
