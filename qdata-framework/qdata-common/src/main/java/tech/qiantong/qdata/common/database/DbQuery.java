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

package tech.qiantong.qdata.common.database;

import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.core.PageResult;

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

    int generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName);

    List<String> generateCreateTableSQL(DbQueryProperty dbQueryProperty, String tableName, String tableComment,
                                        List<DbColumn> dbColumnList);

    /**
     * 获取指定数据库下 所有的表信息
     *
     * @param dbName
     * @return
     */
    List<DbTable> getTables(String dbName);

    List<DbTable> getTables(DbQueryProperty dbQueryProperty);

    /**
     * 获取总数
     *
     * @param sql
     * @return
     */
    int count(String sql);

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
    Boolean copyTable(Connection conn,DbQueryProperty dbQueryProperty, String tableName, String newTableName);

    String getInsertOrUpdateSql(DbQueryProperty property,String tableName, List<String> selectedColumns, List<String> column);


    /**
     * 通过sql查询字段
     * @param querySql
     * @return
     */
    List<DbColumn> getColumnsByQuerySql(String querySql);
}
