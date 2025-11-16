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

import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;

import javax.sql.DataSource;
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

    String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName);

    List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList);

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
     * @param dbQueryProperty
     * @param tableName
     * @return
     */
    String tablesComment(DbQueryProperty dbQueryProperty,String tableName);

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

    String countNew(String sql, Map<String, Object> params);

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
     * 验证连接
     *
     * @param dataSource
     * @param dbQueryProperty
     * @return
     */
    Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty);

    String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue);

    String getTableName(DbQueryProperty property, String tableName);
}
