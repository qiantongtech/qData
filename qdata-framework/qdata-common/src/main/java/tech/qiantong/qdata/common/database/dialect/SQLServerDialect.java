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
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.database.utils.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handle JDBC SQL execution.
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class SQLServerDialect extends SQLServer2008Dialect {

    @Override
    public String columns(String dbName, String tableName) {
        return "select columns.name AS colName, columns.column_id AS COLPOSITION, columns.max_length AS DATALENGTH, columns.precision AS DATAPRECISION, columns.scale AS DATASCALE, " +
                "columns.is_nullable AS NULLABLE, types.name AS DATATYPE, CAST(ep.value  AS NVARCHAR(128)) AS COLCOMMENT, (CASE WHEN e.text LIKE '((%)' AND e.text NOT LIKE '%)%''%' THEN SUBSTRING(e.text, 3, LEN(e.text) - 4) WHEN e.text LIKE '(%' THEN SUBSTRING(e.text, 2, LEN(e.text) - 2) ELSE e.text END) AS DATADEFAULT, " +
                "(CASE WHEN (SELECT ic.column_id FROM sys.indexes idx INNER JOIN sys.index_columns ic ON idx.object_id = ic.object_id AND idx.index_id = ic.index_id WHERE idx.is_primary_key = 1 AND columns.column_id = ic.column_id AND columns.object_id = ic.object_id)  IS NOT NULL THEN '1' ELSE '0' END) AS COLKEY " +
                "from sys.tables tables " +
                "JOIN sys.columns columns ON tables.object_id = columns.object_id " +
                "LEFT JOIN sys.types types ON columns.user_type_id = types.user_type_id AND columns.system_type_id = types.system_type_id " +
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
                "LEFT JOIN sys.types types ON columns.user_type_id = types.user_type_id AND columns.system_type_id = types.system_type_id " +
                "LEFT JOIN syscomments e ON columns.default_object_id= e.id " +
                "LEFT JOIN sys.extended_properties ep ON ep.major_id = columns.object_id AND ep.minor_id = columns.column_id AND ep.name = 'MS_Description' " +
                "where tables.name = '" + tableName + "' " +
                "AND SCHEMA_NAME(tables.schema_id) = '" + dbQueryProperty.getSid() + "' " +
                "order by columns.column_id ";
    }


    @Override
    public String buildPaginationSql(String originalSql, long offset, long count) {
        StringBuilder sqlBuilder = new StringBuilder(originalSql);
        String orderby = getOrderByPart(originalSql);
        if (StringUtils.isEmpty(orderby)) {
            orderby = "ORDER BY CURRENT_TIMESTAMP";
        } else {
            orderby = "";
        }

        sqlBuilder.append(" ").append(orderby).append(" OFFSET ").append(offset).append(" ROWS FETCH NEXT ").append(count).append(" ROWS ONLY ");
        return sqlBuilder.toString();
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


    @Override
    public String buildTableNameByDbType(DbQueryProperty dbQueryProperty, String tableName) {
        if (StringUtils.isNotEmpty(dbQueryProperty.getSid())) {
            return dbQueryProperty.getSid() + "." + tableName;
        }

        return tableName;
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
    public String getFlinkCDCSQL(DbQueryProperty property, String flinkTableName, String tableName, String tableFieldName) {
        String sql = "CREATE TABLE ${flinkTableName} (${tableFieldName}) " +
                "WITH ( 'connector' = 'sqlserver-cdc'," +
                "'hostname' = '${host}' ," +
                "'port' = '${port}' ," +
                "'username' = '${username}' ," +
                "'password' = '${password}'," +
                "'database-name' = '${dbName}' ," +
                "'table-name' = '${sid}.${tableName}'," +
                "'server-time-zone' = 'Asia/Shanghai'," +
                "'debezium.database.encrypt' = 'true'," +
                "'debezium.snapshot.mode'='initial'," +
                "'debezium.database.trustServerCertificate' = 'true'" +
                ")";
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
    public String getFlinkSQL(DbQueryProperty property, String flinkTableName, String tableName, String tableFieldName) {
        String sql = "CREATE TABLE ${flinkTableName} (${tableFieldName}) " +
                "WITH ( 'connector' = 'jdbc'," +
                "'url' = 'jdbc:sqlserver://${host}:${port};DatabaseName=${dbName};encrypt=true;trustServerCertificate=true'," +
                "'table-name' = '${dbName}.${sid}.${tableName}'," +
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
    public String getTableName(DbQueryProperty property, String tableName) {
        return property.getDbName() + "." + property.getSid() + "." + tableName;
    }


    @Override
    public String getFlinkSinkSQL(DbQueryProperty property, JSONObject config, String flinkTableName, String tableName, String tableFieldName) {
        String sql = "CREATE TABLE ${flinkTableName} (${tableFieldName}) " +
                "WITH ( 'connector' = 'jdbc'," +
                "'url' = 'jdbc:sqlserver://${host}:${port};DatabaseName=${dbName};encrypt=true;trustServerCertificate=true'," +
                "'table-name' = '${dbName}.${sid}.${tableName}'," +
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
                .replace("${sid}", property.getSid())
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
        url = url.replace("${dbName}", property.getDbName());
        // Implementation details.
        if (checkUseSSL(property)) {
            JSONObject sslConfig = (JSONObject) property.getDatasourceConfig().get("sslConfig");
            url = url.replace("encrypt=false", "encrypt=true")
                    .replace("trustServerCertificate=true", "trustServerCertificate=false");
            StringBuilder urlStrBuilder = new StringBuilder(url);
            urlStrBuilder
                    .append(";trustStore=").append(sslConfig.getString("trustStore"))
                    .append(";trustStorePassword=").append(sslConfig.getString("trustStorePassword"));
            url = urlStrBuilder.toString();
        }
        return url;
    }
}
