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
 * Postgre 数据库方言
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class PostgreDialect extends AbstractDbDialect {

    @Override
    public String columns(String dbName, String tableName) {
        return "select col.column_name AS COLNAME, col.ordinal_position AS COLPOSITION, col.column_default AS DATADEFAULT, col.is_nullable AS NULLABLE, col.udt_name AS DATATYPE, " +
                "col.character_maximum_length AS DATALENGTH, col.numeric_precision AS DATAPRECISION, col.numeric_scale AS DATASCALE, des.description AS COLCOMMENT, " +
                "case when t.colname is null then 0 else 1 end as COLKEY " +
                "from information_schema.columns col left join pg_description des on col.table_name::regclass = des.objoid and col.ordinal_position = des.objsubid " +
                "left join ( " +
                "select pg_attribute.attname as colname from pg_constraint inner join pg_class on pg_constraint.conrelid = pg_class.oid " +
                "inner join pg_attribute on pg_attribute.attrelid = pg_class.oid and pg_attribute.attnum = any(pg_constraint.conkey) " +
                "where pg_class.relname = '" + tableName + "' and pg_constraint.contype = 'p' " +
                ") t on t.colname = col.column_name " +
                "where col.table_catalog = '" + dbName + "' and col.table_schema = 'public' and col.table_name = '" + tableName + "' order by col.ordinal_position ";
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return "select col.column_name AS COLNAME, col.ordinal_position AS COLPOSITION, col.column_default AS DATADEFAULT, col.is_nullable AS NULLABLE, col.udt_name AS DATATYPE, " +
                "col.character_maximum_length AS DATALENGTH, col.numeric_precision AS DATAPRECISION, col.numeric_scale AS DATASCALE, des.description AS COLCOMMENT, " +
                "case when t.colname is null then 0 else 1 end as COLKEY " +
                "from information_schema.columns col left join pg_description des on col.table_name::regclass = des.objoid and col.ordinal_position = des.objsubid " +
                "left join ( " +
                "select pg_attribute.attname as colname from pg_constraint inner join pg_class on pg_constraint.conrelid = pg_class.oid " +
                "inner join pg_attribute on pg_attribute.attrelid = pg_class.oid and pg_attribute.attnum = any(pg_constraint.conkey) " +
                "where pg_class.relname = '" + tableName + "' and pg_constraint.contype = 'p' " +
                ") t on t.colname = col.column_name " +
                "where col.table_catalog = '" + dbQueryProperty.getDbName() + "' and col.table_schema = 'public' and col.table_name = '" + tableName + "' order by col.ordinal_position ";
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(*) FROM pg_catalog.pg_tables WHERE schemaname = '" + dbQueryProperty.getDbName() + "' AND tablename = '" + tableName + "';";
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

                // 转换数据类型为PostgreSQL支持的类型
                switch (columnType) {
                    case "VARCHAR":
                    case "VARCHAR2": // PostgreSQL不支持VARCHAR2，映射为VARCHAR
                        sql.append("VARCHAR");
                        if (StringUtils.isNotEmpty(column.getDataLength())) {
                            sql.append("(").append(column.getDataLength()).append(")");
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
                        sql.append("INTEGER");
                        break;
                    case "BIGINT":
                        sql.append("BIGINT");
                        break;
                    case "TINYINT":
                        sql.append("SMALLINT"); // PostgreSQL没有TINYINT，使用SMALLINT
                        break;
                    case "DECIMAL":
                        sql.append("NUMERIC");
                        if (StringUtils.isNotEmpty(column.getDataLength())) {
                            sql.append("(").append(column.getDataLength());
                            if (StringUtils.isNotEmpty(column.getDataScale())) {
                                sql.append(", ").append(column.getDataScale());
                            }
                            sql.append(")");
                        }
                        break;
                    case "FLOAT":
                        sql.append("REAL"); // PostgreSQL的单精度浮点数
                        break;
                    case "DOUBLE":
                        sql.append("DOUBLE PRECISION");
                        break;
                    case "DATE":
                        sql.append("DATE");
                        break;
                    case "DATETIME":
                    case "TIMESTAMP":
                        sql.append("TIMESTAMP");
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


        // 添加表备注
        if (StringUtils.isNotEmpty(tableComment)) {
            StringBuilder sql = new StringBuilder();
            sql.append("COMMENT ON TABLE ").append(tableName).append(" IS '").append(MD5Util.escapeSingleQuotes(tableComment)).append("'\n");
            sqlList.add(sql.toString());
        }

        // 添加字段备注
        for (DbColumn column : dbColumnList) {
            if (StringUtils.isNotEmpty(column.getColComment())) {
                StringBuilder sql = new StringBuilder();
                sql.append("COMMENT ON COLUMN ").append(tableName).append(".")
                        .append(column.getColName()).append(" IS '")
                        .append(MD5Util.escapeSingleQuotes(column.getColComment())).append("'\n");
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
        return "select relname AS TABLENAME, cast(obj_description(relfilenode, 'pg_class') as varchar) AS TABLECOMMENT from pg_class " +
                "where relname in (select tablename from pg_tables where schemaname = 'public' and tableowner = '" + dbName + "' and position('_2' in tablename) = 0) ";
    }


    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return "select relname AS TABLENAME, cast(obj_description(relfilenode, 'pg_class') as varchar) AS TABLECOMMENT from pg_class " +
                "where relname in (select tablename from pg_tables where schemaname = 'public' and tableowner = '" + dbQueryProperty.getDbName() + "' and position('_2' in tablename) = 0) ";
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

    @Override
    public String buildPaginationSql(String originalSql, long offset, long count) {
        StringBuilder sqlBuilder = new StringBuilder(originalSql);
        sqlBuilder.append(" LIMIT ").append(count).append(" offset ").append(offset);
        return sqlBuilder.toString();
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return "SELECT ( pg_database_size ( pg_database.datname ) / 1024 / 1024 ) AS used_size_mb FROM pg_database WHERE datname = '" + dbName + "'";
    }

    @Override
    public String getDbName() {
        return "SELECT current_database()";
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        return null;
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
            entity.setNullable("YES".equals(rs.getString("NULLABLE")) ? true : false);
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
}
