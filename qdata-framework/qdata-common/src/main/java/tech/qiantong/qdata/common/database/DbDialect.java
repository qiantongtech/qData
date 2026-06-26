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
 * 表数据查询接口
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public interface DbDialect {

    RowMapper<DbTable> tableMapper();

    RowMapper<DbColumn> columnMapper();

    /**
     * 获取指定表的所有列
     *
     * @param dbName
     * @param tableName
     * @return
     */
    String columns(String dbName, String tableName);

    String columns(DbQueryProperty dbQueryProperty, String tableName);

    /**
     * 获取指定库下所有列
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
     * 校验表及列信息是否符合 DM8 的规范要求.
     *
     * @param tableName    表名.
     * @param tableComment 表注释.
     * @param columns      列定义列表.
     * @return 返回错误信息的列表。如果列表为空，则表示所有输入均符合规范.
     */
    List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns);

    /**
     * 获取数据库下的 所有表
     *
     * @param dbName
     * @return
     */
    String tables(String dbName);

    String tables(DbQueryProperty dbQueryProperty);

    /**
     * 暂时hive专属
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
     * 构建 分页 sql
     *
     * @param sql
     * @param offset
     * @param count
     * @return
     */
    String buildPaginationSql(String sql, long offset, long count);

    /**
     * 包装 count sql
     *
     * @param sql
     * @return
     */
    String count(String sql);

    String countNew(String tableName, Map<String, Object> params);

    /**
     * oracl 读取long 类型会流关闭，是oracle的bug，需要特殊处理
     *
     * @return
     */
    default RowMapper<DbColumn> columnLongMapper() {
        return null;
    }


    /**
     * 获取存储量
     *
     * @return
     */
    String getDataStorageSize(String dbName);

    /**
     * 获取数据库名或模式名
     *
     * @return
     */
    String getDbName();

    /**
     * 获取数据库名或模式名
     *
     * @param dbName
     * @return
     */
    String getDbName(DbName dbName);

    /**
     * 首次层级 RowMapper：
     * 把 DBNAME/TOTALLEVELS 映射到 DbName
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
     * 验证连接
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
     * DbQueryProperty转jdbcUrl
     *
     * @param property
     * @return
     */
    String trainToJdbcUrl(DbQueryProperty property);


    /**
     * 获取表信息sql
     * @param dbQueryProperty
     * @param tableName
     * @return
     */
    String table(DbQueryProperty dbQueryProperty,String tableName);

    /**
     * 获取更改注释的SQL
     * @param dbQueryProperty
     * @param tableName
     * @param tableComment
     * @return
     */
    String updateTableComment(DbQueryProperty dbQueryProperty, String tableName, String tableComment);

    /**
     * 修改列
     * @param dbQueryProperty
     * @param tableName
     * @param column
     * @return
     */
    List<String> modifyColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column);

    /**
     * 添加列
     * @param dbQueryProperty
     * @param tableName
     * @param column
     * @return
     */
    List<String> addColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column);

    /**
     * 删除列
     * @param dbQueryProperty
     * @param tableName
     * @param colName
     * @return
     */
    String dropColumn(DbQueryProperty dbQueryProperty, String tableName, String colName);

    /**
     * 更新主键
     * @param dbQueryProperty
     * @param tableName
     * @param colKeyDbColumnList
     * @return
     */
    List<String> updateColKey(DbQueryProperty dbQueryProperty, String tableName, List<DbColumn> colKeyDbColumnList);

    /**
     * 获取列类型
     * @param column
     * @return
     */
    String getColumnType(DbColumn column);
}
