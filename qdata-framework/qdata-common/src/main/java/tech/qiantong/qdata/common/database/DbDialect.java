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
