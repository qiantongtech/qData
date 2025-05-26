package tech.qiantong.qdata.common.database.dialect;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.fieldtypes.DM8FieldType;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.database.utils.MD5Util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2024-08-07 10:28
 **/
@Slf4j
public class DM8Dialect extends AbstractDbDialect {

    @Override
    public String columns(String dbName, String tableName) {
        return "SELECT " +
                "atc.COLUMN_NAME AS COLNAME, " +
                "atc.COLUMN_ID AS COLPOSITION, " +
                "atc.DATA_DEFAULT AS DATADEFAULT, " +
                "atc.NULLABLE AS NULLABLE, " +
                "atc.DATA_TYPE AS DATATYPE, " +
                "atc.DATA_LENGTH AS DATALENGTH, " +
                "atc.DATA_PRECISION AS DATAPRECISION, " +
                "atc.DATA_SCALE AS DATASCALE, " +
                "(CASE WHEN ac.CONSTRAINT_TYPE = 'p' THEN 1 ELSE 0 END) AS COLKEY, " +
                "acc.COMMENTS AS COLCOMMENT " +
                "FROM " +
                "ALL_TAB_COLUMNS atc " +
                "INNER JOIN ALL_COL_COMMENTS acc ON acc.COLUMN_NAME = atc.COLUMN_NAME AND acc.Table_Name = atc.Table_Name AND acc.OWNER = atc.OWNER " +
                "LEFT JOIN DBA_CONS_COLUMNS dcc ON dcc.COLUMN_NAME = atc.COLUMN_NAME AND dcc.Table_Name = atc.Table_Name AND dcc.OWNER = atc.OWNER " +
                "LEFT JOIN ALL_CONSTRAINTS ac ON ac.CONSTRAINT_NAME = dcc.CONSTRAINT_NAME AND ac.Table_Name = atc.Table_Name AND ac.OWNER = atc.OWNER " +
                "WHERE " +
                "atc.OWNER =  '" + dbName + "'" +
                "AND atc.TABLE_NAME = '" + tableName + "' " +
                "ORDER BY atc.COLUMN_ID ASC";
    }
//
//    @Override
//    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
//        return "SELECT " +
//                "atc.COLUMN_NAME AS COLNAME, " +
//                "atc.COLUMN_ID AS COLPOSITION, " +
//                "atc.DATA_DEFAULT AS DATADEFAULT, " +
//                "atc.NULLABLE AS NULLABLE, " +
//                "atc.DATA_TYPE AS DATATYPE, " +
//                "atc.DATA_LENGTH AS DATALENGTH, " +
//                "atc.DATA_PRECISION AS DATAPRECISION, " +
//                "atc.DATA_SCALE AS DATASCALE, " +
//                "(CASE WHEN ac.CONSTRAINT_TYPE = 'p' THEN 1 ELSE 0 END) AS COLKEY, " +
//                "acc.COMMENTS AS COLCOMMENT " +
//                "FROM " +
//                "ALL_TAB_COLUMNS atc " +
//                "INNER JOIN ALL_COL_COMMENTS acc ON acc.COLUMN_NAME = atc.COLUMN_NAME AND acc.Table_Name = atc.Table_Name AND acc.OWNER = atc.OWNER " +
//                "LEFT JOIN DBA_CONS_COLUMNS dcc ON dcc.COLUMN_NAME = atc.COLUMN_NAME AND dcc.Table_Name = atc.Table_Name AND dcc.OWNER = atc.OWNER " +
//                "LEFT JOIN ALL_CONSTRAINTS ac ON ac.CONSTRAINT_NAME = dcc.CONSTRAINT_NAME AND ac.Table_Name = atc.Table_Name AND ac.OWNER = atc.OWNER " +
//                "WHERE " +
//                "atc.OWNER =  '" + dbQueryProperty.getDbName() + "'" +
//                "AND atc.TABLE_NAME = '" + tableName + "' " +
//                "ORDER BY atc.COLUMN_ID ASC";
//    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT " +
                "atc.COLUMN_NAME AS COLNAME, " +
                "atc.COLUMN_ID AS COLPOSITION, " +
                "atc.DATA_DEFAULT AS DATADEFAULT, " +
                "atc.NULLABLE AS NULLABLE, " +
                "atc.DATA_TYPE AS DATATYPE, " +
                "atc.DATA_LENGTH AS DATALENGTH, " +
                "atc.DATA_PRECISION AS DATAPRECISION, " +
                "atc.DATA_SCALE AS DATASCALE, " +
                "MAX(CASE WHEN ac.CONSTRAINT_TYPE = 'p' THEN 1 ELSE 0 END) AS COLKEY, " +
                "acc.COMMENTS AS COLCOMMENT " +
                "FROM ALL_TAB_COLUMNS atc " +
                "INNER JOIN ALL_COL_COMMENTS acc ON acc.COLUMN_NAME = atc.COLUMN_NAME " +
                "AND acc.Table_Name = atc.Table_Name " +
                "AND acc.OWNER = atc.OWNER " +
                "LEFT JOIN DBA_CONS_COLUMNS dcc ON dcc.COLUMN_NAME = atc.COLUMN_NAME " +
                "AND dcc.Table_Name = atc.Table_Name " +
                "AND dcc.OWNER = atc.OWNER " +
                "LEFT JOIN ALL_CONSTRAINTS ac ON ac.CONSTRAINT_NAME = dcc.CONSTRAINT_NAME " +
                "AND ac.Table_Name = atc.Table_Name " +
                "AND ac.OWNER = atc.OWNER " +
                "WHERE atc.OWNER = '" + dbQueryProperty.getDbName() + "' " +
                "AND atc.TABLE_NAME = '" + tableName + "' " +
                "GROUP BY atc.COLUMN_NAME, atc.COLUMN_ID, atc.DATA_DEFAULT, atc.NULLABLE, " +
                "atc.DATA_TYPE, atc.DATA_LENGTH, atc.DATA_PRECISION, atc.DATA_SCALE, acc.COMMENTS " +
                "ORDER BY atc.COLUMN_ID ASC";
    }


    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(*) FROM all_tables WHERE owner = '" + dbQueryProperty.getDbName() + "' AND table_name = '" + tableName.toUpperCase() + "'";

    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        String dbName = dbQueryProperty.getDbName();

        if (StringUtils.isNotEmpty(dbName)) {
            tableName = dbName + "." + tableName;
        }

        List<String> sqlList = generateDmCreateSql(tableName, tableComment, dbColumnList);

        return sqlList;
    }

    private List<String> generateDmCreateSql(String tableName, String tableComment, List<DbColumn> columns) {
        // 达梦在语法上与 Oracle 类似，但也有差异
        List<String> sqlList = new ArrayList<>();
        StringBuilder createSql = new StringBuilder();

        createSql.append("CREATE TABLE ").append(tableName).append(" (");
        List<String> pkList = new ArrayList<>();
        for (DbColumn col : columns) {
            createSql.append("\n  ").append(col.getColName()).append(" ");
            createSql.append(mapDmColumnType(col));

            if (!col.getNullable()) {
                createSql.append(" NOT NULL");
            }
            if (StringUtils.isNotEmpty(col.getDataDefault())) {
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
        if (createSql.lastIndexOf(",") == createSql.length() - 1) {
            createSql.deleteCharAt(createSql.length() - 1);
        }
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

        if (StringUtils.isNotEmpty(tableComment)) {
            String tableCmt = "COMMENT ON TABLE " + tableName + " IS '" + MD5Util.escapeSingleQuotes(tableComment) + "'";
            sqlList.add(tableCmt);
        }
        for (DbColumn col : columns) {
            if (StringUtils.isNotEmpty(col.getColComment())) {
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

    private static String mapDmColumnType(DbColumn col) {
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
                return "INT";
            case "BIGINT":
                return "BIGINT";
            case "DECIMAL":
                return "DECIMAL(" + (length != null ? length : 10) + "," + (scale != null ? scale : 0) + ")";
            case "DATE":
            case "DATETIME":
                return "TIMESTAMP";
            case "TEXT":
            case "CLOB":
                return "TEXT"; // 达梦也可使用 CLOB
            default:
                return type;
        }
    }

    @Override
    public String tables(String dbName) {
        return "SELECT at.TABLE_NAME AS TABLENAME, atc.COMMENTS AS TABLECOMMENT " +
                "FROM ALL_TABLES at " +
                "LEFT JOIN ALL_TAB_COMMENTS atc ON atc.TABLE_NAME = at.TABLE_NAME AND atc.OWNER = at.OWNER " +
                "WHERE at.OWNER = '" + dbName + "' ";
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return "SELECT at.TABLE_NAME AS TABLENAME, atc.COMMENTS AS TABLECOMMENT " +
                "FROM ALL_TABLES at " +
                "LEFT JOIN ALL_TAB_COMMENTS atc ON atc.TABLE_NAME = at.TABLE_NAME AND atc.OWNER = at.OWNER " +
                "WHERE at.OWNER = '" + dbQueryProperty.getDbName() + "' ";
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
        return "SELECT " + fields + " FROM " + dbQueryProperty.getDbName() + "." + tableName;
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return "SELECT OWNER, SUM(BYTES) / 1024 / 1024 AS \"usedSizeMb\" FROM DBA_SEGMENTS WHERE OWNER = '" + dbName + "' AND SEGMENT_TYPE = 'TABLE' GROUP BY OWNER";
    }

    @Override
    public String getDbName() {
        return "SELECT SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA') AS \"databaseName\" FROM DUAL";
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
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return this.validateDm8Specification(tableName, tableComment, columns);
    }


    @Override
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(trainToJdbcUrl(dbQueryProperty), dbQueryProperty.getUsername(),
                    dbQueryProperty.getPassword());
            return conn.isValid(0);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DataQueryException("数据库连接失败,稍后重试");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new DataQueryException("关闭数据库连接出错");
                }
            }
        }
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

    /**
     * 检验表名、表注释和字段是否符合 DM8 数据库规范
     *
     * @param tableName    表名
     * @param tableComment 表注释
     * @param columns      字段列表
     * @return 如果有错误，则返回错误信息列表；若无错误则返回空列表
     */
    public static List<String> validateDm8Specification(String tableName, String tableComment, List<DbColumn> columns) {
        List<String> errors = new ArrayList<>();

        // 1. 校验表名
        if (tableName == null || tableName.trim().isEmpty()) {
            errors.add("表名不能为空");
        } else {
            String tn = tableName.trim();
            // DM8 规范：表名必须以大写字母开头，只能包含大写字母、数字和下划线
            if (!tn.matches("^[A-Z][A-Z0-9_]*$")) {
                errors.add("表名格式不符合DM8规范，必须以大写字母开头，且只能包含大写字母、数字和下划线");
            }
            if (tn.length() > 30) {
                errors.add("表名长度不能超过30个字符");
            }
        }

        // 2. 校验表注释
        if (tableComment == null || tableComment.trim().isEmpty()) {
            errors.add("表注释不能为空");
        }

        // 3. 校验字段列表
        if (columns == null || columns.isEmpty()) {
            errors.add("表必须至少包含一个字段");
        } else {
            for (DbColumn column : columns) {
                List<String> strings = DM8FieldType.validateColumn(column);
                if (CollectionUtils.isNotEmpty(strings)) {
                    errors.addAll(strings);
                }
            }
        }
        return errors;
    }

}
