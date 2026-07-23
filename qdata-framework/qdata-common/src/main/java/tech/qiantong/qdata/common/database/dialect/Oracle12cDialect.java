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
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.utils.DatabaseUtil;
import tech.qiantong.qdata.common.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * ORACLE Oracle12c+ database dialect
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class Oracle12cDialect extends OracleDialect {

    @Override
    public String buildPaginationSql(String originalSql, long offset, long count) {
        StringBuilder sqlBuilder = new StringBuilder(originalSql);
        sqlBuilder.append(" OFFSET ").append(offset).append(" ROWS FETCH NEXT ").append(count).append(" ROWS ONLY ");
        return sqlBuilder.toString();
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(*) FROM all_tables WHERE owner = '" + dbQueryProperty.getDbName() + "' AND table_name = '" + tableName.toUpperCase() + "'";
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
        String dbName = dbQueryProperty.getDbName();

        if(StringUtils.isNotEmpty(dbName)){
            tableName = dbName + "." + tableName;
        }

        List<String> sqlList =  generateOracleCreateSql(tableName,tableComment,dbColumnList);

        return sqlList;
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

        if(columnLength == null){
            throw new UnsupportedOperationException(MessageUtils.messageWithFallback(
                    "sys.error.database.numeric.length.missing",
                    "Invalid attribute type format: numeric type length is missing"));
        }

        // If columnLength is empty, maxLength is used as the default value
        long length = columnLength;

        if (length > maxLength) {
            length = maxLength;
        }

        // Splicing length
        sql.append("(").append(length);

        // Determine whether the number of decimal places needs to be spliced based on includeScale and columnScale
        if (includeScale &&  columnScale != 0 ) {
            sql.append(", ").append(columnScale);
        }

        sql.append(")");

        return sql.toString();
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
        return "SELECT " + fields + " FROM " +dbQueryProperty.getDbName()+"."+ tableName;
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
                .replace("${batchSize}", String.valueOf(config.getIntValue("batchSize",100)));
        return sql;
    }
}
