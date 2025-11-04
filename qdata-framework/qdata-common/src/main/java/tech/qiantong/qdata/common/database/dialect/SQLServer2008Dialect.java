package tech.qiantong.qdata.common.database.dialect;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.utils.MD5Util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SQLServer 2005 数据库方言
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class SQLServer2008Dialect extends AbstractDbDialect {

    @Override
    public String columns(String dbName, String tableName) {
        return "select columns.name AS colName, columns.column_id AS COLPOSITION, columns.max_length AS DATALENGTH, columns.precision AS DATAPRECISION, columns.scale AS DATASCALE, " +
                "columns.is_nullable AS NULLABLE, types.name AS DATATYPE, CAST(ep.value  AS NVARCHAR(128)) AS COLCOMMENT, e.text AS DATADEFAULT, " +
                "(select top 1 ind.is_primary_key from sys.index_columns ic left join sys.indexes ind on ic.object_id = ind.object_id and ic.index_id = ind.index_id and ind.name like 'PK_%' where ic.object_id=columns.object_id and ic.column_id=columns.column_id) AS COLKEY " +
                "from sys.columns columns LEFT JOIN sys.types types ON columns.system_type_id = types.system_type_id " +
                "LEFT JOIN syscomments e ON columns.default_object_id= e.id " +
                "LEFT JOIN sys.extended_properties ep ON ep.major_id = columns.object_id AND ep.minor_id = columns.column_id AND ep.name = 'MS_Description' " +
                "where columns.object_id = object_id('" + tableName + "') order by columns.column_id ";
    }
    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return "select columns.name AS colName, columns.column_id AS COLPOSITION, columns.max_length AS DATALENGTH, columns.precision AS DATAPRECISION, columns.scale AS DATASCALE, " +
                "columns.is_nullable AS NULLABLE, types.name AS DATATYPE, CAST(ep.value  AS NVARCHAR(128)) AS COLCOMMENT, e.text AS DATADEFAULT, " +
                "(select top 1 ind.is_primary_key from sys.index_columns ic left join sys.indexes ind on ic.object_id = ind.object_id and ic.index_id = ind.index_id and ind.name like 'PK_%' where ic.object_id=columns.object_id and ic.column_id=columns.column_id) AS COLKEY " +
                "from sys.columns columns LEFT JOIN sys.types types ON columns.system_type_id = types.system_type_id " +
                "LEFT JOIN syscomments e ON columns.default_object_id= e.id " +
                "LEFT JOIN sys.extended_properties ep ON ep.major_id = columns.object_id AND ep.minor_id = columns.column_id AND ep.name = 'MS_Description' " +
                "where columns.object_id = object_id('" + tableName + "') order by columns.column_id ";
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '" + dbQueryProperty.getDbName() + "' AND TABLE_NAME = '" + tableName + "';";
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        List<String> sqlList = new ArrayList<>();


        List<String> primaryKeys = new ArrayList<>();
        {
            StringBuilder sql = new StringBuilder();
            // 生成CREATE TABLE语句
            sql.append("CREATE TABLE ").append(tableName).append(" (\n");

            for (DbColumn column : dbColumnList) {
                String columnType = column.getDataType().toUpperCase();
                sql.append("  ").append(column.getColName()).append(" ");

                // 转换数据类型为SQL Server支持的类型
                switch (columnType) {
                    case "VARCHAR":
                    case "VARCHAR2": // SQL Server不支持VARCHAR2，映射为VARCHAR
                        sql.append("VARCHAR");
                        if (StringUtils.isNotEmpty(column.getDataLength())) {
                            sql.append("(").append(column.getDataLength()).append(")");
                        } else {
                            sql.append("(MAX)"); // SQL Server中的VARCHAR默认支持最大长度
                        }
                        break;
                    case "CHAR":
                        sql.append("CHAR");
                        if (StringUtils.isNotEmpty(column.getDataLength())) {
                            sql.append("(").append(column.getDataLength()).append(")");
                        }
                        break;
                    case "TEXT":
                        sql.append("TEXT");
                        break;
                    case "INT":
                    case "INTEGER":
                        sql.append("INT");
                        break;
                    case "BIGINT":
                        sql.append("BIGINT");
                        break;
                    case "TINYINT":
                        sql.append("TINYINT");
                        break;
                    case "DECIMAL":
                        sql.append("DECIMAL");
                        if (StringUtils.isNotEmpty(column.getDataLength())) {
                            sql.append("(").append(column.getDataLength());
                            if (StringUtils.isNotEmpty(column.getDataScale())) {
                                sql.append(", ").append(column.getDataScale());
                            }
                            sql.append(")");
                        }
                        break;
                    case "FLOAT":
                        sql.append("FLOAT");
                        break;
                    case "DOUBLE":
                        sql.append("FLOAT"); // SQL Server中没有DOUBLE，使用FLOAT
                        break;
                    case "DATE":
                        sql.append("DATE");
                        break;
                    case "DATETIME":
                        sql.append("DATETIME");
                        break;
                    case "TIME":
                        sql.append("TIME");
                        break;
                    default:
                        sql.append(columnType); // 默认处理未知类型
                        break;
                }

                // 检查是否必填
                if (!column.getNullable()) {
                    sql.append(" NOT NULL");
                }

                // 默认值处理
                if (StringUtils.isNotEmpty(column.getDataDefault())) {
                    if (columnType.equals("VARCHAR") || columnType.equals("CHAR") || columnType.equals("TEXT")) {
                        sql.append(" DEFAULT '").append(column.getDataDefault()).append("'");
                    } else {
                        sql.append(" DEFAULT ").append(column.getDataDefault());
                    }
                }

                // 加入字段到主键列表，如果是主键
                if (column.getColKey()) {
                    primaryKeys.add(column.getColName());
                }

                sql.append(",\n");
            }

            // 移除最后的逗号和换行
            sql.setLength(sql.length() - 2);
            sql.append("\n");

            // 添加主键约束
            if (!primaryKeys.isEmpty()) {
                sql.append(", PRIMARY KEY (");
                for (String pk : primaryKeys) {
                    sql.append(pk).append(", ");
                }
                sql.setLength(sql.length() - 2); // 移除最后的逗号和空格
                sql.append(")");
            }

            sql.append("\n)\n");
            sqlList.add(sql.toString());
        }


        // 添加表备注（SQL Server不直接支持表备注，但可以使用扩展属性等方式）
        if (StringUtils.isNotEmpty(tableComment)) {
            StringBuilder sql = new StringBuilder();
            sql.append("EXEC sys.sp_addextendedproperty @name = N'MS_Description', @value = N'")
                    .append(MD5Util.escapeSingleQuotes(tableComment)).append("', @level0type = N'SCHEMA', @level0name = N'dbo', @level1type = N'TABLE', @level1name = N'")
                    .append(tableName).append("'\n");
            sqlList.add(sql.toString());
        }

        // 添加字段备注
        for (DbColumn column : dbColumnList) {
            if (StringUtils.isNotEmpty(column.getColComment())) {
                StringBuilder sql = new StringBuilder();
                sql.append("EXEC sys.sp_addextendedproperty @name = N'MS_Description', @value = N'")
                        .append(MD5Util.escapeSingleQuotes(column.getColComment())).append("', @level0type = N'SCHEMA', @level0name = N'dbo', @level1type = N'TABLE', @level1name = N'")
                        .append(tableName).append("', @level2type = N'COLUMN', @level2name = N'")
                        .append(column.getColName()).append("'\n");
                sqlList.add(sql.toString());
            }
        }

        return sqlList;
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return null;
    }

    @Override
    public String tables(String dbName) {
        return "select tables.name AS TABLENAME, CAST(ep.value AS NVARCHAR(128)) AS TABLECOMMENT " +
                "from sys.tables tables LEFT JOIN sys.extended_properties ep ON ep.major_id = tables.object_id AND ep.minor_id = 0";
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return "select tables.name AS TABLENAME, CAST(ep.value AS NVARCHAR(128)) AS TABLECOMMENT " +
                "from sys.tables tables LEFT JOIN sys.extended_properties ep ON ep.major_id = tables.object_id AND ep.minor_id = 0";
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        // 如果没有传入字段，则默认使用 * 查询所有字段
        if (columns == null || columns.isEmpty()) {
            return "SELECT * FROM " + tableName;
        }

        // 根据传入的 DbColumn 列表获取所有字段名，并用逗号分隔
        String fields = columns.stream()
                .map(DbColumn::getColName)
                .collect(Collectors.joining(", "));

        // 构造最终的 SQL 查询语句
        return "SELECT " + fields + " FROM " +dbQueryProperty.getDbName()+"."+ tableName;
    }

    private static String getOrderByPart(String sql) {
        String loweredString = sql.toLowerCase();
        int orderByIndex = loweredString.indexOf("order by");
        if (orderByIndex != -1) {
            return sql.substring(orderByIndex);
        } else {
            return "";
        }
    }

    @Override
    public String buildPaginationSql(String originalSql, long offset, long count) {
        StringBuilder pagingBuilder = new StringBuilder();
        String orderby = getOrderByPart(originalSql);
        String distinctStr = "";

        String loweredString = originalSql.toLowerCase();
        String sqlPartString = originalSql;
        if (loweredString.trim().startsWith("select")) {
            int index = 6;
            if (loweredString.startsWith("select distinct")) {
                distinctStr = "DISTINCT ";
                index = 15;
            }
            sqlPartString = sqlPartString.substring(index);
        }
        pagingBuilder.append(sqlPartString);

        // if no ORDER BY is specified use fake ORDER BY field to avoid errors
        if (StringUtils.isEmpty(orderby)) {
            orderby = "ORDER BY CURRENT_TIMESTAMP";
        }
        StringBuilder sql = new StringBuilder();
        sql.append("WITH selectTemp AS (SELECT ").append(distinctStr).append("TOP 100 PERCENT ")
                .append(" ROW_NUMBER() OVER (").append(orderby).append(") as __row_number__, ").append(pagingBuilder)
                .append(") SELECT * FROM selectTemp WHERE __row_number__ BETWEEN ")
                //FIX#299：原因：mysql中limit 10(offset,size) 是从第10开始（不包含10）,；而这里用的BETWEEN是两边都包含，所以改为offset+1
                .append(offset + 1)
                .append(" AND ")
                .append(offset + count).append(" ORDER BY __row_number__");
        return sql.toString();
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return "SELECT SUM(size/128.0) AS \"usedSizeMb\" FROM sys.master_files WHERE DB_NAME(database_id) = '"+dbName+"'";
    }

    @Override
    public String getDbName() {
        return "SELECT DB_NAME() AS \"databaseName\"";
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        String sql = "MERGE INTO {tableName} USING (SELECT COUNT(1) count FROM {tableName}  WHERE {where}) c ON (c.count > 0) WHEN MATCHED THEN UPDATE SET {setValue} WHERE {where} WHEN NOT MATCHED THEN INSERT ({tableFieldName}) VALUES ({tableFieldValue})";
        sql = StringUtils
                .replace(sql, "{tableName}", tableName)
                .replace("{where}", where)
                .replace("{tableFieldName}", tableFieldName)
                .replace("{tableFieldValue}", tableFieldValue)
                .replace("{setValue}", setValue);
        return sql;
    }

    @Override
    public RowMapper<DbColumn> columnMapper() {
        return (ResultSet rs, int rowNum) -> {
            DbColumn entity = new DbColumn();
            entity.setColName(rs.getString("COLNAME"));
            entity.setDataType(rs.getString("DATATYPE"));
            entity.setDataLength(rs.getString("DATALENGTH"));
            entity.setDataPrecision(rs.getString("DATAPRECISION"));
            entity.setDataScale(rs.getString("DATASCALE"));
            entity.setColKey("1".equals(rs.getString("COLKEY")) ? true : false);
            entity.setNullable("1".equals(rs.getString("NULLABLE")) ? true : false);
            entity.setColPosition(rs.getInt("COLPOSITION"));
            entity.setDataDefault(rs.getString("DATADEFAULT"));
            entity.setColComment(rs.getString("COLCOMMENT"));
            return entity;
        };
    }

    @Override
    public RowMapper<DbTable> tableMapper() {
        return (ResultSet rs, int rowNum) -> {
            DbTable entity = new DbTable();
            entity.setTableName(rs.getString("TABLENAME"));
            entity.setTableComment(rs.getString("TABLECOMMENT"));
            return entity;
        };
    }

    @Override
    public String getTableName(DbQueryProperty property, String tableName) {
        return property.getDbName() + "." + property.getSid() + "." + tableName;
    }
}
