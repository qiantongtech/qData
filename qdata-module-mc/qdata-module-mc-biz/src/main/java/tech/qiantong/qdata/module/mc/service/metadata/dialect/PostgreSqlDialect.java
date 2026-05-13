package tech.qiantong.qdata.module.mc.service.metadata.dialect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

/**
 * PostgreSQL数据库方言实现
 */
@Slf4j
public class PostgreSqlDialect implements DatabaseDialect {
    @Override
    public ColumnMetadata getColumnMetadata(McDbDO mcDbDO, String tableName, String columnName) {
        return null;
    }

    @Override
    public TableMetadata getTableMetadata(McDbDO mcDbDO, String tableName) {
        return null;
    }

    @Override
    public String getStorageEngine(McDbDO mcDbDO) {
        try {
            // PostgreSQL使用表空间，这里返回PostgreSQL
            return "PostgreSQL";
        } catch (Exception e) {
            log.error("获取PostgreSQL存储引擎失败", e);
            return null;
        }
    }

    @Override
    public Long getTableRowCount(McDbDO mcDbDO, String tableName) {
        try {
            // 解析datasourceConfig获取连接信息
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return 0L;
            }

            // 构建连接字符串
            String url = "jdbc:postgresql://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");

            // 连接数据库并执行查询
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Statement stmt = conn.createStatement()) {
                String sql = "SELECT COUNT(*) FROM " + tableName;
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (Exception e) {
            log.error("获取PostgreSQL表行数失败", e);
        }
        return 0L;
    }

    @Override
    public String getTableIndexes(McDbDO mcDbDO, String tableName) {
        try {
            // 解析datasourceConfig获取连接信息
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return "";
            }

            // 构建连接字符串
            String url = "jdbc:postgresql://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");

            // 连接数据库并执行查询
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Statement stmt = conn.createStatement()) {
                String sql = "SELECT indexname FROM pg_indexes WHERE tablename = '" + tableName + "' AND indexname != 'pk_'";
                ResultSet rs = stmt.executeQuery(sql);
                StringBuilder indexes = new StringBuilder();
                while (rs.next()) {
                    String indexName = rs.getString("indexname");
                    if (indexes.length() > 0) {
                        indexes.append(", ");
                    }
                    indexes.append(indexName);
                }
                return indexes.toString();
            }
        } catch (Exception e) {
            log.error("获取PostgreSQL表索引信息失败", e);
        }
        return "";
    }

    @Override
    public String getTablePartitionFields(McDbDO mcDbDO, String tableName) {
        try {
            // 解析datasourceConfig获取连接信息
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return "";
            }

            // 构建连接字符串
            String url = "jdbc:postgresql://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");

            // 连接数据库并执行查询
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Statement stmt = conn.createStatement()) {
                String sql = "SELECT column_name FROM information_schema.partitions WHERE table_name = '" + tableName + "'";
                ResultSet rs = stmt.executeQuery(sql);
                StringBuilder partitionFields = new StringBuilder();
                while (rs.next()) {
                    String columnName = rs.getString("column_name");
                    if (partitionFields.length() > 0) {
                        partitionFields.append(", ");
                    }
                    partitionFields.append(columnName);
                }
                return partitionFields.toString();
            }
        } catch (Exception e) {
            log.error("获取PostgreSQL表分区字段信息失败", e);
        }
        return "";
    }

    @Override
    public boolean isColumnAutoIncrement(McDbDO mcDbDO, String tableName, String columnName) {
        try {
            // 解析datasourceConfig获取连接信息
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return false;
            }

            // 构建连接字符串
            String url = "jdbc:postgresql://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");

            // 连接数据库并执行查询
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Statement stmt = conn.createStatement()) {
                String sql = "SELECT column_default FROM information_schema.columns WHERE table_name = '" + tableName + "' AND column_name = '" + columnName + "'";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    String columnDefault = rs.getString("column_default");
                    return columnDefault != null && columnDefault.contains("nextval");
                }
            }
        } catch (Exception e) {
            log.error("获取PostgreSQL字段自增信息失败", e);
        }
        return false;
    }

    @Override
    public boolean isPartitionField(McDbDO mcDbDO, String tableName, String columnName) {
        try {
            // 解析datasourceConfig获取连接信息
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return false;
            }

            // 构建连接字符串
            String url = "jdbc:postgresql://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");

            // 连接数据库并执行查询
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Statement stmt = conn.createStatement()) {
                String sql = "SELECT column_name FROM information_schema.partitions WHERE table_name = '" + tableName + "' AND column_name = '" + columnName + "'";
                ResultSet rs = stmt.executeQuery(sql);
                return rs.next();
            }
        } catch (Exception e) {
            log.error("判断PostgreSQL字段是否为分区字段失败", e);
        }
        return false;
    }

    @Override
    public DbMetadata getDbMetadata(McDbDO mcDbDO) {
        return null;
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
}
