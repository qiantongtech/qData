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
 * Oracle Oracle11g及以下数据库方言
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class OracleDialect extends AbstractDbDialect {

    @Override
    public String columns(String dbName, String tableName) {
        if (StringUtils.isNotBlank(dbName)) {
            return "SELECT\n" +
                    "\tc.table_name,\n" +
                    "\tc.column_name AS colName,\n" +
                    "\tc.data_type AS DATATYPE,\n" +
                    "\tc.data_length AS DATALENGTH,\n" +
                    "\tc.data_precision AS DATAPRECISION,\n" +
                    "\tc.data_scale AS DATASCALE,\n" +
                    "\tc.nullable AS NULLABLE,\n" +
                    "\tc.column_id AS COLPOSITION,\n" +
                    "\tc.data_default AS DATADEFAULT,\n" +
                    "\tcm.comments AS COLCOMMENT,\n" +
                    "CASE WHEN t.column_name IS NULL THEN 0 ELSE 1 END AS COLKEY " +
                    "FROM\n" +
                    "\tall_tab_columns c\n" +
                    "\tLEFT JOIN all_col_comments cm ON cm.OWNER = '" + dbName + "' AND c.table_name = cm.table_name AND c.column_name = cm.column_name\n" +
                    "\tLEFT JOIN ( " +
                    "SELECT a.table_name, a.column_name FROM all_constraints b JOIN all_cons_columns a ON b.owner = a.owner AND b.constraint_name = a.constraint_name\n" +
                    "WHERE b.owner ='"+dbName+"' AND b.constraint_type = 'P' AND b.table_name ='"+tableName+"' "+
                    ") t on t.table_name = c.table_name and c.column_name = t.column_name " +
                    "WHERE\n" +
                    "\t c.OWNER = '" + dbName + "' \n" +
                    "\tAND c.Table_Name = '" + tableName + "'";
        } else {
            return "select columns.column_name AS colName, columns.data_type AS DATATYPE, columns.data_length AS DATALENGTH, columns.data_precision AS DATAPRECISION, " +
                    "columns.data_scale AS DATASCALE, columns.nullable AS NULLABLE, columns.column_id AS COLPOSITION, columns.data_default AS DATADEFAULT, comments.comments AS COLCOMMENT," +
                    "case when t.column_name is null then 0 else 1 end as COLKEY " +
                    "from sys.user_tab_columns columns LEFT JOIN sys.user_col_comments comments ON columns.table_name = comments.table_name AND columns.column_name = comments.column_name " +
                    "left join ( " +
                    "select col.column_name as column_name, con.table_name as table_name from user_constraints con, user_cons_columns col " +
                    "where con.constraint_name = col.constraint_name and con.constraint_type = 'P' " +
                    ") t on t.table_name = columns.table_name and columns.column_name = t.column_name " +
                    "where columns.table_name = UPPER('" + tableName + "') order by columns.column_id ";
        }
    }
    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        if (StringUtils.isNotBlank(dbQueryProperty.getDbName())) {
            return "SELECT\n" +
                    "\tc.table_name,\n" +
                    "\tc.column_name AS colName,\n" +
                    "\tc.data_type AS DATATYPE,\n" +
                    "\tc.data_length AS DATALENGTH,\n" +
                    "\tc.data_precision AS DATAPRECISION,\n" +
                    "\tc.data_scale AS DATASCALE,\n" +
                    "\tc.nullable AS NULLABLE,\n" +
                    "\tc.column_id AS COLPOSITION,\n" +
                    "\tc.data_default AS DATADEFAULT,\n" +
                    "\tcm.comments AS COLCOMMENT,\n" +
                    "CASE WHEN t.column_name IS NULL THEN 0 ELSE 1 END AS COLKEY " +
                    "FROM\n" +
                    "\tall_tab_columns c\n" +
                    "\tLEFT JOIN all_col_comments cm ON cm.OWNER = '" + dbQueryProperty.getDbName() + "' AND c.table_name = cm.table_name AND c.column_name = cm.column_name\n" +
                    "\tLEFT JOIN ( " +
                    "SELECT a.table_name, a.column_name FROM all_constraints b JOIN all_cons_columns a ON b.owner = a.owner AND b.constraint_name = a.constraint_name\n" +
                    "WHERE b.owner ='" + dbQueryProperty.getDbName() + "' AND b.constraint_type = 'P' AND b.table_name ='" + tableName + "' "+
                    ") t on t.table_name = c.table_name and c.column_name = t.column_name " +
                    "WHERE\n" +
                    "\t c.OWNER = '" + dbQueryProperty.getDbName() + "' \n" +
                    "\tAND c.Table_Name = '" + tableName + "'";
        } else {
            return "select columns.column_name AS colName, columns.data_type AS DATATYPE, columns.data_length AS DATALENGTH, columns.data_precision AS DATAPRECISION, " +
                    "columns.data_scale AS DATASCALE, columns.nullable AS NULLABLE, columns.column_id AS COLPOSITION, columns.data_default AS DATADEFAULT, comments.comments AS COLCOMMENT," +
                    "case when t.column_name is null then 0 else 1 end as COLKEY " +
                    "from sys.user_tab_columns columns LEFT JOIN sys.user_col_comments comments ON columns.table_name = comments.table_name AND columns.column_name = comments.column_name " +
                    "left join ( " +
                    "select col.column_name as column_name, con.table_name as table_name from user_constraints con, user_cons_columns col " +
                    "where con.constraint_name = col.constraint_name and con.constraint_type = 'P' " +
                    ") t on t.table_name = columns.table_name and columns.column_name = t.column_name " +
                    "where columns.table_name = UPPER('" + tableName + "') order by columns.column_id ";
        }
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(*) FROM all_tables WHERE owner = '" + dbQueryProperty.getDbName() + "' AND table_name = '" + tableName.toUpperCase() + "'";
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        String dbName = dbQueryProperty.getDbName();

        if(StringUtils.isNotEmpty(dbName)){
            tableName = dbName + "." + tableName;
        }

        List<String> sqlList =  generateOracleCreateSql(tableName,tableComment,dbColumnList);

        return sqlList;
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return null;
    }

    private List<String> generateOracleCreateSql(String tableName, String tableComment, List<DbColumn> columns) {
        List<String> sqlList = new ArrayList<>();
        StringBuilder createSql = new StringBuilder();

        createSql.append("CREATE TABLE ").append(tableName).append(" (");
        List<String> pkList = new ArrayList<>();
        for (DbColumn col : columns) {
            createSql.append("\n  ").append(col.getColName()).append(" ");
            createSql.append(mapOracleColumnType(col));

            if (!col.getNullable()) {
                String columnType = col.getDataType();
                if (isStringTypeSwitchNullableFlag(columnType)) {
                    createSql.append(" NOT NULL");
                }
            }
            if (tech.qiantong.qdata.common.utils.StringUtils.hasText(col.getDataDefault())) {
                createSql.append(" DEFAULT ").append(col.getDataDefault());
//                String columnType = col.getDataType();
//                if (isStringTypeSwitchDEFAULT(columnType)) {
//                    createSql.append(" DEFAULT '").append(MD5Util.escapeSingleQuotes(col.getDataDefault())).append("'");
//                } else {
//                    createSql.append(" DEFAULT ").append(col.getDataDefault());
//                }
            }
            if (col.getColKey()) {
                pkList.add(col.getColName());
            }
            createSql.append(",");
        }
        // 去逗号
        if (createSql.lastIndexOf(",") == createSql.length() - 1) {
            createSql.deleteCharAt(createSql.length() - 1);
        }
        // 主键
        if (!pkList.isEmpty()) {
            createSql.append(",\n  PRIMARY KEY(");
            for (String pk : pkList) {
                createSql.append(pk).append(",");
            }
            createSql.deleteCharAt(createSql.length() - 1);
            createSql.append(")");
        }
        createSql.append("\n)");
        sqlList.add(createSql.toString());

        // 表注释
        if (tech.qiantong.qdata.common.utils.StringUtils.hasText(tableComment)) {
            String tableCmt = "COMMENT ON TABLE " + tableName + " IS '" + MD5Util.escapeSingleQuotes(tableComment) + "'";
            sqlList.add(tableCmt);
        }
        // 字段注释
        for (DbColumn col : columns) {
            if (tech.qiantong.qdata.common.utils.StringUtils.hasText(col.getColComment())) {
                String colCmt = "COMMENT ON COLUMN " + tableName + "." + col.getColName()
                        + " IS '" + MD5Util.escapeSingleQuotes(col.getColComment()) + "'";
                sqlList.add(colCmt);
            }
        }

        return sqlList;
    }

    private static boolean isStringTypeSwitchDEFAULT(String columnType) {
        switch (columnType) {
            case "VARCHAR":
            case "VARCHAR2":
            case "CHAR":
            case "CLOB":
            case "TEXT":
                return true;
            default:
                return false;
        }
    }

    private static boolean isStringTypeSwitchNullableFlag(String columnType) {
        switch (columnType) {
            case "CLOB":
            case "BLOB":
            case "NCLOB":
            case "BFILE":
            case "NUMBER":
                return true;
            default:
                return false;
        }
    }

    private static String mapOracleColumnType(DbColumn col) {
        // 类似 Oracle
        String type = col.getDataType();
        Long length = MD5Util.getStringToLong(col.getDataLength());
        Long scale = MD5Util.getStringToLong(col.getDataScale());

        switch (type) {
            case "varchar":
            case "varchar2":
            case "VARCHAR":
            case "VARCHAR2":
                return "VARCHAR2(" + (length != null ? length : 255) + ")";
            case "CHAR":
                return "CHAR(" + (length != null ? length : 1) + ")";
            case "INT":
            case "INTEGER":
                String resultINT = generateColumnDefinitionOracle(
                        length
                        , 10
                        , false
                        , scale
                );
                return new StringBuilder("NUMBER").append(resultINT).toString();
            case "BIGINT":
                String resultBIGINT = generateColumnDefinitionOracle(
                        length
                        , 19
                        , false
                        , scale
                );
                return new StringBuilder("NUMBER").append(resultBIGINT).toString();
            case "DECIMAL":
                return "NUMBER(" + (length != null ? length : 10) + "," + (scale != null ? scale : 0) + ")";
            case "DATE":
                return "DATE";
            case "DATETIME":
                return "TIMESTAMP";
            case "TEXT":
            case "CLOB":
                return "CLOB";
            default:
                return type;
        }
    }

    /**
     * 根据列的长度和小数位数生成用于拼接的 SQL 字符串
     *
     * @param columnLength 列的长度（字符串表示）
     * @param maxLength    长度限制的最大值（例如 38）
     * @param includeScale 是否拼接小数位数
     * @param columnScale  列的小数位数（字符串表示，可能为空）
     * @return 生成的用于拼接的 SQL 字符串
     */
    public static String generateColumnDefinitionOracle(Long columnLength, long maxLength, boolean includeScale, Long columnScale) {
        StringBuilder sql = new StringBuilder("");

        if(columnLength == null){
            throw new UnsupportedOperationException("属性类型：格式错误，数字类型长度未填充");
        }

        // 如果 columnLength 为空，则使用 maxLength 作为默认值
        long length = columnLength;

        if (length > maxLength) {
            length = maxLength;
        }

        // 拼接长度
        sql.append("(").append(length);

        // 根据 includeScale 和 columnScale 判断是否需要拼接小数位数
        if (includeScale &&  columnScale != 0 ) {
            sql.append(", ").append(columnScale);
        }

        sql.append(")");

        return sql.toString();
    }


    @Override
    public String tables(String dbName) {
        if (StringUtils.isNotBlank(dbName)) {
            return "SELECT DISTINCT t.TABLE_NAME AS TABLENAME,c.COMMENTS AS TABLECOMMENT FROM ALL_TAB_COMMENTS c JOIN ALL_TABLES t ON c.TABLE_NAME = t.TABLE_NAME WHERE t.OWNER = '" + dbName + "'";
        } else {
            return "select tables.table_name AS TABLENAME, comments.comments AS TABLECOMMENT from sys.user_tables tables " +
                    "LEFT JOIN sys.user_tab_comments comments ON tables.table_name = comments.table_name ";
        }
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        if (StringUtils.isNotBlank(dbQueryProperty.getDbName())) {
            return "SELECT DISTINCT t.TABLE_NAME AS TABLENAME,c.COMMENTS AS TABLECOMMENT FROM ALL_TAB_COMMENTS c JOIN ALL_TABLES t ON c.TABLE_NAME = t.TABLE_NAME WHERE t.OWNER = '" + dbQueryProperty.getDbName() + "'";
        } else {
            return "select tables.table_name AS TABLENAME, comments.comments AS TABLECOMMENT from sys.user_tables tables " +
                    "LEFT JOIN sys.user_tab_comments comments ON tables.table_name = comments.table_name ";
        }
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
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM ( ");
        sqlBuilder.append(originalSql).append(" ) TMP WHERE ROWNUM <=").append((offset >= 1) ? (offset + count) : count);
        sqlBuilder.append(") WHERE ROW_ID > ").append(offset);
        return sqlBuilder.toString();
    }

    @Override
    public RowMapper<DbColumn> columnLongMapper() {
        return (ResultSet rs, int rowNum) -> {
            DbColumn entity = new DbColumn();
            entity.setDataDefault(rs.getString("DATADEFAULT"));
            return entity;
        };
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return "SELECT ROUND(SUM(bytes) / 1024 / 1024, 2) AS \"usedSizeMb\" FROM dba_segments WHERE owner = '" + dbName + "' GROUP BY owner";
    }

    @Override
    public String getDbName() {
        return "SELECT SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA')  AS \"databaseName\" FROM DUAL";
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
            entity.setColKey("1".equals(rs.getString("COLKEY")));
            entity.setNullable("Y".equals(rs.getString("NULLABLE")));
            //long类型，单独处理
            //entity.setDataDefault(rs.getString("DATADEFAULT"));
            entity.setColPosition(rs.getInt("COLPOSITION"));
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
