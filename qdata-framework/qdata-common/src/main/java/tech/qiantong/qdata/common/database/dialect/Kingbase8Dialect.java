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
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbName;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.utils.DatabaseUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
                "FROM pg_attribute a " +
                "JOIN pg_class c ON a.attrelid = c.oid " +
                "JOIN pg_type t ON a.atttypid = t.oid " +
                "LEFT JOIN pg_constraint con ON con.conrelid = c.oid AND a.attnum = ANY(con.conkey) AND con.contype = 'p' " +
                "LEFT JOIN pg_attrdef d ON a.attrelid = d.adrelid AND a.attnum = d.adnum " +
                "WHERE (SELECT current_database()) =  '" + dbName + "'  " +
                "AND c.relname =  '" + tableName + "'  " +
                "AND c.relnamespace = (SELECT oid FROM pg_namespace WHERE nspname =  '" + "public" + "' ) " +
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
                "FROM pg_attribute a " +
                "JOIN pg_class c ON a.attrelid = c.oid " +
                "JOIN pg_type t ON a.atttypid = t.oid " +
                "LEFT JOIN pg_constraint con ON con.conrelid = c.oid AND a.attnum = ANY(con.conkey) AND con.contype = 'p' " +
                "LEFT JOIN pg_attrdef d ON a.attrelid = d.adrelid AND a.attnum = d.adnum " +
                "WHERE (SELECT current_database()) =  '" + dbQueryProperty.getDbName() + "'  " +
                "AND c.relname =  '" + tableName + "'  " +
                "AND c.relnamespace = (SELECT oid FROM pg_namespace WHERE nspname =  '" + dbQueryProperty.getSid() + "' ) " +
                "AND a.attnum > 0 " +  // 过滤掉系统隐藏列
                "ORDER BY a.attnum";
    }


    @Override
    public String getDbColumns(DbQueryProperty dbQueryProperty) {
        return "SELECT " +
                "c.relname AS TABLENAME, " +                       // ★ 新增
                "a.attname AS COLNAME, " +
                "CASE " +
                "  WHEN t.typname = 'int2' THEN 'SMALLINT' " +
                "  WHEN t.typname = 'int4' THEN 'INTEGER' " +
                "  WHEN t.typname = 'int8' THEN 'BIGINT' " +
                "  WHEN t.typname = 'float4' THEN 'REAL' " +
                "  WHEN t.typname = 'float8' THEN 'DOUBLE PRECISION' " +
                "  WHEN t.typname = 'varchar' THEN 'VARCHAR' " +
                "  WHEN t.typname = 'bpchar' THEN 'CHAR' " +
                "  WHEN t.typname = 'text' THEN 'TEXT' " +
                "  ELSE t.typname " +
                "END AS DATATYPE, " +
                "CASE " +
                "  WHEN t.typname IN ('varchar','char','bpchar','text') THEN a.atttypmod - 4 " +
                "  ELSE a.attlen " +
                "END AS DATALENGTH, " +
                "CASE " +
                "  WHEN t.typname IN ('numeric','decimal','float4','float8') THEN (a.atttypmod - 4) >> 16 " +
                "  ELSE NULL " +
                "END AS DATAPRECISION, " +
                "CASE " +
                "  WHEN t.typname IN ('numeric','decimal','float4','float8') THEN (a.atttypmod - 4) & 65535 " +
                "  ELSE NULL " +
                "END AS DATASCALE, " +
                "CASE WHEN con.contype = 'p' THEN TRUE ELSE FALSE END AS COLKEY, " +
                "NOT a.attnotnull AS NULLABLE, " +
                "a.attnum AS COLPOSITION, " +
                "regexp_replace(pg_get_expr(d.adbin, d.adrelid), '(::[a-zA-Z0-9_]+)+$', '') AS DATADEFAULT, " +
                "col_description(a.attrelid, a.attnum) AS COLCOMMENT " +
                "FROM pg_attribute a " +
                "JOIN pg_class c ON a.attrelid = c.oid " +
                "JOIN pg_type t ON a.atttypid = t.oid " +
                "LEFT JOIN pg_constraint con " +
                "  ON con.conrelid = c.oid AND a.attnum = ANY(con.conkey) AND con.contype = 'p' " +
                "LEFT JOIN pg_attrdef d " +
                "  ON a.attrelid = d.adrelid AND a.attnum = d.adnum " +
                "WHERE c.relnamespace = ( " +
                "  SELECT oid FROM pg_namespace WHERE nspname = '" + dbQueryProperty.getSid() + "' " +
                ") " +
                "AND a.attnum > 0 " +                               // 过滤系统列
                "ORDER BY c.relname, a.attnum";
    }


    @Override
    public String buildTableNameByDbType(DbQueryProperty dbQueryProperty, String tableName) {
        if (StringUtils.isNotEmpty(dbQueryProperty.getSid())) {
            return dbQueryProperty.getSid() + "." + tableName;
        }

        return " public." + tableName;
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

        if (StringUtils.isNotEmpty(sid)) {
            tableName = sid + "." + tableName;
        }

        List<String> sqlList = generateKingbaseCreateSql(tableName, tableComment, dbColumnList);

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
            String tableCmt = "COMMENT ON TABLE " + tableName + " IS '" + DatabaseUtil.escapeSingleQuotes(tableComment) + "'";
            sqlList.add(tableCmt);
        }
        for (DbColumn col : columns) {
            if (StringUtils.isNotEmpty(col.getColComment())) {
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

    private static String mapKingbaseColumnType(DbColumn col) {
        // 类似 Oracle
        String type = col.getDataType().toUpperCase(Locale.ROOT);
        Long length = DatabaseUtil.getStringToLong(col.getDataLength());
        Long scale = DatabaseUtil.getStringToLong(col.getDataScale());

        switch (type) {
            case "VARCHAR":
            case "VARCHAR2":
                if (length == null || length < 1) {
                    return "VARCHAR";
                }
                return "VARCHAR(" + (length != null ? length : 255) + ")";
            case "CHAR":
                return "CHAR(" + (length != null ? length : 1) + ")";
            case "TEXT":
                return "TEXT";
            case "CLOB":
                return "CLOB";
            case "NCLOB":
                return "NCLOB";
            case "TINYINT":
                return "TINYINT";
            case "SMALLINT":
                return "SMALLINT";
            case "INT":
            case "INTEGER":
                return "INTEGER";
            case "BIGINT":
                return "BIGINT";
            case "DECIMAL":
                return "DECIMAL(" + (length != null ? length : 10) + "," + (scale != null ? scale : 0) + ")";
            case "NUMERIC":
                return "NUMERIC(" + (length != null ? length : 10) + "," + (scale != null ? scale : 0) + ")";
            case "NUMBER":
                return "NUMBER(" + (length != null ? length : 10) + "," + (scale != null ? scale : 0) + ")";
            case "FLOAT":
                return "FLOAT(" + (scale != null ? scale : 10) + ")";
            case "REAL":
                return "REAL";
            case "DOUBLE PRECISION":
                return "DOUBLE PRECISION";
            case "BOOL":
                return "BOOL";
            case "BOOLEAN":
                return "BOOLEAN";
            case "JSON":
                return "JSON";
            case "DATE":
                return "DATE";
            case "DATETIME":
                return "TIMESTAMP";
            default:
                return type;
        }
    }


    @Override
    public String tables(String dbName) {
        return "SELECT " +
                "    c.relname AS TABLENAME, " +
                "    d.description AS TABLECOMMENT " +
                "FROM " +
                "    pg_class c " +
                " LEFT JOIN " +
                "    pg_description d ON c.oid = d.objoid AND d.objsubid = 0 " +
                "WHERE " +
                "    c.relkind = 'r' " +
                "    AND c.relnamespace = (SELECT oid FROM pg_namespace WHERE nspname = '" + "public" + "') " +
                "    AND (SELECT current_database()) = '" + dbName + "' " +
                "ORDER BY " +
                "    c.relname;";
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return "SELECT " +
                "    c.relname AS TABLENAME, " +
                "    d.description AS TABLECOMMENT " +
                "FROM " +
                "    pg_class c " +
                " LEFT JOIN " +
                "    pg_description d ON c.oid = d.objoid AND d.objsubid = 0 " +
                "WHERE " +
                "    c.relkind = 'r' " +
                "    AND c.relnamespace = (SELECT oid FROM pg_namespace WHERE nspname = '" + dbQueryProperty.getSid() + "') " +
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
        return "SELECT " + fields + " FROM " + dbQueryProperty.getSid() + "." + tableName;
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
    public String getDbName(DbName dbName) {
        int level = dbName == null ? 1 : dbName.getLevel() + 1;
        if (level == 1) {
            return "SELECT datname AS DBNAME, 2 AS TOTALLEVELS " +
                    "FROM pg_database WHERE datistemplate = false ORDER BY DBNAME";
        } else if (level == 2) {
            // 必须先用 dbName 重建连接，再执行：
            return "SELECT schema_name AS DBNAME, 2 AS TOTALLEVELS " +
                    "FROM information_schema.schemata " +
                    "WHERE schema_name NOT LIKE 'pg_%' " +
                    "AND schema_name <> 'information_schema' " +
                    "ORDER BY DBNAME";
        }
        throw new UnsupportedOperationException("仅支持 level=1/2");
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
            entity.setColKey("t".equals(rs.getString("COLKEY")));
            entity.setNullable("t".equals(rs.getString("NULLABLE")));
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
    public String getFlinkCDCSQL(DbQueryProperty property, String flinkTableName, String tableName, String tableFieldName) {
        String sql = "CREATE TABLE ${flinkTableName} (${tableFieldName}) " +
                "WITH ( 'connector' = 'kingbase-cdc'," +
                "'hostname' = '${host}' ," +
                "'port' = '${port}' ," +
                "'username' = '${username}' ," +
                "'password' = '${password}'," +
                "'database-name' = '${dbName}' ," +
                "'schema-name' = '${sid}' ," +
                "'table-name' = '${tableName}' ," +
                "'slot.name' = 'flink'," +
                "'decoding.plugin.name' = 'decoderbufs'," +
                "'scan.startup.mode' = 'initial' ," +
                "'scan.incremental.snapshot.enabled' = 'true'," +
                "'debezium.snapshot.mode'='initial'" +
                ")";
        sql = StringUtils
                .replace(sql, "${flinkTableName}", flinkTableName)
                .replace("${tableName}", tableName)
                .replace("${host}", property.getHost())
                .replace("${tableFieldName}", tableFieldName)
                .replace("${port}", String.valueOf(property.getPort()))
                .replace("${sid}", property.getSid())
                .replace("${dbName}", property.getDbName())
                .replace("${username}", property.getUsername())
                .replace("${password}", property.getPassword());
        return sql;
    }

    @Override
    public String getFlinkSQL(DbQueryProperty property, String flinkTableName, String tableName, String tableFieldName) {
        String sql = "CREATE TABLE ${flinkTableName} (${tableFieldName}) " +
                "WITH ( 'connector' = 'jdbc'," +
                "'url' = 'jdbc:kingbase8://${host}:${port}/${dbName}?stringtype=unspecified'," +
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
    public String getTableName(DbQueryProperty property, String tableName) {
        return property.getDbName() + "." + property.getSid() + "." + tableName;
    }

    @Override
    public String getFlinkSinkSQL(DbQueryProperty property, JSONObject config, String flinkTableName, String tableName, String tableFieldName) {
        String sql = "CREATE TABLE ${flinkTableName} (${tableFieldName}) " +
                "WITH ( 'connector' = 'jdbc'," +
                "'url' = 'jdbc:kingbase8://${host}:${port}/${dbName}?stringtype=unspecified'," +
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

        if (Boolean.TRUE.equals(column.getColKey())) {
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(fullTableName).append(" ADD CONSTRAINT PK_").append(tableName).append("_").append(column.getColName());
            sql.append(" PRIMARY KEY (").append(column.getColName()).append(")");
            sqlList.add(sql.toString());
        } else {
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(fullTableName).append(" ALTER COLUMN ");
            sql.append(column.getColName()).append(" TYPE ");
            sql.append(mapKingbaseColumnType(column));
            sqlList.add(sql.toString());

            if (!column.getNullable()) {
                sqlList.add("ALTER TABLE " + fullTableName + " ALTER COLUMN " + column.getColName() + " SET NOT NULL");
            } else {
                sqlList.add("ALTER TABLE " + fullTableName + " ALTER COLUMN " + column.getColName() + " DROP NOT NULL");
            }

            if (StringUtils.isNotEmpty(column.getDataDefault())) {
                sqlList.add("ALTER TABLE " + fullTableName + " ALTER COLUMN " + column.getColName() + " SET DEFAULT " + column.getDataDefault());
            } else {
                sqlList.add("ALTER TABLE " + fullTableName + " ALTER COLUMN " + column.getColName() + " DROP DEFAULT");
            }
        }

        if (StringUtils.isNotEmpty(column.getColComment())) {
            sqlList.add("COMMENT ON COLUMN " + fullTableName + "." + column.getColName()
                    + " IS '" + DatabaseUtil.escapeSingleQuotes(column.getColComment()) + "'");
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
            sql.append(column.getColName()).append(" ");
            sql.append(mapKingbaseColumnType(column));

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
            sql.append("ALTER TABLE ").append(fullTableName).append(" ADD COLUMN ");
            sql.append(column.getColName()).append(" ");
            sql.append(mapKingbaseColumnType(column));

            if (!column.getNullable()) {
                sql.append(" NOT NULL");
            }

            if (StringUtils.isNotEmpty(column.getDataDefault())) {
                sql.append(" DEFAULT ").append(column.getDataDefault());
            }

            sqlList.add(sql.toString());
        }

        if (StringUtils.isNotEmpty(column.getColComment())) {
            sqlList.add("COMMENT ON COLUMN " + fullTableName + "." + column.getColName()
                    + " IS '" + DatabaseUtil.escapeSingleQuotes(column.getColComment()) + "'");
        }

        return sqlList;
    }
}
