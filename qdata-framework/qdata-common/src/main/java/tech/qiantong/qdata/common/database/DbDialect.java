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

package tech.qiantong.qdata.common.database;

import com.alibaba.fastjson2.JSONObject;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbName;
import tech.qiantong.qdata.common.database.core.DbTable;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * Table data query interface
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public interface DbDialect {

    RowMapper<DbTable> tableMapper();

    RowMapper<DbColumn> columnMapper();

    /**
     * Get all columns of the specified table
     *
     * @param dbName
     * @param tableName
     * @return
     */
    String columns(String dbName, String tableName);

    String columns(DbQueryProperty dbQueryProperty, String tableName);

    /**
     * Get all columns under the specified database
     *
     * @param dbQueryProperty
     * @return
     */
    String getDbColumns(DbQueryProperty dbQueryProperty);

    String getPkColumnNames(DbQueryProperty dbQueryProperty, String tableName);

    String getPkColumnNames(DbQueryProperty dbQueryProperty);

    String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName);

    List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList);

    List<String> someInternalSqlDorisGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList, String partitionRule, String bucketRule, Integer replica);

    /**
     * Validate whether the table and column information conforms to DM8 specification requirements.
     *
     * @param tableName    Table name.
     * @param tableComment Table comment.
     * @param columns      Column definition list.
     * @return Returns a list of error messages. If the list is empty, all inputs conform to the specification.
     */
    List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns);

    /**
     * Get all tables in the database
     *
     * @param dbName
     * @return
     */
    String tables(String dbName);

    String tables(DbQueryProperty dbQueryProperty);

    /**
     * Hive-specific for now
     *
     * @param dbQueryProperty
     * @param tableName
     * @return
     */
    String tablesComment(DbQueryProperty dbQueryProperty, String tableName);

    String buildTableNameByDbType(DbQueryProperty dbQueryProperty, String tableName);

    /**
     * @param columns
     * @param tableName
     * @param dbQueryProperty
     * @return
     */
    String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty);

    /**
     * Build pagination SQL
     *
     * @param sql
     * @param offset
     * @param count
     * @return
     */
    String buildPaginationSql(String sql, long offset, long count);

    /**
     * Wrap count SQL
     *
     * @param sql
     * @return
     */
    String count(String sql);

    String countNew(String tableName, Map<String, Object> params);

    /**
     * Oracle has a bug where reading long type causes stream closure, requiring special handling
     *
     * @return
     */
    default RowMapper<DbColumn> columnLongMapper() {
        return null;
    }


    /**
     * Get storage size
     *
     * @return
     */
    String getDataStorageSize(String dbName);

    /**
     * Get database name or schema name
     *
     * @return
     */
    String getDbName();

    /**
     * Get database name or schema name
     *
     * @param dbName
     * @return
     */
    String getDbName(DbName dbName);

    /**
     * First-level RowMapper:
     * Maps DBNAME/TOTALLEVELS to DbName
     */
    default RowMapper<DbName> firstLevelMapper(int level) {
        return (ResultSet rs, int rowNum) -> DbName.builder()
                .dbName(rs.getString("DBNAME"))
                .level(level)
                .totalLevels(rs.getInt("TOTALLEVELS"))
                .children(null)
                .build();
    }


//    String getDatabasePhysicalInfo(DbName dbName);


    /**
     * Validate connection
     *
     * @param dataSource
     * @param dbQueryProperty
     * @return
     */
    Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty);

    String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue);

    String getFlinkSQL(DbQueryProperty property, String flinkTableName, String tableName, String tableFieldName);

    String getFlinkCDCSQL(DbQueryProperty property, String flinkTableName, String tableName, String tableFieldName);

    String getTableName(DbQueryProperty property, String tableName);

    String getFlinkSinkSQL(DbQueryProperty dbQueryProperty, JSONObject config, String flinkTableName, String tableName, String tableFieldName);

    /**
     * Convert DbQueryProperty to jdbcUrl
     *
     * @param property
     * @return
     */
    String trainToJdbcUrl(DbQueryProperty property);


    /**
     * Get table information SQL
     * @param dbQueryProperty
     * @param tableName
     * @return
     */
    String table(DbQueryProperty dbQueryProperty,String tableName);

    /**
     * Get SQL for updating comments
     * @param dbQueryProperty
     * @param tableName
     * @param tableComment
     * @return
     */
    String updateTableComment(DbQueryProperty dbQueryProperty, String tableName, String tableComment);

    /**
     * Modify column
     * @param dbQueryProperty
     * @param tableName
     * @param column
     * @return
     */
    List<String> modifyColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column);

    /**
     * Add column
     * @param dbQueryProperty
     * @param tableName
     * @param column
     * @return
     */
    List<String> addColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column);

    /**
     * Drop column
     * @param dbQueryProperty
     * @param tableName
     * @param colName
     * @return
     */
    String dropColumn(DbQueryProperty dbQueryProperty, String tableName, String colName);

    /**
     * Update primary key
     * @param dbQueryProperty
     * @param tableName
     * @param colKeyDbColumnList
     * @return
     */
    List<String> updateColKey(DbQueryProperty dbQueryProperty, String tableName, List<DbColumn> colKeyDbColumnList);

    /**
     * Get column type
     * @param column
     * @return
     */
    String getColumnType(DbColumn column);
}
