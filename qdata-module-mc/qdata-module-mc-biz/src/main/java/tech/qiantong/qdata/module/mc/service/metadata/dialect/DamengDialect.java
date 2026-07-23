package tech.qiantong.qdata.module.mc.service.metadata.dialect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qdata.common.database.utils.AesEncryptUtil;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

import java.sql.*;
import java.util.Map;

/**
 * Dameng 8 database dialect implementation
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
            // Dameng 8 uses DM8 storage engine, return DM8 here
            return "DM8";
        } catch (Exception e) {
            log.error("Failed to get the DM8 storage engine", e);
            return null;
        }
    }

    @Override
    public Long getTableRowCount(McDbDO mcDbDO, String tableName) {
        try {
            // Parse datasourceConfig to obtain connection information
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return 0L;
            }

            // Build connection string
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("Failed to decrypt password", e);
            }
            // Connect to the database and execute queries
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Statement stmt = conn.createStatement()) {
                String sql = "SELECT COUNT(*) FROM " + tableName;
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (Exception e) {
            log.error("Failed to get the DM8 table row count", e);
        }
        return 0L;
    }

    @Override
    public String getTableIndexes(McDbDO mcDbDO, String tableName) {
        try {
            // Parse datasourceConfig to obtain connection information
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return "";
            }

            // Build connection string
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("Failed to decrypt password", e);
            }
            // Connect to the database and use the JDBC metadata API to obtain index information
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
            log.error("Failed to get DM8 table index information", e);
        }
        return "";
    }

    @Override
    public String getTablePartitionFields(McDbDO mcDbDO, String tableName) {
        try {
            // Parse datasourceConfig to obtain connection information
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return "";
            }

            // Build connection string
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("Failed to decrypt password", e);
            }
            // Connect to the database and execute queries
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
            log.error("Failed to get DM8 table partition-column information", e);
        }
        return "";
    }

    @Override
    public boolean isColumnAutoIncrement(McDbDO mcDbDO, String tableName, String columnName) {
        try {
            // Parse datasourceConfig to obtain connection information
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return false;
            }

            // Build connection string
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("Failed to decrypt password", e);
            }
            // Connect to the database and use the JDBC metadata API to obtain field auto-increment information
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet rs = metaData.getColumns(null, null, tableName, columnName);
                if (rs.next()) {
                    String isAutoIncrement = rs.getString("IS_AUTOINCREMENT");
                    return "YES".equals(isAutoIncrement);
                }
            }
        } catch (Exception e) {
            log.error("Failed to get DM8 column auto-increment information", e);
        }
        return false;
    }

    @Override
    public boolean isPartitionField(McDbDO mcDbDO, String tableName, String columnName) {
        try {
            // Parse datasourceConfig to obtain connection information
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return false;
            }

            // Build connection string
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("Failed to decrypt password", e);
            }
            // Connect to the database and execute queries
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
            log.error("Failed to determine whether the DM8 column is a partition column", e);
        }
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
            log.error("Failed to parse datasourceConfig", e);
            return null;
        }
    }

    @Override
    public TableMetadata getTableMetadata(McDbDO mcDbDO, String tableName) {
        TableMetadata metadata = new TableMetadata();
        try {
            // Parse datasourceConfig to obtain connection information
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return metadata;
            }

            // Build connection string
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("Failed to decrypt password", e);
            }

            // Connect to the database and execute queries
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String dbName = mcDbDO.getDbName();
                // Get the number of table rows
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SELECT COUNT(*) FROM " + dbName+"."+tableName;
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        metadata.setRowCount(rs.getLong(1));
                    }
                }

                // Get table index information
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

                // Get table partition field information
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SELECT COLUMN_NAME  FROM DBA_PART_KEY_COLUMNS WHERE NAME = '" + tableName.toUpperCase() + "' AND OWNER = '"+dbName.toUpperCase()+"';  ";
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        String partitionColumns = rs.getString("COLUMN_NAME");
                        metadata.setPartitionFields(partitionColumns != null ? partitionColumns : "");
                    }
                }

                // Set up storage engine
                metadata.setStorageEngine("Dameng");
            }
        } catch (Exception e) {
            log.error("Failed to fetch DM8 table metadata in batch", e);
        }
        return metadata;
    }

    @Override
    public ColumnMetadata getColumnMetadata(McDbDO mcDbDO, String tableName, String columnName) {
        ColumnMetadata metadata = new ColumnMetadata();
        try {
            // Parse datasourceConfig to obtain connection information
            Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
            if (configMap == null) {
                return metadata;
            }

            // Build connection string
            String url = "jdbc:dm://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            try {
                password = AesEncryptUtil.desEncrypt(password).trim();
            } catch (Exception e) {
                log.error("Failed to decrypt password", e);
                return null;
            }

            // Connect to the database and execute queries
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String dbName = mcDbDO.getDbName();
                // Get field auto-increment information
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SELECT IDENTITY_COLUMN FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '" + tableName.toUpperCase() + "' AND COLUMN_NAME = '" + columnName.toUpperCase() + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        String identityColumn = rs.getString("IDENTITY_COLUMN");
                        metadata.setAutoIncrement("YES".equals(identityColumn));
                    }
                }

                // Get whether the field is a partition field
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SELECT COLUMN_NAME FROM DBA_PART_KEY_COLUMNS WHERE NAME = '" + tableName.toUpperCase() + "' AND OWNER = '" + dbName.toUpperCase() + "' AND COLUMN_NAME = '" + columnName.toUpperCase() + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    metadata.setPartitionField(rs.next());
                }
            }
        } catch (Exception e) {
            log.error("Failed to fetch DM8 column metadata in batch", e);
        }
        return metadata;
    }
}
