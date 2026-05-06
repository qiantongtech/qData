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
 * MySQL数据库方言实现
 */
@Slf4j
public class MySqlDialect implements DatabaseDialect {
    // 连接池映射，key为数据库连接信息的唯一标识
    private static final Map<String, HikariDataSource> dataSourceMap = new HashMap<>();

    // 连接池配置参数
    private static final int MAX_POOL_SIZE = 10;
    private static final int MIN_IDLE = 2;
    private static final long CONNECTION_TIMEOUT = 30000; // 30秒
    private static final long IDLE_TIMEOUT = 600000; // 10分钟
    private static final long MAX_LIFETIME = 1800000; // 30分钟

    @Override
    public String getStorageEngine(McDbDO mcDbDO) {
        try {
            // 从连接池获取连接
            try (Connection conn = getConnection(mcDbDO)) {
                DatabaseMetaData metaData = conn.getMetaData();
                // 获取数据库产品名称和版本
                String productName = metaData.getDatabaseProductName();
                String productVersion = metaData.getDatabaseProductVersion();
                // MySQL默认使用InnoDB存储引擎
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
     * 解析datasourceConfig获取连接信息
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
     * 构建数据源
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

        // 生成数据源唯一标识
        String dataSourceKey = mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname") + ":" + username;

        // 如果数据源已存在，直接返回
        if (dataSourceMap.containsKey(dataSourceKey)) {
            HikariDataSource dataSource = dataSourceMap.get(dataSourceKey);
            if (!dataSource.isClosed()) {
                return dataSource;
            } else {
                // 数据源已关闭，移除并重新创建
                dataSourceMap.remove(dataSourceKey);
                dataSource.close();
            }
        }

        // 创建新的数据源
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
     * 从数据源获取连接
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
            // 从连接池获取连接
            try (Connection conn = getConnection(mcDbDO)) {
                String dbName = mcDbDO.getDbName();
                // 获取表行数
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SELECT COUNT(*) FROM " + tableName;
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        metadata.setRowCount(rs.getLong(1));
                    }
                }

                // 获取表索引信息
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

                // 获取表分区字段信息
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

                        // 解析并提取字段名
                        if (partExpr != null) {
                            String fieldName = extractFieldName(partExpr);
                            if (fieldName != null && !fieldName.isEmpty()) {
                                fieldSet.add(fieldName);
                            }
                        }
                        if (subPartExpr != null&& !subPartExpr.trim().isEmpty()) {
                            // 去掉反引号
                            String field = subPartExpr.trim().replaceAll("`", "");
                            fieldSet.add(field);
                        }
                    }
                    // 拼接字段，用逗号分隔
                    metadata.setPartitionFields(String.join(",", fieldSet));
                }

                // 获取表存储大小、创建时间和修改时间
                try (Statement stmt = conn.createStatement()) {
                        String sql = "SELECT DATA_LENGTH + INDEX_LENGTH AS table_size, ENGINE AS storage_engine, " +
                                " DATE_FORMAT(CREATE_TIME, '%Y-%m-%d %H:%i:%s') AS create_time, DATE_FORMAT(UPDATE_TIME, '%Y-%m-%d %H:%i:%s') AS update_time ,TABLE_COMMENT as table_comment" +
                                " FROM information_schema.TABLES WHERE TABLE_NAME = '" + tableName + "' AND TABLE_SCHEMA = '" + dbName + "'";
                    ResultSet rsSize = stmt.executeQuery(sql);
                    if (rsSize.next()) {
                        metadata.setTableSize(rsSize.getInt("table_size"));
                        metadata.setStorageEngine(rsSize.getString("storage_engine"));
                        metadata.setTableComment(rsSize.getString("table_comment"));
                        // 确保时间格式正确，不包含毫秒
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

                // 获取表主键字段
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
            // 从连接池获取连接
            try (Connection conn = getConnection(mcDbDO)) {
                // 使用JDBC元数据API获取字段自增信息
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet rs = metaData.getColumns(null, null, tableName, columnName);
                if (rs.next()) {
                    String isAutoIncrement = rs.getString("IS_AUTOINCREMENT");
                    metadata.setAutoIncrement("YES".equals(isAutoIncrement));
                }

                // 查询字段是否唯一
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
            // 从连接池获取连接
            try (Connection conn = getConnection(mcDbDO)) {
                String dbName = mcDbDO.getDbName();
                // 获取存储大小，转换为MB
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
     * 从分区表达式中提取原始字段名
     * 例如：to_days(tm) -> tm
     *       UNIX_TIMESTAMP(create_time) -> create_time
     *       user_id -> user_id
     */
    private static String extractFieldName(String expr) {
        if (expr == null || expr.trim().isEmpty()) {
            return "";
        }

        expr = expr.trim().replaceAll("`", "");

        // 去掉函数调用，保留括号内的内容
        // 匹配 pattern: func_name(column_name)
        int openParen = expr.indexOf('(');
        int closeParen = expr.lastIndexOf(')');

        if (openParen > 0 && closeParen > openParen) {
            String funcName = expr.substring(0, openParen).trim().toLowerCase();
            // 常见函数列表，可根据需要扩展
            Set<String> knownFuncs = new HashSet<>(Arrays.asList(
                    "to_days", "year", "month", "day", "hour", "minute",
                    "unix_timestamp", "from_days", "date", "str_to_date"
            ));

            if (knownFuncs.contains(funcName)) {
                String inner = expr.substring(openParen + 1, closeParen).trim();
                // 递归处理嵌套函数，如 to_days(date(create_time))
                return extractFieldName(inner);
            }
        }

        // 如果没有函数包装，直接返回表达式（可能是字段名或常量）
        // 过滤掉常量，如 '2024-01-01'，只保留标识符
        if (expr.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            return expr;
        }

        return "";
    }
}
