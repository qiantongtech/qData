package tech.qiantong.qdata.common.database.dialect;

import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.utils.MD5Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ORACLE Oracle12c+数据库方言
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class Oracle12cDialect extends OracleDialect {

    @Override
    public String buildPaginationSql(String originalSql, long offset, long count) {
        StringBuilder sqlBuilder = new StringBuilder(originalSql);
        sqlBuilder.append(" OFFSET ").append(offset).append(" ROWS FETCH NEXT ").append(count).append(" ROWS ONLY ");
        return sqlBuilder.toString();
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

}
