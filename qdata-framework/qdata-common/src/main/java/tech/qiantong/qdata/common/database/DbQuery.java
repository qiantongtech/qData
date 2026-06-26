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
 * 表数据查询接口
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public interface DbQuery {

    /**
     * 获取数据库连接
     */
    Connection getConnection();

    /**
     * 检测连通性
     */
    boolean valid();

    /**
     * 关闭数据源
     */
    void close();

    /**
     * 获取指定表 具有的所有字段列表
     *
     * @param dbName
     * @param tableName
     * @return
     */
    List<DbColumn> getTableColumns(String dbName, String tableName);

    List<DbColumn> getTableColumns(DbQueryProperty dbQueryProperty, String tableName);

    /**
     * 获取库下所有字段信息
     * @param dbQueryProperty
     * @return
     */
    List<DbColumn> getDbColumns(DbQueryProperty dbQueryProperty);

    int generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName);

    List<String> generateCreateTableSQL(DbQueryProperty dbQueryProperty, String tableName, String tableComment,
                                        List<DbColumn> dbColumnList);

    /**
     * doris 创建表 SQL
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
     * 获取指定数据库下 所有的表信息
     *
     * @param dbName
     * @return
     */
    List<DbTable> getTables(String dbName);

    List<DbTable> getTables(DbQueryProperty dbQueryProperty);

    List<DbName> getDbNames(DbName dbName);

    List<FileInfo> getFiles(String path);

    /**
     * 获取总数
     *
     * @param sql
     * @return
     */
    int count(String sql);

    /**
     * 直接执行sql获取统计数量
     *
     * @param sql
     * @return
     */
    int executeCountSql(String sql);

    /**
     * 获取总数带查询参数
     *
     * @param sql
     * @return
     */
    int count(String sql, Object[] args);

    /**
     * 获取总数带查询参数 NamedParameterJdbcTemplate
     *
     * @param sql
     * @return
     */
    int count(String sql, Map<String, Object> params);

    int countNew(String sql, Map<String, Object> params);

    int countNew(String tableName, DbQueryProperty dbQueryProperty, String where);

    /**
     * 查询结果列表
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
     * 查询结果列表带查询参数
     *
     * @param sql
     * @param args
     * @return
     */
    List<Map<String, Object>> queryList(String sql, Object[] args);

    /**
     * 查询结果列表带查询参数
     *
     * @param sql
     * @param params
     * @param cache  是否开启缓存 0否 1是
     * @return
     */
    List<Map<String, Object>> queryList(String sql, Map<String, Object> params, Integer cache);

    /**
     * 查询详情结果带查询参数
     *
     * @param sql
     * @param params
     * @param cache  是否开启缓存 0否 1是
     * @return
     */
    Map<String, Object> queryOne(String sql, Map<String, Object> params, Integer cache);

    /**
     * 查询结果分页
     *
     * @param sql
     * @param offset
     * @param size
     * @return
     */
    PageResult<Map<String, Object>> queryByPage(String sql, long offset, long size);

    /**
     * 查询结果分页带查询参数
     *
     * @param sql
     * @param args
     * @param offset
     * @param size
     * @return
     */
    PageResult<Map<String, Object>> queryByPage(String sql, Object[] args, long offset, long size);

    /**
     * 查询结果分页带查询参数 NamedParameterJdbcTemplate
     *
     * @param sql
     * @param params
     * @param offset
     * @param size
     * @param cache  是否开启缓存 0否 1是
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
     * 获取存储量
     *
     * @return
     */
    Integer getDataStorageSize();

    /**
     * 根据一个表创建新表
     *
     * @param dbQueryProperty
     * @param tableName
     * @param newTableName
     * @return
     */
    Boolean copyTable(Connection conn, DbQueryProperty dbQueryProperty, String tableName, String newTableName);

    /**
     * 根据一个表创建表到另一个数据库中
     *
     * @param otherDbQueryProperty
     * @param tableName
     * @param newTableName
     * @param addColumn            追加字段可空
     * @return
     */
    Boolean copyTableToOtherDb(DbQueryProperty otherDbQueryProperty, String tableName, String newTableName, String newTableComment, List<JSONObject> addColumn, String partitionRule, String bucketRule, Integer replica);

    String getInsertOrUpdateSql(DbQueryProperty property, String tableName, List<String> selectedColumns, List<String> column);


    /**
     * 通过sql查询字段
     *
     * @param querySql
     * @return
     */
    List<DbColumn> getColumnsByQuerySql(String querySql);

    void uploadFile(String path, MultipartFile file);

    /**
     * 获取flink 字段
     *
     * @param dbQueryProperty 数据库连接信息
     * @param config          额外的扩展参数
     * @param tableName
     * @param column
     * @param querySql        该数据如果不为空时、tableName无效
     * @return
     */
    String generateFlinkFields(DbQueryProperty dbQueryProperty, JSONObject config, String tableName, List<String> column, String querySql);

    /**
     * 获取flink sql
     *
     * @param taskExecuteType 执行类型 STREAM：流模式  BATCH：批处理
     * @param flinkTableName  flink表名
     * @param tableName       真实表名
     * @param column          字段
     * @param querySql
     * @return
     */
    String getFlinkSQL(JSONObject config, String taskExecuteType, String flinkTableName, String tableName, List<String> column, String querySql);

    /**
     * 获取flink cdc sql
     *
     * @param flinkTableName flink表名
     * @param tableName      真实表名
     * @param column         字段
     * @param querySql
     * @return
     */
    String getFlinkCDCSQL(JSONObject config, String flinkTableName, String tableName, List<String> column, String querySql);

    /**
     * 获取flink sink sql
     *
     * @param taskExecuteType 执行类型 STREAM：流模式  BATCH：批处理
     * @param flinkTableName  flink表名
     * @param tableName       真实表名
     * @param column          字段
     * @return
     */
    String getFlinkSinkSQL(JSONObject config, String taskExecuteType, String flinkTableName, String tableName, List<String> column);

    /**
     * 删除表
     *
     * @param dbQueryProperty
     * @param tableName
     * @return
     */
    Boolean deleteTable(DbQueryProperty dbQueryProperty, String tableName);

    /**
     * 更新表 SQL
     * @param dbQueryProperty
     * @param tableName
     * @param tableComment
     * @param dbColumnList
     * @return
     */
    List<String> generateUpdateTableSQL(DbQueryProperty dbQueryProperty, String tableName, String tableComment,
                                        List<DbColumn> dbColumnList);
}
