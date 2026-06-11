/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.common.database.query;

import cn.hutool.db.ds.simple.SimpleDataSource;
import com.alibaba.fastjson2.JSONObject;
import com.zaxxer.hikari.HikariDataSource;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.database.DbDialect;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.core.*;
import tech.qiantong.qdata.common.database.datasource.AbstractDataSourceFactory;
import tech.qiantong.qdata.common.database.datasource.DefaultDataSourceFactoryBean;
import tech.qiantong.qdata.common.database.dialect.FileDialect;
import tech.qiantong.qdata.common.database.dialect.OracleDialect;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.exception.ServiceException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Setter
public abstract class AbstractDbQueryFactory implements DbQuery {

    AbstractDataSourceFactory dataSourceFactory = new DefaultDataSourceFactoryBean();

    protected DataSource dataSource;

    protected JdbcTemplate jdbcTemplate;

    private RedisClient redisClient;
    protected StatefulRedisConnection<String, String> redisConnection;


    protected DbDialect dbDialect;

    private DbQueryProperty dbQueryProperty;

    // JDBC 类型 → Flink SQL 类型（简化版，可按需扩展）
    private static final Map<String, String> TYPE_MAPPING = new HashMap<>();

    static {
        TYPE_MAPPING.put("VARCHAR", "STRING");
        TYPE_MAPPING.put("LONGVARCHAR", "STRING");
        TYPE_MAPPING.put("CHAR", "STRING");
        TYPE_MAPPING.put("INTEGER", "INT");
        TYPE_MAPPING.put("INT", "INT");
        TYPE_MAPPING.put("INT2", "INT");
        TYPE_MAPPING.put("INT4", "INT");
        TYPE_MAPPING.put("INT8", "BIGINT");
        TYPE_MAPPING.put("INT16", "BIGINT");
        TYPE_MAPPING.put("SERIAL", "INT");
        TYPE_MAPPING.put("BIGSERIAL", "BIGINT");
        TYPE_MAPPING.put("SMALLSERIAL", "INT");
        TYPE_MAPPING.put("NAME", "STRING");          // 系统表常用
        TYPE_MAPPING.put("BYTEA", "BYTES");          // 兼容 PostgreSQL
        TYPE_MAPPING.put("BIGINT", "BIGINT");
        TYPE_MAPPING.put("DECIMAL", "DECIMAL");
        TYPE_MAPPING.put("NUMERIC", "DECIMAL");
        TYPE_MAPPING.put("NUMBER", "DECIMAL");
        TYPE_MAPPING.put("DOUBLE", "DOUBLE");
        TYPE_MAPPING.put("FLOAT", "FLOAT");
        TYPE_MAPPING.put("FLOAT2", "DOUBLE");
        TYPE_MAPPING.put("FLOAT4", "DOUBLE");
        TYPE_MAPPING.put("FLOAT8", "DOUBLE");
        TYPE_MAPPING.put("FLOAT16", "DOUBLE");
        TYPE_MAPPING.put("BOOLEAN", "BOOLEAN");
        TYPE_MAPPING.put("DATE", "DATE");
        TYPE_MAPPING.put("TIMESTAMP", "TIMESTAMP");
        TYPE_MAPPING.put("TIMESTAMP_WITH_TIMEZONE", "TIMESTAMP_LTZ");


        /* ---------- 1. 标准 JDBC 类型（JSR-221） ---------- */
        TYPE_MAPPING.put("VARCHAR", "STRING");
        TYPE_MAPPING.put("LONGVARCHAR", "STRING");
        TYPE_MAPPING.put("CHAR", "STRING");
        TYPE_MAPPING.put("CLOB", "STRING");
        TYPE_MAPPING.put("NVARCHAR", "STRING");
        TYPE_MAPPING.put("NCHAR", "STRING");
        TYPE_MAPPING.put("NCLOB", "STRING");

        TYPE_MAPPING.put("INTEGER", "INT");
        TYPE_MAPPING.put("INT", "INT");
        TYPE_MAPPING.put("TINYINT", "TINYINT");
        TYPE_MAPPING.put("SMALLINT", "SMALLINT");
        TYPE_MAPPING.put("BIGINT", "BIGINT");

        TYPE_MAPPING.put("DECIMAL", "DECIMAL");
        TYPE_MAPPING.put("NUMERIC", "DECIMAL");

        TYPE_MAPPING.put("FLOAT", "FLOAT");
        TYPE_MAPPING.put("REAL", "FLOAT");
        TYPE_MAPPING.put("DOUBLE", "DOUBLE");

        TYPE_MAPPING.put("BOOLEAN", "BOOLEAN");
        TYPE_MAPPING.put("BIT", "BOOLEAN");

        TYPE_MAPPING.put("DATE", "DATE");
        TYPE_MAPPING.put("TIME", "TIME");
        TYPE_MAPPING.put("TIMESTAMP", "TIMESTAMP");
        TYPE_MAPPING.put("TIMESTAMP_WITH_TIMEZONE", "TIMESTAMP_LTZ");

        TYPE_MAPPING.put("BINARY", "BYTES");
        TYPE_MAPPING.put("VARBINARY", "BYTES");
        TYPE_MAPPING.put("LONGVARBINARY", "BYTES");
        TYPE_MAPPING.put("BLOB", "BYTES");

        /* ----------  MySQL 8 特有 ---------- */
        TYPE_MAPPING.put("TEXT", "STRING");
        TYPE_MAPPING.put("LONGTEXT", "STRING");
        TYPE_MAPPING.put("MEDIUMTEXT", "STRING");
        TYPE_MAPPING.put("TINYTEXT", "STRING");
        TYPE_MAPPING.put("JSON", "STRING");          // Flink 无 JSON，先用 STRING
        TYPE_MAPPING.put("YEAR", "INT");
        TYPE_MAPPING.put("DATETIME", "TIMESTAMP");
        TYPE_MAPPING.put("TIMESTAMP", "TIMESTAMP");  // MySQL 的 TIMESTAMP 实质 TIMESTAMP_LTZ，但列名已覆盖


        /* ----------  Oracle 19c 特有 ---------- */
        TYPE_MAPPING.put("VARCHAR2", "STRING");
        TYPE_MAPPING.put("NVARCHAR2", "STRING");
        TYPE_MAPPING.put("BINARY_FLOAT", "FLOAT");
        TYPE_MAPPING.put("BINARY_DOUBLE", "DOUBLE");
        TYPE_MAPPING.put("CLOB", "STRING");
        TYPE_MAPPING.put("BLOB", "BYTES");
        TYPE_MAPPING.put("RAW", "BYTES");
        TYPE_MAPPING.put("LONG RAW", "BYTES");

        /* ---------- 达梦 DM8 ---------- */
        TYPE_MAPPING.put("TEXT", "STRING");
        TYPE_MAPPING.put("LONG", "STRING");
        TYPE_MAPPING.put("IMAGE", "BYTES");
        TYPE_MAPPING.put("SIGNED", "BIGINT");        // DM 的整数类型别名
        TYPE_MAPPING.put("BIGINT", "BIGINT");        // 再次确认

        /* ----------  PostgreSQL 15 ---------- */
        TYPE_MAPPING.put("UUID", "STRING");          // Flink 无 UUID
        TYPE_MAPPING.put("JSON", "STRING");
        TYPE_MAPPING.put("JSONB", "STRING");
        TYPE_MAPPING.put("SERIAL2", "SMALLINT");
        TYPE_MAPPING.put("SERIAL4", "INT");
        TYPE_MAPPING.put("SERIAL8", "BIGINT");

        /* ----------  SQL Server 2022 ---------- */
        TYPE_MAPPING.put("NVARCHAR", "STRING");
        TYPE_MAPPING.put("NTEXT", "STRING");
        TYPE_MAPPING.put("DATETIME2", "TIMESTAMP");
        TYPE_MAPPING.put("DATETIMEOFFSET", "TIMESTAMP_LTZ");
        TYPE_MAPPING.put("SMALLDATETIME", "TIMESTAMP");
        TYPE_MAPPING.put("UNIQUEIDENTIFIER", "STRING"); // UUID
        TYPE_MAPPING.put("SQL_VARIANT", "STRING");   // 万能类型，先转 STRING
        TYPE_MAPPING.put("GEOGRAPHY", "STRING");     // 空间类型先转 STRING
        TYPE_MAPPING.put("GEOMETRY", "STRING");
    }

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
        if (DbType.KAFKA.getDb().equals(dbType.getDb())
                || DbType.HDFS.getDb().equals(dbType.getDb())
                || DbType.RABBITMQ.getDb().equals(dbType.getDb())
                || DbType.FTP.getDb().equals(dbType.getDb())
                || DbType.OSS_ALIYUN.getDb().equals(dbType.getDb())) {
            return;
        } else if (DbType.REDIS.getDb().equals(dbType.getDb())) {
            if (redisConnection != null) {
                redisConnection.close();   // 关闭连接
            }
            if (redisClient != null) {
                redisClient.shutdown();    // 关闭客户端资源
            }
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
    public List<DbColumn> getDbColumns(DbQueryProperty dbQueryProperty) {
        String sql = dbDialect.getDbColumns(dbQueryProperty);
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
        List<DbColumn> query = jdbcTemplate.query(sql, dbDialect.columnMapper());
        if (CollectionUtils.isEmpty(query)) {
            return query;
        }
        if (DbType.DORIS.getDb().equals(dbQueryProperty.getDbType())) {
            String pkColumnNames = dbDialect.getPkColumnNames(dbQueryProperty);
            List<String> pkList = new ArrayList<>();

            jdbcTemplate.query(pkColumnNames, rs -> {
                String createTableSql = rs.getString(2); // 第2列为完整DDL
                Pattern pattern = Pattern.compile("(UNIQUE|PRIMARY) KEY\\s*\\(([^)]+)\\)", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(createTableSql);
                if (matcher.find()) {
                    String keys = matcher.group(2);
                    Arrays.stream(keys.split(","))
                            .map(col -> col.replace("`", "").trim())
                            .forEach(pkList::add);
                }
            });
            for (DbColumn column : query) {
                column.setColKey(pkList.contains(column.getColName()));
            }
        }
        return query;
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
        List<DbColumn> query = jdbcTemplate.query(sql, dbDialect.columnMapper());
        if (CollectionUtils.isEmpty(query)) {
            return query;
        }
        if (DbType.DORIS.getDb().equals(dbQueryProperty.getDbType())) {
            String pkColumnNames = dbDialect.getPkColumnNames(dbQueryProperty, tableName);
            List<String> pkList = new ArrayList<>();

            jdbcTemplate.query(pkColumnNames, rs -> {
                String createTableSql = rs.getString(2); // 第2列为完整DDL
                Pattern pattern = Pattern.compile("(UNIQUE|PRIMARY) KEY\\s*\\(([^)]+)\\)", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(createTableSql);
                if (matcher.find()) {
                    String keys = matcher.group(2);
                    Arrays.stream(keys.split(","))
                            .map(col -> col.replace("`", "").trim())
                            .forEach(pkList::add);
                }
            });
            for (DbColumn column : query) {
                column.setColKey(pkList.contains(column.getColName()));
            }
        }
        return query;
    }

    @Override
    public int generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        String sql = dbDialect.generateCheckTableExistsSQL(dbQueryProperty, tableName);
        System.out.println(sql);
        //hive不支持
        if (DbType.HIVE.getDb().equals(dbQueryProperty.getDbType())) {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
            return CollectionUtils.isEmpty(result) ? 0 : result.size();
        } else {
            return this.isTableExists(sql);
        }
    }

    @Override
    public List<String> generateCreateTableSQL(DbQueryProperty dbQueryProperty, String tableName, String tableComment,
                                               List<DbColumn> dbColumnList) {
        List<String> sql = dbDialect.someInternalSqlGenerator(dbQueryProperty, tableName, tableComment, dbColumnList);
        return sql;
    }

    @Override
    public List<String> generateDorisCreateTableSQL(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList, String partitionRule, String bucketRule, Integer replica) {
//        DbDialect otherDialect = DialectFactory.getDialect(DbType.getDbType(dbQueryProperty.getDbType()));
        List<String> sql = dbDialect.someInternalSqlDorisGenerator(dbQueryProperty, tableName, tableComment, dbColumnList, partitionRule, bucketRule, replica);
        return sql;
    }

    @Override
    public int createCollectionWithSchema(DbQueryProperty dbQueryProperty, String tableName, String tableComment,
                                          List<DbColumn> dbColumnList) {

        List<String> sql = dbDialect.someInternalSqlGenerator(dbQueryProperty, tableName, tableComment, dbColumnList);

        for (String string : sql) {
            this.execute(string);
        }
        return 1;
    }

    @Override
    public List<DbTable> getTables(String dbName) {
        String sql = dbDialect.tables(dbName);
        return jdbcTemplate.query(sql, dbDialect.tableMapper());
    }

    @Override
    public List<DbTable> getTables(DbQueryProperty dbQueryProperty) {
        if (DbType.HIVE.getDb().equals(dbQueryProperty.getDbType())) {
            String sql = dbDialect.tables(dbQueryProperty);
            List<DbTable> query = jdbcTemplate.query(sql, dbDialect.tableMapper());
            for (DbTable dbTable : query) {
                String tablesCommentSql = dbDialect.tablesComment(dbQueryProperty, dbTable.getTableName());
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(tablesCommentSql);
                String comment = null;
                for (Map<String, Object> row : rows) {
                    if ("comment".equalsIgnoreCase(String.valueOf(row.get("col_name")))) {
                        comment = String.valueOf(row.get("data_type")); // Hive 会把注释放在这一列
                        break;
                    }
                }
                dbTable.setTableComment(comment);
            }
            return query;
        } else {
            String sql = dbDialect.tables(dbQueryProperty);
            return jdbcTemplate.query(sql, dbDialect.tableMapper());
        }
    }

    @Override
    public List<DbName> getDbNames(DbName dbName) {
        int level = dbName == null ? 1 : dbName.getLevel() + 1;
        if (DbType.KINGBASE8.getDb().equals(dbQueryProperty.getDbType())
                || DbType.POSTGRE_SQL.getDb().equals(dbQueryProperty.getDbType())) {

        }

        String sql = dbDialect.getDbName(dbName);
        return jdbcTemplate.query(sql, dbDialect.firstLevelMapper(level));
    }

    @Override
    public List<FileInfo> getFiles(String path) {
        if (dbDialect instanceof FileDialect) {
            FileDialect fileDialect = (FileDialect) dbDialect;
            return fileDialect.getFiles(dbQueryProperty, path);
        }
        throw new ServiceException("当前数据源不支持获取文件列表");
    }

    @Override
    public int executeCountSql(String sql) {
        return jdbcTemplate.queryForObject(sql, Integer.class);
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
        return countNew(dbDialect.buildTableNameByDbType(dbQueryProperty, StringUtils.isEmpty(where) ? tableName : tableName + " where " + where), new HashMap<>());
    }

    @Override
    public List<Map<String, Object>> queryList(String sql) {
        return jdbcTemplate.query(sql, new MyRowMapper());
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

    /**
     * 新增，暂时仅支持MONGODB
     *
     * @param tableName
     * @param after
     * @return
     */
    @Override
    public int addTableData(String tableName, Map<String, Object> after) {
        return 0;
    }

    @Override
    public int updateTableData(Map<String, Object> after,
                               List<String> setCols,
                               List<String> whereCols,
                               String tableName) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> queryList(String sql, Object[] args) {
        return jdbcTemplate.query(sql, new MyRowMapper(), args);
    }

    @Override
    public PageResult<Map<String, Object>> queryByPage(String sql, long offset, long size) {
        int total = count(sql);
        String pageSql = dbDialect.buildPaginationSql(sql, offset, size);
        List<Map<String, Object>> records = jdbcTemplate.query(pageSql, new MyRowMapper());
        if (records.size() > 0) {
            records.forEach(record -> {
                if (record.containsKey("__row_number__")) {
                    record.remove("__row_number__");
                }
            });
        }
        return new PageResult<>(total, records);
    }

    @Override
    public PageResult<Map<String, Object>> queryByPage(String sql, Object[] args, long offset, long size) {
        int total = count(sql, args);
        String pageSql = dbDialect.buildPaginationSql(sql, offset, size);
        List<Map<String, Object>> records = jdbcTemplate.query(pageSql, new MyRowMapper(), args);
        return new PageResult<>(total, records);
    }

    @Override
    public PageResult<Map<String, Object>> queryByPage(String sql, Map<String, Object> params, long offset, long size,
                                                       Integer cache) {
        int total = count(sql, params);
        String pageSql = dbDialect.buildPaginationSql(sql, offset, size);
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> records = namedJdbcTemplate.query(pageSql, params, new MyRowMapper());
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
        List<Map<String, Object>> records = namedJdbcTemplate.query(sql, params, new MyRowMapper());
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
        MyRowMapper mapper = new MyRowMapper();
        return namedJdbcTemplate.query(sql, params, rs -> {
            if (rs.next()) {
                // 只映射第一行，不再组装 List
                return mapper.mapRow(rs, 1);
            }
            return null;
        });
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
        List<Map<String, Object>> dbNameResult = namedJdbcTemplate.query(dbNameSql, new MyRowMapper());
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
        List<Map<String, Object>> dataStorageSizeResult = namedJdbcTemplate.query(dataStorageSizeSql, new MyRowMapper());

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
            //判断是否是 Hive
            if (dbQueryProperty.getDbType().equals(DbType.HIVE.getDb())) {
                String sql = "CREATE TABLE " + newTableName + " LIKE " + tableName;
                this.execute(sql);
                return true;
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(dbQueryProperty.getDbName())) {
                tableName = org.apache.commons.lang3.StringUtils.replace(tableName, dbQueryProperty.getDbName() + ".", "");
                newTableName = org.apache.commons.lang3.StringUtils.replace(newTableName, dbQueryProperty.getDbName() + ".", "");
            }
            if (org.apache.commons.lang3.StringUtils.equals(DbType.DB2.getDb(), dbQueryProperty.getDbType()) ||
                    org.apache.commons.lang3.StringUtils.equals(DbType.OSCAR.getDb(), dbQueryProperty.getDbType())) {
                tableName = org.apache.commons.lang3.StringUtils.replace(tableName, dbQueryProperty.getSid() + ".", "");
                newTableName = org.apache.commons.lang3.StringUtils.replace(newTableName, dbQueryProperty.getSid() + ".", "");
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
    public Boolean copyTableToOtherDb(DbQueryProperty otherDbQueryProperty, String tableName, String newTableName, String newTableComment, List<JSONObject> addColumn, String partitionRule, String bucketRule, Integer replica) {
        try {
            List<DbColumn> dbColumnList = this.getTableColumns(dbQueryProperty, tableName);

            //获取表注释
            List<DbTable> dbTableList = this.getTables(dbQueryProperty.getDbName());
            if (addColumn != null && addColumn.size() > 0) {
                for (JSONObject jsonObject : addColumn) {
                    DbColumn dbColumn = new DbColumn();
                    dbColumn.setColName(jsonObject.getString("columnName"));
                    dbColumn.setColComment(jsonObject.getString("columnComment"));
                    dbColumn.setDataType(jsonObject.getString("columnType"));
                    dbColumn.setDataLength(jsonObject.getString("columnLength"));
                    dbColumn.setDataScale(jsonObject.containsKey("columnScale") ? jsonObject.getString("columnScale") : null);
                    dbColumn.setColKey(jsonObject.getBooleanValue("colKey", false));
                    dbColumn.setNullable(jsonObject.getBooleanValue("nullable", true));
                    dbColumn.setDataDefault(jsonObject.getString("defaultValue"));
                    dbColumnList.add(dbColumn);
                }
            }
            //将主键抽取到最上面
            List<DbColumn> keyColumns = dbColumnList.stream().filter(e -> e.getColKey()).collect(Collectors.toList());
            List<DbColumn> nonKeyColumns = dbColumnList.stream().filter(e -> !e.getColKey()).collect(Collectors.toList());
            dbColumnList.clear();
            dbColumnList.addAll(keyColumns);
            dbColumnList.addAll(nonKeyColumns);
            List<String> tableSQLList = this.generateDorisCreateTableSQL(otherDbQueryProperty, newTableName, newTableComment, dbColumnList, partitionRule, bucketRule, replica);
            DbQuery writerDbQuery = dataSourceFactory.createDbQuery(otherDbQueryProperty);
            for (String sql : tableSQLList) {
                writerDbQuery.execute(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String getInsertOrUpdateSql(DbQueryProperty writerProperty, String tableName, List<String> selectedColumns, List<String> column) {
        if (org.apache.commons.lang3.StringUtils.equals(DbType.KINGBASE8.getDb(), writerProperty.getDbType()) ||
                org.apache.commons.lang3.StringUtils.equals(DbType.SQL_SERVER.getDb(), writerProperty.getDbType()) ||
                org.apache.commons.lang3.StringUtils.equals(DbType.DB2.getDb(), writerProperty.getDbType())) {
            tableName = writerProperty.getDbName() + "." + writerProperty.getSid() + "." + tableName;
        } else if (org.apache.commons.lang3.StringUtils.equals(DbType.OSCAR.getDb(), writerProperty.getDbType())) {
            tableName = writerProperty.getSid() + "." + tableName;
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
        } else if (dbQueryProperty.getDbType().equals(DbType.DORIS.getDb())) {
            writeDataSqlTemplate = new StringBuilder()
                    .append("INSERT INTO %s (").append(org.apache.commons.lang3.StringUtils.join(column, ","))
                    .append(") VALUES(").append(org.apache.commons.lang3.StringUtils.join(valueHolders, ","))
                    .append(")")
                    .toString();
        } else if (dbQueryProperty.getDbType().equals(DbType.SQL_SERVER.getDb()) || dbQueryProperty.getDbType().equals(DbType.SQL_SERVER2008.getDb())) {
            writeDataSqlTemplate = new StringBuilder().append(onMergeIntoSQLServerDoString(selectedColumns, column)).append("INSERT (")
                    .append(org.apache.commons.lang3.StringUtils.join(column, ","))
                    .append(") VALUES(").append(org.apache.commons.lang3.StringUtils.join(valueHolders, ","))
                    .append(");").toString();
        } else if (dbQueryProperty.getDbType().equals(DbType.DB2.getDb())) {
            writeDataSqlTemplate = new StringBuilder().append(onMergeIntoDB2DoString(selectedColumns, column)).append("INSERT (")
                    .append(org.apache.commons.lang3.StringUtils.join(column, ","))
                    .append(") VALUES(").append(org.apache.commons.lang3.StringUtils.join(valueHolders, ","))
                    .append(")").toString();
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

    public static String onMergeIntoDB2DoString(List<String> selectedColumns, List<String> column) {
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

        sb.append(" FROM SYSIBM.SYSDUMMY1 ) TMP ON (");
        sb.append(str);
        sb.append(" ) WHEN MATCHED THEN UPDATE SET ");
        sb.append(update);
        sb.append(" WHEN NOT MATCHED THEN ");
        return sb.toString();
    }


    public static String onMergeIntoSQLServerDoString(List<String> selectedColumns, List<String> column) {
        StringBuilder sb = new StringBuilder();
        sb.append("MERGE INTO %s A ");

        StringBuilder columnValueStr = new StringBuilder();
        StringBuilder columnStr = new StringBuilder();
        StringBuilder updateSetStr = new StringBuilder();
        for (int i = 0; i < column.size(); i++) {
            if (i != 0) {
                columnValueStr.append(",");
                columnStr.append(",");
            }
            columnValueStr.append("?");
            columnStr.append(column.get(i));
        }

        Boolean first = true;
        for (int i = 0; i < column.size(); i++) {
            if (selectedColumns.contains(column.get(i))) {
                continue;
            }
            if (!first) {
                updateSetStr.append(",");
            }
            updateSetStr.append(column.get(i)).append(" = TMP.").append(column.get(i));
            if (first) {
                first = false;
            }
        }

        sb.append(" USING ( VALUES (").append(columnValueStr).append(" ) ) AS TMP( ").append(columnStr).append(")");

        sb.append(" ON ");
        for (int i = 0; i < selectedColumns.size(); i++) {
            String selectedColumn = selectedColumns.get(i);
            if (i != 0) {
                sb.append(" AND ");
            }
            sb.append(" A.").append(selectedColumn);
            sb.append(" = ");
            sb.append(" TMP.").append(selectedColumn);
        }

        sb.append(" WHEN MATCHED THEN UPDATE SET ");

        sb.append(updateSetStr);

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

    @Override
    public void uploadFile(String path, MultipartFile file) {
        if (dbDialect instanceof FileDialect) {
            FileDialect fileDialect = (FileDialect) dbDialect;
            fileDialect.uploadFile(dbQueryProperty, path, file);
            return;
        }
        throw new ServiceException("当前数据源不支持上传文件");
    }

    @Override
    public String generateFlinkFields(DbQueryProperty dbQueryProperty, JSONObject config, String tableName, List<String> column, String querySql) {
        String sql = "SELECT " + String.join(",", column) + " FROM " + dbDialect.getTableName(dbQueryProperty, tableName) + " WHERE 1=0";
        if (!StringUtils.isEmpty(querySql)) {
            sql = "SELECT " + String.join(",", column) + " FROM (" + querySql + ") WHERE 1=0";
        }
        Boolean createPK = config.getBooleanValue("createPK", false);
        try {
            Connection conn = this.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSetMetaData meta = ps.getMetaData();
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                String colName = meta.getColumnName(i);
                String jdbcType = meta.getColumnTypeName(i);
                int scale = meta.getScale(i);
                int precision = meta.getPrecision(i);
                String flinkType = TYPE_MAPPING.getOrDefault(jdbcType.toUpperCase(), "STRING"); // 默认兜底
                if (org.apache.commons.lang3.StringUtils.equals("NUMBER", jdbcType.toUpperCase()) && precision == 0) {
                    flinkType = "INT";
                }
                sb.append("  `").append(colName).append("` ").append(flinkType);
                if (i < meta.getColumnCount()) sb.append(",\n");
            }
            if (createPK && org.apache.commons.lang3.StringUtils.isBlank(querySql)) {
                DatabaseMetaData databaseMetaData = conn.getMetaData();
                ResultSet rs = databaseMetaData.getPrimaryKeys(null, dbQueryProperty.getSid(), tableName);
                List<String> primaryKeys = new ArrayList<>();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    String colName = meta.getColumnName(i);
                    boolean isPrimaryKey = false;
                    while (rs.next()) {
                        String keyColName = rs.getString("COLUMN_NAME"); // 主键列名
                        if (keyColName.equalsIgnoreCase(colName)) {
                            isPrimaryKey = true;
                            break;
                        }
                    }
                    if (isPrimaryKey) {
                        primaryKeys.add(" PRIMARY KEY(`" + meta.getColumnName(i) + "`) NOT ENFORCED ");
                    }
                }
                if (primaryKeys.size() > 0) {
                    sb.append(",\n").append(String.join(",\n", primaryKeys));
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getFlinkSQL(JSONObject config, String taskExecuteType, String flinkTableName, String tableName, List<String> column, String querySql) {
        if ("STREAM".equals(taskExecuteType)) {
            return getFlinkCDCSQL(config, flinkTableName, tableName, column, querySql);
        }
        String tableFieldName = this.generateFlinkFields(dbQueryProperty, config, tableName, column, querySql);
        return dbDialect.getFlinkSQL(dbQueryProperty, flinkTableName, tableName, tableFieldName);
    }

    @Override
    public String getFlinkCDCSQL(JSONObject config, String flinkTableName, String tableName, List<String> column, String querySql) {
        String tableFieldName = this.generateFlinkFields(dbQueryProperty, config, tableName, column, querySql);
        return dbDialect.getFlinkCDCSQL(dbQueryProperty, flinkTableName, tableName, tableFieldName);
    }

    @Override
    public String getFlinkSinkSQL(JSONObject config, String taskExecuteType, String flinkTableName, String tableName, List<String> column) {
        String tableFieldName = this.generateFlinkFields(dbQueryProperty, config, tableName, column, null);
        return dbDialect.getFlinkSinkSQL(dbQueryProperty, config, flinkTableName, tableName, tableFieldName);
    }

    @Override
    public Boolean deleteTable(DbQueryProperty dbQueryProperty, String tableName) {
        try {
            String sql = "DROP TABLE " + dbDialect.getTableName(dbQueryProperty, tableName);
            this.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<String> generateUpdateTableSQL(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> sourceDbColumnList) {
        List<String> sqlList = new ArrayList<>();
        //生成获取表信息的sql
        String getTableInfoSql = dbDialect.table(dbQueryProperty, tableName);
        List<DbTable> dbTableList = jdbcTemplate.query(getTableInfoSql, dbDialect.tableMapper());
        if (dbTableList == null || dbTableList.size() == 0) {
            throw new ServiceException("表不存在");
        }
        //目标库表信息
        DbTable targetDbTable = dbTableList.get(0);
        //目标库表注释
        String targetTableComment = dbTableList.get(0).getTableComment();
        //查询表注释是否有改动
        if (!org.apache.commons.lang3.StringUtils.equals(tableComment, targetTableComment)) {
            sqlList.add(dbDialect.updateTableComment(dbQueryProperty, tableName, tableComment));
        }

        //生成获取目标字段列表的SQL
        String getTableColumnsSql = dbDialect.columns(dbQueryProperty, tableName);
        List<DbColumn> targetDbColumnList = new ArrayList<>();
        if (dbDialect instanceof OracleDialect) {
            List<DbColumn> longColumns = jdbcTemplate.query(getTableColumnsSql, dbDialect.columnLongMapper());
            List<DbColumn> queryColumns = jdbcTemplate.query(getTableColumnsSql, dbDialect.columnMapper());
            for (int i = 0; i < longColumns.size(); i++) {
                DbColumn longColumn = longColumns.get(i);
                DbColumn otherColumn = queryColumns.get(i);
                otherColumn.setDataDefault(longColumn.getDataDefault());
            }
            targetDbColumnList.addAll(queryColumns);
        } else {
            List<DbColumn> queryColumns = jdbcTemplate.query(getTableColumnsSql, dbDialect.columnMapper());
            targetDbColumnList.addAll(queryColumns);
        }

        // 比较源列和目标列的差异
        Map<String, Object> columnChanges = compareColumnLists(sourceDbColumnList, targetDbColumnList);
        List<DbColumn> columnsToDelete = (List<DbColumn>) columnChanges.get("columnsToDelete");
        List<DbColumn> columnsToModify = (List<DbColumn>) columnChanges.get("columnsToModify");
        List<DbColumn> columnsToAdd = (List<DbColumn>) columnChanges.get("columnsToAdd");
        Boolean updateColKey = (Boolean) columnChanges.get("updateColKey");

        // 处理需要新增的列
        for (DbColumn column : columnsToAdd) {
            sqlList.addAll(sqlList.size(), dbDialect.addColumn(dbQueryProperty, tableName, column));
        }

        // 处理需要修改的列
        for (DbColumn column : columnsToModify) {
            sqlList.addAll(sqlList.size(), dbDialect.modifyColumn(dbQueryProperty, tableName, column));
        }

        // 处理需要删除的列
        for (DbColumn column : columnsToDelete) {
            sqlList.add(dbDialect.dropColumn(dbQueryProperty, tableName, column.getColName()));
        }

        //处理主键更新
        if (updateColKey) {
            List<DbColumn> colKeyDbColumnList = sourceDbColumnList.stream()
                    .filter(DbColumn::getColKey)
                    .collect(Collectors.toList());
            //TODO 这个逻辑暂时不要——将需要删除的列中为主键的进行过滤
//            List<DbColumn> deleteColKeyColumnList = columnsToDelete.stream()
//                    .filter(DbColumn::getColKey)
//                    .collect(Collectors.toList());
//            if (deleteColKeyColumnList.size() > 0) {
//                colKeyDbColumnList.addAll(deleteColKeyColumnList);
//            }
            sqlList.addAll(sqlList.size(), dbDialect.updateColKey(dbQueryProperty, tableName, colKeyDbColumnList));
        }
        return sqlList;
    }


    /**
     * 比较源列列表和目标列列表，返回需要删除、修改和新增的列
     *
     * @param sourceDbColumnList 源列列表
     * @param targetDbColumnList 目标列列表
     * @return 包含三个键的Map：columnsToDelete（需删除）、columnsToModify（需修改）、columnsToAdd（需新增）
     */
    private Map<String, Object> compareColumnLists(List<DbColumn> sourceDbColumnList, List<DbColumn> targetDbColumnList) {
        //删除的字段
        List<DbColumn> columnsToDelete = new ArrayList<>();
        //修改的字段
        List<DbColumn> columnsToModify = new ArrayList<>();
        //添加的字段
        List<DbColumn> columnsToAdd = new ArrayList<>();
        //是否更改主键
        Boolean updateColKey = false;

        // 创建目标列的Map，以列名为key
        Map<String, DbColumn> targetColumnMap = new HashMap<>();
        if (targetDbColumnList != null) {
            for (DbColumn targetColumn : targetDbColumnList) {
                targetColumnMap.put(targetColumn.getColName(), targetColumn);
            }
        }

        // 创建源列的Map，以列名为key
        Map<String, DbColumn> sourceColumnMap = new HashMap<>();
        if (sourceDbColumnList != null) {
            for (DbColumn sourceColumn : sourceDbColumnList) {
                sourceColumnMap.put(sourceColumn.getColName(), sourceColumn);
            }
        }

        // 查找需要删除和修改的列
        if (targetDbColumnList != null) {
            for (DbColumn targetColumn : targetDbColumnList) {
                String colName = targetColumn.getColName();
                if (!sourceColumnMap.containsKey(colName)) {
                    // 源列中不存在该列，需要删除
                    columnsToDelete.add(targetColumn);
                    if (targetColumn.getColKey()) {
                        updateColKey = true;
                    }
                } else {
                    // 列存在，检查是否有变化需要修改
                    DbColumn sourceColumn = sourceColumnMap.get(colName);
                    if (isColumnModified(sourceColumn, targetColumn)) {
                        columnsToModify.add(sourceColumn);
                        if (!java.util.Objects.equals(sourceColumn.getColKey(), targetColumn.getColKey())) {
                            updateColKey = true;
                        }
                    }
                }
            }
        }

        // 查找需要新增的列
        if (sourceDbColumnList != null) {
            for (DbColumn sourceColumn : sourceDbColumnList) {
                String colName = sourceColumn.getColName();
                if (!targetColumnMap.containsKey(colName)) {
                    //判断是否是主键
                    if (sourceColumn.getColKey()) {
                        updateColKey = true;
                    }
                    // 目标列中不存在该列，需要新增
                    columnsToAdd.add(sourceColumn);
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("columnsToDelete", columnsToDelete);
        result.put("columnsToModify", columnsToModify);
        result.put("columnsToAdd", columnsToAdd);
        result.put("updateColKey", updateColKey);
        return result;
    }

    /**
     * 判断列是否发生了改变
     *
     * @param sourceColumn 源列
     * @param targetColumn 目标列
     * @return true表示有改变，false表示无改变
     */
    private boolean isColumnModified(DbColumn sourceColumn, DbColumn targetColumn) {
        //将源表字段类型进行转换
        String sourceDataType = dbDialect.getColumnType(targetColumn);

        if (sourceColumn == null || targetColumn == null) {
            return true;
        }

        // 比较数据类型
        if (!org.apache.commons.lang3.StringUtils.equalsIgnoreCase(sourceDataType, targetColumn.getDataType())) {
            return true;
        }

        // 比较数据长度
        if (!org.apache.commons.lang3.StringUtils.equals(sourceColumn.getDataLength(), targetColumn.getDataLength())) {
            return true;
        }

        // 比较数据精度
        if (targetColumn.getDataPrecision() != null && !org.apache.commons.lang3.StringUtils.equals(sourceColumn.getDataPrecision() == null ? sourceColumn.getDataLength() : sourceColumn.getDataPrecision(), targetColumn.getDataPrecision())) {
            return true;
        }

        // 比较小数位
        if (!org.apache.commons.lang3.StringUtils.equals(sourceColumn.getDataScale(), targetColumn.getDataScale())) {
            return true;
        }

        // 比较是否允许为空
        if (!java.util.Objects.equals(sourceColumn.getNullable(), targetColumn.getNullable())) {
            return true;
        }

        // 比较默认值
        if (!org.apache.commons.lang3.StringUtils.equals(sourceColumn.getDataDefault(), targetColumn.getDataDefault())) {
            return true;
        }

        // 比较注释
        if (!org.apache.commons.lang3.StringUtils.equals(sourceColumn.getColComment(), targetColumn.getColComment())) {
            return true;
        }

        if (!java.util.Objects.equals(sourceColumn.getColKey(), targetColumn.getColKey())) {
            return true;
        }

        return false;
    }
}
