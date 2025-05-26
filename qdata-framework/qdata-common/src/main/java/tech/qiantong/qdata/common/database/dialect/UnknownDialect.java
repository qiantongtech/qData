package tech.qiantong.qdata.common.database.dialect;

import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.exception.DataQueryException;

import java.util.List;
import java.util.Map;

/**
 * 未知 数据库方言
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class UnknownDialect extends AbstractDbDialect {

    @Override
    public String columns(String dbName, String tableName) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String tables(String dbName) {
        throw new DataQueryException("不支持的数据库类型");
    }
    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String buildPaginationSql(String sql, long offset, long count) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String count(String sql) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String countNew(String sql, Map<String, Object> params) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String getDataStorageSize(String dbName) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String getDbName() {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public RowMapper<DbColumn> columnMapper() {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public RowMapper<DbTable> tableMapper() {
        throw new DataQueryException("不支持的数据库类型");
    }
}
