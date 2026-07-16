package tech.qiantong.qdata.module.mc.service.metadata.dialect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qdata.common.database.utils.AesEncryptUtil;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * MySQL database dialect implementation
 */
@Slf4j
public class MySqlDialect implements DatabaseDialect {
    // Connection pool mapping, key is the unique identifier of database connection information
    private static final Map<String, HikariDataSource> dataSourceMap = new HashMap<>();

    // Connection pool configuration parameters
    private static final int MAX_POOL_SIZE = 10;
    private static final int MIN_IDLE = 2;
    private static final long CONNECTION_TIMEOUT = 30000; // 30 seconds
    private static final long IDLE_TIMEOUT = 600000; // 10 minutes
    private static final long MAX_LIFETIME = 1800000; // 30 minutes

    @Override
    public String getStorageEngine(McDbDO mcDbDO) {
        try {
            // Get connection from connection pool
            try (Connection conn = getConnection(mcDbDO)) {
                DatabaseMetaData metaData = conn.getMetaData();
                // Get database product name and version
                String productName = metaData.getDatabaseProductName();
                String productVersion = metaData.getDatabaseProductVersion();
                // MySQL uses the InnoDB storage engine by default
                return "InnoDB";
            }
        } catch (Exception e) {
            log.error("获取MySQL存储引擎失败", e);
            return null;
        }
    }

    @Override
    public Long getTableRowCount(McDbDO mcDbDO, String tableName) {
        return 0L;
    }

    @Override
    public String getTableIndexes(McDbDO mcDbDO, String tableName) {
        return "";
    }

    @Override
    public String getTablePartitionFields(McDbDO mcDbDO, String tableName) {
        return "";
    }

    @Override
    public boolean isColumnAutoIncrement(McDbDO mcDbDO, String tableName, String columnName) {
        return false;
    }

    @Override
    public boolean isPartitionField(McDbDO mcDbDO, String tableName, String columnName) {
        return false;
    }

    /**
     * Parse datasourceConfig to obtain connection information
     */
    private Map<String, Object> parseDatasourceConfig(String datasourceConfig) {
        if (StringUtils.isBlank(datasourceConfig)) {
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(datasourceConfig, Map.class);
        } catch (Exception e) {
            log.error("解析datasourceConfig失败", e);
            return null;
        }
    }

    /**
     * Build data source
     */
    private HikariDataSource buildDataSource(McDbDO mcDbDO) {
        Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
        if (configMap == null) {
            return null;
        }

        String url = "jdbc:mysql://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname")+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String username = (String) configMap.get("username");
        String password = (String) configMap.get("password");
        try {
            password = AesEncryptUtil.desEncrypt(password).trim();
        } catch (Exception e) {
            log.error("解密密码失败", e);
            return null;
        }

        // Generate data source unique identifier
        String dataSourceKey = mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname") + ":" + username;

        // If the data source already exists, return directly
        if (dataSourceMap.containsKey(dataSourceKey)) {
            HikariDataSource dataSource = dataSourceMap.get(dataSourceKey);
            if (!dataSource.isClosed()) {
                return dataSource;
            } else {
                // Data source has been closed, removed and recreated
                dataSourceMap.remove(dataSourceKey);
                dataSource.close();
            }
        }

        // Create new data source
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(MAX_POOL_SIZE);
        config.setMinimumIdle(MIN_IDLE);
        config.setConnectionTimeout(CONNECTION_TIMEOUT);
        config.setIdleTimeout(IDLE_TIMEOUT);
        config.setMaxLifetime(MAX_LIFETIME);
        config.setConnectionTestQuery("SELECT 1");

        HikariDataSource dataSource = new HikariDataSource(config);
        dataSourceMap.put(dataSourceKey, dataSource);

        return dataSource;
    }

    /**
     * Get connection from data source
     */
    private Connection getConnection(McDbDO mcDbDO) throws Exception {
        HikariDataSource dataSource = buildDataSource(mcDbDO);
        if (dataSource == null) {
            throw new Exception("获取数据源失败");
        }
        return dataSource.getConnection();
    }

    @Override
    public TableMetadata getTableMetadata(McDbDO mcDbDO, String tableName) {
        TableMetadata metadata = new TableMetadata();
        try {
            // Get connection from connection pool
            try (Connection conn = getConnection(mcDbDO)) {
                String dbName = mcDbDO.getDbName();
                // Get the number of table rows
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SELECT COUNT(*) FROM " + tableName;
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        metadata.setRowCount(rs.getLong(1));
                    }
                }

                // Get table index information
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SHOW INDEX FROM " + tableName;
                    ResultSet rs = stmt.executeQuery(sql);
                    StringBuilder indexes = new StringBuilder();
                    while (rs.next()) {
                        String indexName = rs.getString("Key_name");
                        if (!"PRIMARY".equals(indexName) && !indexes.toString().contains(indexName)) {
                            if (indexes.length() > 0) {
                                indexes.append(", ");
                            }
                            indexes.append(indexName);
                        }
                    }
                    metadata.setIndexes(indexes.toString());
                }

                // Get table partition field information
                try (Statement stmt = conn.createStatement()) {
                    Set<String> fieldSet = new LinkedHashSet<>();
                    String sql = "SELECT DISTINCT " +
                            " PARTITION_EXPRESSION, " +
                            " SUBPARTITION_EXPRESSION " +
                            " FROM information_schema.PARTITIONS " +
                            " WHERE TABLE_SCHEMA = '"+dbName+"' AND TABLE_NAME = '"+tableName+"' ";
                    ResultSet rs = stmt.executeQuery(sql);
                    while (rs.next()) {
                        String partExpr = rs.getString("PARTITION_EXPRESSION");
                        String subPartExpr = rs.getString("SUBPARTITION_EXPRESSION");

                        // Parse and extract field names
                        if (partExpr != null) {
                            String fieldName = extractFieldName(partExpr);
                            if (fieldName != null && !fieldName.isEmpty()) {
                                fieldSet.add(fieldName);
                            }
                        }
                        if (subPartExpr != null&& !subPartExpr.trim().isEmpty()) {
                            // Remove backticks
                            String field = subPartExpr.trim().replaceAll("`", "");
                            fieldSet.add(field);
                        }
                    }
                    // Splice fields, separated by commas
                    metadata.setPartitionFields(String.join(",", fieldSet));
                }

                // Get table storage size, creation time and modification time
                try (Statement stmt = conn.createStatement()) {
                        String sql = "SELECT DATA_LENGTH + INDEX_LENGTH AS table_size, ENGINE AS storage_engine, " +
                                " DATE_FORMAT(CREATE_TIME, '%Y-%m-%d %H:%i:%s') AS create_time, DATE_FORMAT(UPDATE_TIME, '%Y-%m-%d %H:%i:%s') AS update_time ,TABLE_COMMENT as table_comment" +
                                " FROM information_schema.TABLES WHERE TABLE_NAME = '" + tableName + "' AND TABLE_SCHEMA = '" + dbName + "'";
                    ResultSet rsSize = stmt.executeQuery(sql);
                    if (rsSize.next()) {
                        metadata.setTableSize(rsSize.getInt("table_size"));
                        metadata.setStorageEngine(rsSize.getString("storage_engine"));
                        metadata.setTableComment(rsSize.getString("table_comment"));
                        // Make sure the time is in the correct format and does not include milliseconds
                        String createTime = rsSize.getString("create_time");
                        if (createTime != null && createTime.contains(".")) {
                            createTime = createTime.substring(0, createTime.indexOf("."));
                        }
                        metadata.setCreateTime(createTime);
                        String updateTime = rsSize.getString("update_time");
                        if (updateTime != null && updateTime.contains(".")) {
                            updateTime = updateTime.substring(0, updateTime.indexOf("."));
                        }
                        metadata.setUpdateTime(updateTime);
                    }
                }

                // Get table primary key field
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SHOW INDEX FROM " + tableName + " WHERE Key_name = 'PRIMARY'";
                    ResultSet rs = stmt.executeQuery(sql);
                    StringBuilder primaryKeys = new StringBuilder();
                    while (rs.next()) {
                        String columnName = rs.getString("Column_name");
                        if (primaryKeys.length() > 0) {
                            primaryKeys.append(", ");
                        }
                        primaryKeys.append(columnName);
                    }
                    metadata.setPrimaryKey(primaryKeys.toString());
                }
            }
        } catch (Exception e) {
            log.error("批量获取MySQL表元数据失败", e);
        }
        return metadata;
    }

    @Override
    public ColumnMetadata getColumnMetadata(McDbDO mcDbDO, String tableName, String columnName) {
        ColumnMetadata metadata = new ColumnMetadata();
        try {
            // Get connection from connection pool
            try (Connection conn = getConnection(mcDbDO)) {
                // Use JDBC metadata API to obtain field auto-increment information
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet rs = metaData.getColumns(null, null, tableName, columnName);
                if (rs.next()) {
                    String isAutoIncrement = rs.getString("IS_AUTOINCREMENT");
                    metadata.setAutoIncrement("YES".equals(isAutoIncrement));
                }

                // Check whether the field is unique
                boolean isUnique = false;
                try (ResultSet uniqueRs = metaData.getIndexInfo(null, null, tableName, true, false)) {
                    while (uniqueRs.next()) {
                        String colName = uniqueRs.getString("COLUMN_NAME");
                        if (columnName.equals(colName)) {
                            isUnique = true;
                            break;
                        }
                    }
                }
                metadata.setUnique(isUnique);
            }
        } catch (Exception e) {
            log.error("批量获取MySQL字段元数据失败", e);
        }
        return metadata;
    }

    @Override
    public DbMetadata getDbMetadata(McDbDO mcDbDO) {
        DbMetadata dbMetadata = new DbMetadata();
        try {
            // Get connection from connection pool
            try (Connection conn = getConnection(mcDbDO)) {
                String dbName = mcDbDO.getDbName();
                // Get storage size, converted to MB
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SELECT ROUND(SUM(DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024, 2) AS total_size_mb " +
                            "FROM information_schema.TABLES " +
                            "WHERE table_schema = '"+dbName+"' ";
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        dbMetadata.setStorageSize(rs.getInt(1));
                    }
                }

            }
        } catch (Exception e) {
            log.error("批量获取MySQL数据库元数据失败", e);
        }
        return dbMetadata;
    }

    /**
     * Extract original field names from partition expression
     * For example: to_days(tm) -> tm
     *       UNIX_TIMESTAMP(create_time) -> create_time
     *       user_id -> user_id
     */
    private static String extractFieldName(String expr) {
        if (expr == null || expr.trim().isEmpty()) {
            return "";
        }

        expr = expr.trim().replaceAll("`", "");

        // Remove the function call and keep the content in the brackets
        // Match pattern: func_name(column_name)
        int openParen = expr.indexOf('(');
        int closeParen = expr.lastIndexOf(')');

        if (openParen > 0 && closeParen > openParen) {
            String funcName = expr.substring(0, openParen).trim().toLowerCase();
            // List of common functions, which can be expanded as needed
            Set<String> knownFuncs = new HashSet<>(Arrays.asList(
                    "to_days", "year", "month", "day", "hour", "minute",
                    "unix_timestamp", "from_days", "date", "str_to_date"
            ));

            if (knownFuncs.contains(funcName)) {
                String inner = expr.substring(openParen + 1, closeParen).trim();
                // Recursively handle nested functions, such as to_days(date(create_time))
                return extractFieldName(inner);
            }
        }

        // If there is no function wrapper, the expression is returned directly (maybe a field name or a constant)
        // Filter out constants, such as '2024-01-01', and keep only identifiers
        if (expr.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            return expr;
        }

        return "";
    }
}
