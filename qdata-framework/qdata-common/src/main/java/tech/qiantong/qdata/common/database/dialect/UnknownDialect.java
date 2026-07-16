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

import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbName;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.exception.DataQueryException;

import java.util.List;
import java.util.Map;

/**
 * Unknown database dialect
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class UnknownDialect extends AbstractDbDialect {

    @Override
    public String columns(String dbName, String tableName) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }
    @Override
    public String getPkColumnNames(DbQueryProperty dbQueryProperty, String tableName) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }


    @Override
    public String buildTableNameByDbType(DbQueryProperty dbQueryProperty, String tableName) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public String tables(String dbName) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }
    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public String buildPaginationSql(String sql, long offset, long count) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public String count(String sql) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public String countNew(String sql, Map<String, Object> params) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public String getDataStorageSize(String dbName) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public String getDbName() {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public String getDbName(DbName dbName) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public RowMapper<DbColumn> columnMapper() {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public RowMapper<DbTable> tableMapper() {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public String dropColumn(DbQueryProperty dbQueryProperty, String tableName, String colName) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public List<String> modifyColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public List<String> addColumn(DbQueryProperty dbQueryProperty, String tableName, DbColumn column) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public List<String> updateColKey(DbQueryProperty dbQueryProperty, String tableName, List<DbColumn> colKeyDbColumnList) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }

    @Override
    public String getColumnType(DbColumn column) {
        throw new DataQueryException("db.error.unsupported.dbtype", "不支持的数据库类型");
    }
}
