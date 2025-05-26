package tech.qiantong.qdata.common.database.dialect;

import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.utils.MD5Util;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLServer 数据库方言
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class SQLServerDialect extends SQLServer2008Dialect {

    @Override
    public String buildPaginationSql(String originalSql, long offset, long count) {
        StringBuilder sqlBuilder = new StringBuilder(originalSql);
        sqlBuilder.append(" OFFSET ").append(offset).append(" ROWS FETCH NEXT ").append(count).append(" ROWS ONLY ");
        return sqlBuilder.toString();
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

}
