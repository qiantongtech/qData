package tech.qiantong.qdata.module.mc.service.metadata.dialect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qdata.common.database.utils.AesEncryptUtil;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

import java.sql.*;
import java.util.Map;

/**
 * 达梦8数据库方言实现
 */
@Slf4j
public class DamengDialect implements DatabaseDialect {
    @Override
    public DbMetadata getDbMetadata(McDbDO mcDbDO) {
        return null;
    }

    @Override
    public String getStorageEngine(McDbDO mcDbDO) {
        try {
            // 达梦8使用DM8存储引擎，这里返回DM8
            return "DM8";
        } catch (Exception e) {
            log.error("获取达梦8存储引擎失败", e);
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
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("解密密码失败", e);
            }
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
            log.error("获取达梦8表行数失败", e);
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
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("解密密码失败", e);
            }
            // 连接数据库并使用JDBC元数据API获取索引信息
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet rs = metaData.getIndexInfo(null, null, tableName, false, false);
                StringBuilder indexes = new StringBuilder();
                while (rs.next()) {
                    String indexName = rs.getString("INDEX_NAME");
                    if (indexName != null && !"PRIMARY".equals(indexName) && !indexes.toString().contains(indexName)) {
                        if (indexes.length() > 0) {
                            indexes.append(", ");
                        }
                        indexes.append(indexName);
                    }
                }
                return indexes.toString();
            }
        } catch (Exception e) {
            log.error("获取达梦8表索引信息失败", e);
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
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("解密密码失败", e);
            }
            // 连接数据库并执行查询
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Statement stmt = conn.createStatement()) {
                String sql = "SELECT PARTITIONING_COLUMNS FROM USER_TABLES WHERE TABLE_NAME = '" + tableName.toUpperCase() + "'";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    String partitionColumns = rs.getString("PARTITIONING_COLUMNS");
                    return partitionColumns != null ? partitionColumns : "";
                }
            }
        } catch (Exception e) {
            log.error("获取达梦8表分区字段信息失败", e);
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
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("解密密码失败", e);
            }
            // 连接数据库并使用JDBC元数据API获取字段自增信息
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet rs = metaData.getColumns(null, null, tableName, columnName);
                if (rs.next()) {
                    String isAutoIncrement = rs.getString("IS_AUTOINCREMENT");
                    return "YES".equals(isAutoIncrement);
                }
            }
        } catch (Exception e) {
            log.error("获取达梦8字段自增信息失败", e);
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
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("解密密码失败", e);
            }
            // 连接数据库并执行查询
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Statement stmt = conn.createStatement()) {
                String sql = "SELECT PARTITIONING_COLUMNS FROM USER_TABLES WHERE TABLE_NAME = '" + tableName.toUpperCase() + "'";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    String partitionColumns = rs.getString("PARTITIONING_COLUMNS");
                    return partitionColumns != null && partitionColumns.contains(columnName.toUpperCase());
                }
            }
        } catch (Exception e) {
            log.error("判断达梦8字段是否为分区字段失败", e);
        }
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

    @Override
    public TableMetadata getTableMetadata(McDbDO mcDbDO, String tableName) {
        TableMetadata metadata = new TableMetadata();
        try {
            // 解析datasourceConfig获取连接信息
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return metadata;
            }

            // 构建连接字符串
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("解密密码失败", e);
            }

            // 连接数据库并执行查询
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String dbName = mcDbDO.getDbName();
                // 获取表行数
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SELECT COUNT(*) FROM " + dbName+"."+tableName;
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        metadata.setRowCount(rs.getLong(1));
                    }
                }

                // 获取表索引信息
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SELECT INDEX_NAME FROM DBA_IND_COLUMNS WHERE TABLE_NAME = '" + tableName.toUpperCase() + "' and INDEX_OWNER='"+dbName.toUpperCase()+"'";
                    ResultSet rs = stmt.executeQuery(sql);
                    StringBuilder indexes = new StringBuilder();
                    while (rs.next()) {
                        String indexName = rs.getString("INDEX_NAME");
                        if (indexes.length() > 0) {
                            indexes.append(", ");
                        }
                        indexes.append(indexName);
                    }
                    metadata.setIndexes(indexes.toString());
                }

                // 获取表分区字段信息
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SELECT COLUMN_NAME  FROM DBA_PART_KEY_COLUMNS WHERE NAME = '" + tableName.toUpperCase() + "' AND OWNER = '"+dbName.toUpperCase()+"';  ";
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        String partitionColumns = rs.getString("COLUMN_NAME");
                        metadata.setPartitionFields(partitionColumns != null ? partitionColumns : "");
                    }
                }

                // 设置存储引擎
                metadata.setStorageEngine("Dameng");
            }
        } catch (Exception e) {
            log.error("批量获取达梦8表元数据失败", e);
        }
        return metadata;
    }

    @Override
    public ColumnMetadata getColumnMetadata(McDbDO mcDbDO, String tableName, String columnName) {
        ColumnMetadata metadata = new ColumnMetadata();
        try {
            // 解析datasourceConfig获取连接信息
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return metadata;
            }

            // 构建连接字符串
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("解密密码失败", e);
                return null;
            }

            // 连接数据库并执行查询
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String dbName = mcDbDO.getDbName();
                // 获取字段自增信息
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SELECT IDENTITY_COLUMN FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '" + tableName.toUpperCase() + "' AND COLUMN_NAME = '" + columnName.toUpperCase() + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        String identityColumn = rs.getString("IDENTITY_COLUMN");
                        metadata.setAutoIncrement("YES".equals(identityColumn));
                    }
                }

                // 获取字段是否为分区字段
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SELECT COLUMN_NAME FROM DBA_PART_KEY_COLUMNS WHERE NAME = '" + tableName.toUpperCase() + "' AND OWNER = '" + dbName.toUpperCase() + "' AND COLUMN_NAME = '" + columnName.toUpperCase() + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    metadata.setPartitionField(rs.next());
                }
            }
        } catch (Exception e) {
            log.error("批量获取达梦8字段元数据失败", e);
        }
        return metadata;
    }
}
