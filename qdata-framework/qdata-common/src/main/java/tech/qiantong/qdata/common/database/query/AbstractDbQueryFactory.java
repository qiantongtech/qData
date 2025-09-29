package tech.qiantong.qdata.common.database.query;

import cn.hutool.db.ds.simple.SimpleDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;
import tech.qiantong.qdata.common.database.DbDialect;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.core.PageResult;
import tech.qiantong.qdata.common.database.dialect.OracleDialect;
import tech.qiantong.qdata.common.database.exception.DataQueryException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
public abstract class AbstractDbQueryFactory implements DbQuery {

    protected DataSource dataSource;

    protected JdbcTemplate jdbcTemplate;


    protected DbDialect dbDialect;

    private DbQueryProperty dbQueryProperty;

    @Override
    public Connection getConnection() {
        try {
            DbType dbType = DbType.getDbType(dbQueryProperty.getDbType());
            String jdbcUrl = trainToJdbcUrl(dbQueryProperty);
            if (DbType.DM8.getDb().equals(dbType.getDb()) && jdbcUrl.startsWith("jdbc:dm")) {
                return DriverManager.getConnection(trainToJdbcUrl(dbQueryProperty), dbQueryProperty.getUsername(),
                        dbQueryProperty.getPassword());
            }
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DataQueryException("获取数据库连接出错");
        }
    }

    @Override
    public boolean valid() {
        return dbDialect.validConnection(dataSource, dbQueryProperty);
    }

    @Override
    public void close() {
        DbType dbType = DbType.getDbType(dbQueryProperty.getDbType());
        if (DbType.KAFKA.getDb().equals(dbType.getDb())) {
            return;
        }
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        } else if (dataSource instanceof SimpleDataSource) {
            ((SimpleDataSource) dataSource).close();
        } else {
            throw new DataQueryException("不合法数据源类型");
        }
    }

    @Override
    public List<DbColumn> getTableColumns(String dbName, String tableName) {
        String sql = dbDialect.columns(dbName, tableName);
        System.out.println(sql);
        if (dbDialect instanceof OracleDialect) {
            List<DbColumn> longColumns = jdbcTemplate.query(sql, dbDialect.columnLongMapper());
            List<DbColumn> queryColumns = jdbcTemplate.query(sql, dbDialect.columnMapper());
            for (int i = 0; i < longColumns.size(); i++) {
                DbColumn longColumn = longColumns.get(i);
                DbColumn otherColumn = queryColumns.get(i);
                otherColumn.setDataDefault(longColumn.getDataDefault());
            }
            return queryColumns;
        }
        return jdbcTemplate.query(sql, dbDialect.columnMapper());
    }

    @Override
    public List<DbColumn> getTableColumns(DbQueryProperty dbQueryProperty, String tableName) {
        String sql = dbDialect.columns(dbQueryProperty, tableName);
        System.out.println(sql);
        if (dbDialect instanceof OracleDialect) {
            List<DbColumn> longColumns = jdbcTemplate.query(sql, dbDialect.columnLongMapper());
            List<DbColumn> queryColumns = jdbcTemplate.query(sql, dbDialect.columnMapper());
            for (int i = 0; i < longColumns.size(); i++) {
                DbColumn longColumn = longColumns.get(i);
                DbColumn otherColumn = queryColumns.get(i);
                otherColumn.setDataDefault(longColumn.getDataDefault());
            }
            return queryColumns;
        }
        return jdbcTemplate.query(sql, dbDialect.columnMapper());
    }

    @Override
    public int generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        String sql = dbDialect.generateCheckTableExistsSQL(dbQueryProperty, tableName);
        System.out.println(sql);
        return this.isTableExists(sql);
    }

    @Override
    public List<String> generateCreateTableSQL(DbQueryProperty dbQueryProperty, String tableName, String tableComment,
                                               List<DbColumn> dbColumnList) {
        List<String> sql = dbDialect.someInternalSqlGenerator(dbQueryProperty, tableName, tableComment, dbColumnList);
        System.out.println(sql.toString());
        return sql;
    }

    @Override
    public List<DbTable> getTables(String dbName) {
        String sql = dbDialect.tables(dbName);
        return jdbcTemplate.query(sql, dbDialect.tableMapper());
    }

    @Override
    public List<DbTable> getTables(DbQueryProperty dbQueryProperty) {
        String sql = dbDialect.tables(dbQueryProperty);
        return jdbcTemplate.query(sql, dbDialect.tableMapper());
    }

    @Override
    public int count(String sql) {
        return jdbcTemplate.queryForObject(dbDialect.count(sql), Integer.class);
    }

    @Override
    public int count(String sql, Object[] args) {
        return jdbcTemplate.queryForObject(dbDialect.count(sql), args, Integer.class);
    }

    @Override
    public int count(String sql, Map<String, Object> params) {
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        return namedJdbcTemplate.queryForObject(dbDialect.count(sql), params, Integer.class);
    }

    @Override
    public int countNew(String sql, Map<String, Object> params) {
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        return namedJdbcTemplate.queryForObject(dbDialect.countNew(sql, params), params, Integer.class);
    }

    @Override
    public int countNew(String tableName, DbQueryProperty dbQueryProperty, String where) {
        String tableNameWhere = StringUtils.isEmpty(where) ? tableName : tableName + " where " + where;
        return countNew(tableNameWhere, new HashMap<>());
    }

    @Override
    public List<Map<String, Object>> queryList(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> queryDbColumnByList(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty, long offset, long size) {
        String sql = dbDialect.buildQuerySqlFields(columns, tableName, dbQueryProperty);
        String pageSql = dbDialect.buildPaginationSql(sql, offset, size);
        return jdbcTemplate.query(pageSql, new MyRowMapper());
    }

    @Override
    public List<Map<String, Object>> queryDbColumnByList(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty, String where, List<Map> orderByList, long offset, long size) {
        String sql = dbDialect.buildQuerySqlFields(columns, tableName, dbQueryProperty);
        sql = StringUtils.isEmpty(where) ? sql : sql + " where " + where;
        if (CollectionUtils.isNotEmpty(orderByList)) {
            StringBuilder orderBySql = new StringBuilder(" ORDER BY ");
            for (int i = 0; i < orderByList.size(); i++) {
                Map map = orderByList.get(i);
                String orderByColumn = MapUtils.getString(map, "orderByColumn");
                String isAsc = MapUtils.getString(map, "isAsc", "desc");

                if (tech.qiantong.qdata.common.utils.StringUtils.isNotBlank(orderByColumn)) {
                    // 拼接表名 + 字段
                    orderBySql = orderBySql.append(orderByColumn).append(" ").append(isAsc);
                    if (i < orderByList.size() - 1) {
                        orderBySql.append(", ");
                    }
                }
            }
            // 最终拼好的 orderBySql
            String finalOrderBy = orderBySql.toString();
            sql += finalOrderBy;
        }
        String pageSql = dbDialect.buildPaginationSql(sql, offset, size);
        return jdbcTemplate.query(pageSql, new MyRowMapper());
    }

    @Override
    public List<Map<String, Object>> queryList(String sql, Object[] args) {
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public PageResult<Map<String, Object>> queryByPage(String sql, long offset, long size) {
        int total = count(sql);
        String pageSql = dbDialect.buildPaginationSql(sql, offset, size);
        List<Map<String, Object>> records = jdbcTemplate.queryForList(pageSql);
        return new PageResult<>(total, records);
    }

    @Override
    public PageResult<Map<String, Object>> queryByPage(String sql, Object[] args, long offset, long size) {
        int total = count(sql, args);
        String pageSql = dbDialect.buildPaginationSql(sql, offset, size);
        List<Map<String, Object>> records = jdbcTemplate.queryForList(pageSql, args);
        return new PageResult<>(total, records);
    }

    @Override
    public PageResult<Map<String, Object>> queryByPage(String sql, Map<String, Object> params, long offset, long size,
                                                       Integer cache) {
        int total = count(sql, params);
        String pageSql = dbDialect.buildPaginationSql(sql, offset, size);
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> records = namedJdbcTemplate.queryForList(pageSql, params);
        return new PageResult<>(total, records);
    }

    /**
     * 查询结果列表带查询参数
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> queryList(String sql, Map<String, Object> params, Integer cache) {
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> records = namedJdbcTemplate.queryForList(sql, params);
        return records;
    }

    /**
     * 查询详情结果带查询参数
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> queryOne(String sql, Map<String, Object> params, Integer cache) {
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> records = namedJdbcTemplate.queryForList(sql, params);
        if (records.size() > 0) {
            return records.get(0);
        }
        return null;
    }

    @Override
    public int update(String sql) {
        return jdbcTemplate.update(sql);
    }

    @Override
    public void execute(String sql) {
        jdbcTemplate.execute(sql);
    }

    @Override
    public int[] batchUpdate(String sql) {
        jdbcTemplate.execute(sql);

        return jdbcTemplate.batchUpdate(sql);
    }

    @Override
    public int isTableExists(String sql) {
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

        return count == null ? 0 : count;
    }

    protected String trainToJdbcUrl(DbQueryProperty property) {
        String url = DbType.getDbType(property.getDbType()).getUrl();
        if (StringUtils.isEmpty(url)) {
            throw new DataQueryException("无效数据库类型!");
        }
        url = url.replace("${host}", property.getHost());
        url = url.replace("${port}", String.valueOf(property.getPort()));
        if (DbType.ORACLE.getDb().equals(property.getDbType())
                || DbType.ORACLE_12C.getDb().equals(property.getDbType())) {
            url = url.replace("${sid}", property.getSid());
        } else {
            url = url.replace("${dbName}", property.getDbName());
        }
        return url;
    }

    @Override
    public Integer getDataStorageSize() {
        // 获取数据库名或模式名
        String dbNameSql = dbDialect.getDbName();
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> dbNameResult = namedJdbcTemplate.queryForList(dbNameSql, new HashMap<>());
        String dbName = null;
        if (dbNameResult.size() > 0) {
            Map<String, Object> data = dbNameResult.get(0);
            if (data.containsKey("databaseName")) {
                dbName = String.valueOf(dbNameResult.get(0).get("databaseName"));
            }
        }
        if (StringUtils.isEmpty(dbName)) {
            throw new DataQueryException("无效数据库类型!");
        }
        String dataStorageSizeSql = dbDialect.getDataStorageSize(dbName);
        List<Map<String, Object>> dataStorageSizeResult = namedJdbcTemplate.queryForList(dataStorageSizeSql,
                new HashMap<>());

        // 获取存储量
        Integer dataStorageSize = 0;
        if (dataStorageSizeResult.size() > 0) {
            Map<String, Object> data = dataStorageSizeResult.get(0);
            if (data.containsKey("usedSizeMb")) {
                dataStorageSize = new BigDecimal(String.valueOf(data.get("usedSizeMb"))).intValue();
            }
        }
        return dataStorageSize;
    }

    @Override
    public Boolean copyTable(Connection conn, DbQueryProperty dbQueryProperty, String tableName, String newTableName) {
        try {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(dbQueryProperty.getDbName())) {
                tableName = org.apache.commons.lang3.StringUtils.replace(tableName, dbQueryProperty.getDbName() + ".", "");
                newTableName = org.apache.commons.lang3.StringUtils.replace(newTableName, dbQueryProperty.getDbName() + ".", "");
            }
//            this.dbDialect.get
            List<DbColumn> dbColumnList = this.getTableColumns(dbQueryProperty, tableName);

            //获取表注释
            List<DbTable> dbTableList = this.getTables(dbQueryProperty.getDbName());
            String finalTableName = tableName;
            List<DbTable> dbTable = dbTableList.stream().filter(e -> e.getTableName().equals(finalTableName)).collect(Collectors.toList());
            String tableComment = "";
            if (dbTable.size() > 0 && !StringUtils.isEmpty(dbTable.get(0).getTableComment())) {
                tableComment = dbTable.get(0).getTableComment();
            }
            List<String> tableSQLList = this.generateCreateTableSQL(dbQueryProperty, newTableName, tableComment, dbColumnList);
            for (String sql : tableSQLList) {
                this.execute(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String getInsertOrUpdateSql(DbQueryProperty writerProperty, String tableName, List<String> selectedColumns, List<String> column) {
        if (org.apache.commons.lang3.StringUtils.equals(DbType.KINGBASE8.getDb(), writerProperty.getDbType())) {
            tableName = writerProperty.getDbName() + "." + writerProperty.getSid() + "." + tableName;
        } else if (!StringUtils.isEmpty(writerProperty.getDbName())) {
            tableName = writerProperty.getDbName() + "." + tableName;
        }
        List<String> valueHolders = new ArrayList<String>(column.size());
        for (int i = 0; i < column.size(); i++) {
            valueHolders.add("?");
        }
        String writeDataSqlTemplate;
        if (dbQueryProperty.getDbType().equals(DbType.MYSQL.getDb())) {
            writeDataSqlTemplate = new StringBuilder()
                    .append("INSERT INTO %s (").append(org.apache.commons.lang3.StringUtils.join(column, ","))
                    .append(") VALUES(").append(org.apache.commons.lang3.StringUtils.join(valueHolders, ","))
                    .append(")")
                    .append(onDuplicateKeyUpdateString(column))
                    .toString();
        } else {
            writeDataSqlTemplate = new StringBuilder().append(onMergeIntoDoString(selectedColumns, column)).append("INSERT (")
                    .append(org.apache.commons.lang3.StringUtils.join(column, ","))
                    .append(") VALUES(").append(org.apache.commons.lang3.StringUtils.join(valueHolders, ","))
                    .append(")").toString();
        }
        writeDataSqlTemplate = String.format(writeDataSqlTemplate, tableName);
        return writeDataSqlTemplate;
    }

    public static String onDuplicateKeyUpdateString(List<String> columnHolders) {
        if (columnHolders == null || columnHolders.size() < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" ON DUPLICATE KEY UPDATE ");
        boolean first = true;
        for (String column : columnHolders) {
            if (!first) {
                sb.append(",");
            } else {
                first = false;
            }
            sb.append(column);
            sb.append("=VALUES(");
            sb.append(column);
            sb.append(")");
        }

        return sb.toString();
    }

    public static String onMergeIntoDoString(List<String> selectedColumns, List<String> column) {
        StringBuilder sb = new StringBuilder();
        sb.append("MERGE INTO %s A USING ( SELECT ");

        boolean first = true;
        boolean first1 = true;
        StringBuilder str = new StringBuilder();
        StringBuilder update = new StringBuilder();
        for (String columnHolder : column) {
            if (selectedColumns.contains(columnHolder)) {
                if (!first) {
                    sb.append(",");
                    str.append(" AND ");
                } else {
                    first = false;
                }
                str.append("TMP.").append(columnHolder);
                sb.append("?");
                str.append(" = ");
                sb.append(" AS ");
                str.append("A.").append(columnHolder);
                sb.append(columnHolder);
            }
        }

        for (String columnHolder : column) {
            if (!selectedColumns.contains(columnHolder)) {
                if (!first1) {
                    update.append(",");
                } else {
                    first1 = false;
                }
                update.append(columnHolder);
                update.append(" = ");
                update.append("?");
            }
        }

        sb.append(" FROM DUAL ) TMP ON (");
        sb.append(str);
        sb.append(" ) WHEN MATCHED THEN UPDATE SET ");
        sb.append(update);
        sb.append(" WHEN NOT MATCHED THEN ");
        return sb.toString();
    }

    @Override
    public List<DbColumn> getColumnsByQuerySql(String querySql) {
        List<DbColumn> res = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(querySql);

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                res.add(DbColumn.builder()
                        .colName(metaData.getColumnName(i))
                        .dataType(metaData.getColumnTypeName(i))
                        .dataLength(String.valueOf(metaData.getColumnDisplaySize(i)))
                        .dataPrecision(String.valueOf(metaData.getPrecision(i)))
                        .dataScale(String.valueOf(metaData.getScale(i)))
                        .colKey(false)
                        .nullable(metaData.isNullable(i) == 1)
                        .colPosition(i)
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataQueryException("sql解析失败!");
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

        return res;
    }
}
