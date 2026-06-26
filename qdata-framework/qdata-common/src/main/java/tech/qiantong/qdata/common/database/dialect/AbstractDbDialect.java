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
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import tech.qiantong.qdata.common.database.DbDialect;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbName;
import tech.qiantong.qdata.common.database.exception.DataQueryException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 方言抽象类
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
@Slf4j
public abstract class AbstractDbDialect implements DbDialect {

    @Override
    public String columns(String dbName, String tableName) {
        return "select column_name AS COLNAME, ordinal_position AS COLPOSITION, column_default AS DATADEFAULT, is_nullable AS NULLABLE, data_type AS DATATYPE, " +
                "character_maximum_length AS DATALENGTH, numeric_precision AS DATAPRECISION, numeric_scale AS DATASCALE, column_key AS COLKEY, column_comment AS COLCOMMENT " +
                "from information_schema.columns where table_schema = '" + dbName + "' and table_name = '" + tableName + "' order by ordinal_position ";
    }

    @Override
    public String tables(String dbName) {
        return "SELECT table_name AS TABLENAME, table_comment AS TABLECOMMENT FROM information_schema.tables where table_schema = '" + dbName + "' ";
    }

    @Override
    public String getPkColumnNames(DbQueryProperty dbQueryProperty, String tableName) {
        return "";
    }


    @Override
    public String getPkColumnNames(DbQueryProperty dbQueryProperty) {
        return "";
    }


    @Override
    public String tablesComment(DbQueryProperty dbQueryProperty, String tableName) {
        return null;
    }

    @Override
    public String buildTableNameByDbType(DbQueryProperty dbQueryProperty, String tableName) {
        return tableName;
    }

    @Override
    public String buildPaginationSql(String originalSql, long offset, long count) {
        // 获取 分页实际条数
        StringBuilder sqlBuilder = new StringBuilder(originalSql);
        sqlBuilder.append(" LIMIT ").append(offset).append(" , ").append(count);
        return sqlBuilder.toString();
    }

    @Override
    public String count(String sql) {
        return "SELECT COUNT(*) FROM ( " + sql + " ) TEMP";
    }

    @Override
    public String countNew(String tableName, Map<String, Object> params) {
        // 动态构建 WHERE 子句
        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM ").append(tableName);
        if (params != null && !params.isEmpty()) {
            countSql.append(buildWhereClause(params));
        }
        return countSql.toString();
    }

    /**
     * 验证连接
     *
     * @param dataSource
     * @param dbQueryProperty
     * @return
     */
    @Override
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(0);
        } catch (SQLException e) {
            log.error("数据库连接失败,稍后重试", e);
            throw new DataQueryException("db.error.connection.retry", "数据库连接失败，请稍后重试");
        }
    }


    /**
     * 动态构建 WHERE 子句
     *
     * @param params 参数 Map
     * @return WHERE 子句字符串
     */
    private static String buildWhereClause(Map<String, Object> params) {
        StringBuilder whereClause = new StringBuilder(" WHERE 1=1");
        for (String key : params.keySet()) {
            whereClause.append(" AND ").append(key).append(" = :").append(key);
        }
        return whereClause.toString();
    }

    @Override
    public String trainToJdbcUrl(DbQueryProperty property) {
        String url = DbType.getDbType(property.getDbType()).getUrl();
        if (StringUtils.isEmpty(url)) {
            throw new DataQueryException("db.error.invalid.dbtype", "无效数据库类型");
        }
        url = url.replace("${host}", property.getHost());
        url = url.replace("${port}", String.valueOf(property.getPort()));
        url = url.replace("${dbName}", property.getDbName());
        return url;
    }

    @Override
    public String getFlinkCDCSQL(DbQueryProperty property, String flinkTableName, String tableName, String tableFieldName) {
        return null;
    }

    @Override
    public String getFlinkSQL(DbQueryProperty property, String flinkTableName, String tableName, String tableFieldName) {
        return null;
    }

    @Override
    public String getTableName(DbQueryProperty property, String tableName) {
        if (!StringUtils.isEmpty(property.getDbName())) {
            return property.getDbName() + "." + tableName;
        }
        return tableName;
    }

    @Override
    public String getFlinkSinkSQL(DbQueryProperty property, JSONObject config, String flinkTableName, String tableName, String tableFieldName) {
        return null;
    }

    @Override
    public String getDbName(DbName dbName) {
        return null;
    }


    @Override
    public String getDbColumns(DbQueryProperty property) {
        return null;
    }

    @Override
    public List<String> someInternalSqlDorisGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList, String partitionRule, String bucketRule, Integer replica) {
        return null;
    }

    /**
     * 检查是否使用SSL
     *
     * @param property
     * @return
     */
    protected Boolean checkUseSSL(DbQueryProperty property) {
        if (property.getDatasourceConfig().containsKey("useSSL")) {
            Integer useSSL = (Integer) property.getDatasourceConfig().get("useSSL");
            if (useSSL == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否使用Kerberos
     *
     * @param property
     * @return
     */
    protected static Boolean checkUseKerberos(DbQueryProperty property) {
        if (property.getDatasourceConfig().containsKey("useKerberos")) {
            Integer useSSL = (Integer) property.getDatasourceConfig().get("useKerberos");
            if (useSSL == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String table(DbQueryProperty dbQueryProperty,String tableName) {
        return null;
    }

    @Override
    public String updateTableComment(DbQueryProperty dbQueryProperty, String tableName, String tableComment) {
        return null;
    }

    @Override
    public List<String> addColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column) {
        return null;
    }

    @Override
    public List<String> modifyColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column) {
        return null;
    }

    @Override
    public String dropColumn(DbQueryProperty dbQueryProperty, String tableName, String colName) {
        return null;
    }

    @Override
    public List<String> updateColKey(DbQueryProperty dbQueryProperty, String tableName, List<DbColumn> colKeyDbColumnList) {
        return null;
    }

    @Override
    public String getColumnType(DbColumn column) {
        return null;
    }
}
