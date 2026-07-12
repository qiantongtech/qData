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
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Table data query interface
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public interface DbQuery {

    /**
     * Get database connection
     */
    Connection getConnection();

    /**
     * Check connectivity
     */
    boolean valid();

    /**
     * Close data source
     */
    void close();

    /**
     * Get all column list of the specified table
     *
     * @param dbName
     * @param tableName
     * @return
     */
    List<DbColumn> getTableColumns(String dbName, String tableName);

    List<DbColumn> getTableColumns(DbQueryProperty dbQueryProperty, String tableName);

    /**
     * Get all column information under the database
     * @param dbQueryProperty
     * @return
     */
    List<DbColumn> getDbColumns(DbQueryProperty dbQueryProperty);

    int generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName);

    List<String> generateCreateTableSQL(DbQueryProperty dbQueryProperty, String tableName, String tableComment,
                                        List<DbColumn> dbColumnList);

    /**
     * Doris create table SQL
     *
     * @param dbQueryProperty
     * @param tableName
     * @param tableComment
     * @param dbColumnList
     * @param partitionRule
     * @param bucketRule
     * @param replica
     * @return
     */
    List<String> generateDorisCreateTableSQL(DbQueryProperty dbQueryProperty, String tableName, String tableComment,
                                             List<DbColumn> dbColumnList,
                                             String partitionRule,
                                             String bucketRule,
                                             Integer replica);

    int createCollectionWithSchema(DbQueryProperty dbQueryProperty, String tableName, String tableComment,
                                   List<DbColumn> dbColumnList);

    /**
     * Get all table information in the specified database
     *
     * @param dbName
     * @return
     */
    List<DbTable> getTables(String dbName);

    List<DbTable> getTables(DbQueryProperty dbQueryProperty);

    List<DbName> getDbNames(DbName dbName);

    List<FileInfo> getFiles(String path);

    /**
     * Get total count
     *
     * @param sql
     * @return
     */
    int count(String sql);

    /**
     * Execute SQL directly to get statistics count
     *
     * @param sql
     * @return
     */
    int executeCountSql(String sql);

    /**
     * Get total count with query parameters
     *
     * @param sql
     * @return
     */
    int count(String sql, Object[] args);

    /**
     * Get total count with query parameters using NamedParameterJdbcTemplate
     *
     * @param sql
     * @return
     */
    int count(String sql, Map<String, Object> params);

    int countNew(String sql, Map<String, Object> params);

    int countNew(String tableName, DbQueryProperty dbQueryProperty, String where);

    /**
     * Query result list
     *
     * @param sql
     * @return
     */
    List<Map<String, Object>> queryList(String sql);

    List<Map<String, Object>> queryDbColumnByList(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty, long offset, long size);

    List<Map<String, Object>> queryDbColumnByList(
            List<DbColumn> columns
            , String tableName
            , DbQueryProperty dbQueryProperty
            , String where
            , List<Map> orderByList
            , long offset
            , long size
    );

    /**
     * Query result list with query parameters
     *
     * @param sql
     * @param args
     * @return
     */
    List<Map<String, Object>> queryList(String sql, Object[] args);

    /**
     * Query result list with query parameters
     *
     * @param sql
     * @param params
     * @param cache  Whether to enable cache 0 No 1 Yes
     * @return
     */
    List<Map<String, Object>> queryList(String sql, Map<String, Object> params, Integer cache);

    /**
     * Query detail result with query parameters
     *
     * @param sql
     * @param params
     * @param cache  Whether to enable cache 0 No 1 Yes
     * @return
     */
    Map<String, Object> queryOne(String sql, Map<String, Object> params, Integer cache);

    /**
     * Paginated query results
     *
     * @param sql
     * @param offset
     * @param size
     * @return
     */
    PageResult<Map<String, Object>> queryByPage(String sql, long offset, long size);

    /**
     * Paginated query results with query parameters
     *
     * @param sql
     * @param args
     * @param offset
     * @param size
     * @return
     */
    PageResult<Map<String, Object>> queryByPage(String sql, Object[] args, long offset, long size);

    /**
     * Paginated query results with query parameters using NamedParameterJdbcTemplate
     *
     * @param sql
     * @param params
     * @param offset
     * @param size
     * @param cache  Whether to enable cache 0 No 1 Yes
     * @return
     */
    PageResult<Map<String, Object>> queryByPage(String sql, Map<String, Object> params, long offset, long size,
                                                Integer cache);

    int update(String sql);

    int addTableData(String tableName, Map<String, Object> after);

    int updateTableData(Map<String, Object> after,
                        List<String> setCols,
                        List<String> whereCols,
                        String tableName);

    void execute(String sql);

    int[] batchUpdate(String sql);

    int isTableExists(String sql);

    /**
     * Get storage size
     *
     * @return
     */
    Integer getDataStorageSize();

    /**
     * Create a new table based on an existing table
     *
     * @param dbQueryProperty
     * @param tableName
     * @param newTableName
     * @return
     */
    Boolean copyTable(Connection conn, DbQueryProperty dbQueryProperty, String tableName, String newTableName);

    /**
     * Create a table from one table to another database
     *
     * @param otherDbQueryProperty
     * @param tableName
     * @param newTableName
     * @param addColumn            Additional columns, nullable
     * @return
     */
    Boolean copyTableToOtherDb(DbQueryProperty otherDbQueryProperty, String tableName, String newTableName, String newTableComment, List<JSONObject> addColumn, String partitionRule, String bucketRule, Integer replica);

    String getInsertOrUpdateSql(DbQueryProperty property, String tableName, List<String> selectedColumns, List<String> column);


    /**
     * Query columns by SQL
     *
     * @param querySql
     * @return
     */
    List<DbColumn> getColumnsByQuerySql(String querySql);

    void uploadFile(String path, MultipartFile file);

    /**
     * Get Flink fields
     *
     * @param dbQueryProperty Database connection information
     * @param config          Additional extension parameters
     * @param tableName
     * @param column
     * @param querySql        When this parameter is not empty, tableName is ignored
     * @return
     */
    String generateFlinkFields(DbQueryProperty dbQueryProperty, JSONObject config, String tableName, List<String> column, String querySql);

    /**
     * Get Flink SQL
     *
     * @param taskExecuteType Execution type: STREAM streaming mode, BATCH batch processing
     * @param flinkTableName  Flink table name
     * @param tableName       Real table name
     * @param column          Columns
     * @param querySql
     * @return
     */
    String getFlinkSQL(JSONObject config, String taskExecuteType, String flinkTableName, String tableName, List<String> column, String querySql);

    /**
     * Get Flink CDC SQL
     *
     * @param flinkTableName Flink table name
     * @param tableName      Real table name
     * @param column         Columns
     * @param querySql
     * @return
     */
    String getFlinkCDCSQL(JSONObject config, String flinkTableName, String tableName, List<String> column, String querySql);

    /**
     * Get Flink sink SQL
     *
     * @param taskExecuteType Execution type: STREAM streaming mode, BATCH batch processing
     * @param flinkTableName  Flink table name
     * @param tableName       Real table name
     * @param column          Columns
     * @return
     */
    String getFlinkSinkSQL(JSONObject config, String taskExecuteType, String flinkTableName, String tableName, List<String> column);

    /**
     * Delete table
     *
     * @param dbQueryProperty
     * @param tableName
     * @return
     */
    Boolean deleteTable(DbQueryProperty dbQueryProperty, String tableName);

    /**
     * Update table SQL
     * @param dbQueryProperty
     * @param tableName
     * @param tableComment
     * @param dbColumnList
     * @return
     */
    List<String> generateUpdateTableSQL(DbQueryProperty dbQueryProperty, String tableName, String tableComment,
                                        List<DbColumn> dbColumnList);
}
