package tech.qiantong.qdata.module.mc.service.metadata.dialect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qdata.common.database.utils.AesEncryptUtil;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Hive database dialect implementation
 */
@Slf4j
public class HiveDialect implements DatabaseDialect {

    @Override
    public String getStorageEngine(McDbDO mcDbDO) {
        try {
            // Hive uses HDFS storage, return Hive here
            return "Hive";
        } catch (Exception e) {
            log.error("Failed to get the Hive storage engine", e);
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
        // Hive does not support auto-increment fields and returns false directly.
        log.info("Hive does not support auto-increment columns");
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
            log.error("Failed to parse datasourceConfig", e);
            return null;
        }
    }

    /**
     * Build connection information
     */
    private ConnectionInfo buildConnectionInfo(McDbDO mcDbDO) {
        Map<String, Object> configMap = parseDatasourceConfig(mcDbDO.getDatasourceConfig());
        if (configMap == null) {
            return null;
        }

        String url = "jdbc:hive2://" + mcDbDO.getIp() + ":" + mcDbDO.getPort() + "/" + configMap.get("dbname");
        String username = (String) configMap.get("username");
        String password = (String) configMap.get("password");
        try {
            password = AesEncryptUtil.desEncrypt(password).trim();
        } catch (Exception e) {
            log.error("Failed to decrypt password", e);
            return null;
        }

        return new ConnectionInfo(url, username, password);
    }

    /**
     * Connection information class
     */
    private static class ConnectionInfo {
        private final String url;
        private final String username;
        private final String password;

        public ConnectionInfo(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }

        public String getUrl() {
            return url;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    @Override
    public DbMetadata getDbMetadata(McDbDO mcDbDO) {
        DbMetadata dbMetadata = new DbMetadata();
        try {
            // Build connection information
            ConnectionInfo connectionInfo = buildConnectionInfo(mcDbDO);
            if (connectionInfo == null) {
                return dbMetadata;
            }

            // Connect to the database and execute queries
            try (Connection conn = DriverManager.getConnection(connectionInfo.getUrl(), connectionInfo.getUsername(), connectionInfo.getPassword())) {
                List<String> tables = getTableNames(conn, mcDbDO.getDbName());
                long totalSizeBytes = 0;
                for (String table : tables) {
                    totalSizeBytes += getTableSizeBytes(conn, table);
                }

                // Set storage size
                dbMetadata.setStorageSize((int) (totalSizeBytes / 1024.0 / 1024.0));
            }
        } catch (Exception e) {
            log.error("Failed to fetch Hive database metadata in batch", e);
        }
        return dbMetadata;
    }

    // Get all table names in the specified database
    private static List<String> getTableNames(Connection conn, String dbName) throws SQLException {
        List<String> tables = new ArrayList<>();
        // Switch to the specified database
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("USE " + dbName);
            // Get all tables
            ResultSet rs = conn.getMetaData().getTables(null, dbName, null, new String[]{"TABLE"});
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }

    // Query the size of a single table (unit: bytes)
    private static long getTableSizeBytes(Connection conn, String tableName) throws SQLException {
        String sql = "DESCRIBE FORMATTED " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // DESCRIBE FORMATTED returns three columns: col_name, data_type, comment
                String colName = rs.getString("col_name");
                if ("Total Size".equals(colName)) {
                    String sizeStr = rs.getString("data_type").replaceAll(",", ""); // Remove commas from numbers
                    return Long.parseLong(sizeStr);
                }
                String data_type = rs.getString("data_type");
            }
        }
        return 0;
    }
    // Query table-level comments
    private static String getTableComment(Connection conn, String tableName) throws SQLException {
        String sql = "DESCRIBE FORMATTED " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // DESCRIBE FORMATTED returns three columns: col_name, data_type, comment
                String data_type = rs.getString("data_type");
                if (StringUtils.isNotBlank(data_type)&&data_type.contains("comment")) {
                    String comment = rs.getString("comment");
                    return comment;
                }
            }
        }
        return "";
    }

    // Query table creation time
    private static String getTableCreateTime(Connection conn, String tableName) throws SQLException {
        String sql = "DESCRIBE FORMATTED " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // DESCRIBE FORMATTED returns three columns: col_name, data_type, comment
                String colName = rs.getString("col_name");
                if ("Created".equals(colName)) {
                    String originalTime = rs.getString("data_type");
                    // Convert the time format to yyyy-MM-dd HH:mm:ss
                    try {
                        // The time format returned by Hive is usually: Thu Apr 14 10:00:00 CST 2026
                        java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.ENGLISH);
                        java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        java.util.Date date = inputFormat.parse(originalTime);
                        return outputFormat.format(date);
                    } catch (Exception e) {
                        // Parsing failed; return the original time
                        log.error("Time conversion failed: {}", e.getMessage());
                        return originalTime;
                    }
                }
            }
        }
        return "";
    }
    @Override
    public TableMetadata getTableMetadata(McDbDO mcDbDO, String tableName) {
        TableMetadata metadata = new TableMetadata();
        try {
            // Build connection information
            ConnectionInfo connectionInfo = buildConnectionInfo(mcDbDO);
            if (connectionInfo == null) {
                return metadata;
            }

            // Connect to the database and execute queries
            try (Connection conn = DriverManager.getConnection(connectionInfo.getUrl(), connectionInfo.getUsername(), connectionInfo.getPassword())) {
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
//                try (Statement stmt = conn.createStatement()) {
//                    String sql = "SHOW INDEXES IN " + tableName;
//                    ResultSet rs = stmt.executeQuery(sql);
//                    StringBuilder indexes = new StringBuilder();
//                    while (rs.next()) {
//                        String indexName = rs.getString(1);
//                        if (indexes.length() > 0) {
//                            indexes.append(", ");
//                        }
//                        indexes.append(indexName);
//                    }
//                    metadata.setIndexes(indexes.toString());
//                }

                // Get table partition field information
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SHOW PARTITIONS " + tableName;
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        String partition = rs.getString(1);
                        if (partition != null && partition.contains("=")) {
                            String[] parts = partition.split("/");
                            StringBuilder partitionFields = new StringBuilder();
                            for (String part : parts) {
                                if (part.contains("=")) {
                                    String field = part.split("=")[0];
                                    if (partitionFields.length() > 0) {
                                        partitionFields.append(", ");
                                    }
                                    partitionFields.append(field);
                                }
                            }
                            metadata.setPartitionFields(partitionFields.toString());
                        }
                    }
                }catch (SQLException e) {
                    log.warn("Not a Hive partitioned table: {}", e.getMessage());
                    metadata.setPartitionFields("");
                }

                // Get table storage size
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SHOW TABLE EXTENDED LIKE '" + tableName + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    while (rs.next()) {
                        String line = rs.getString(1);
                        if (line != null && line.contains("totalFileSize:")) {
                            // Parse the storage size and directly obtain the number of bytes
                            int startIndex = line.indexOf("totalFileSize:") + "totalFileSize:".length();
                            String sizeStr = line.substring(startIndex).trim();
                            try {
                                Integer size = Integer.valueOf(sizeStr);
                                // Convert to MB to two decimal places
                                metadata.setTableSize(size);
                            } catch (NumberFormatException e) {
                                log.warn("Failed to parse Hive table storage size: {}", sizeStr);
                            }
                            break;
                        }
                    }
                }
                metadata.setTableComment(getTableComment(conn,tableName));

                // Hive does not support primary keys and is set to empty
                metadata.setPrimaryKey("");

                // Use DESCRIBE FORMATTED query to get creation time and InputFormat
                try (Statement stmt = conn.createStatement()) {
                    String sql = "DESCRIBE FORMATTED " + tableName;
                    ResultSet rs = stmt.executeQuery(sql);
                    String inputFormat = "";
                    String createTime = "";
                    while (rs.next()) {
                        String colName = rs.getString("col_name");
                        if (colName.contains("InputFormat")) {
                            inputFormat = rs.getString("data_type");
                        } else if (colName.contains("CreateTime")) {
                            createTime = rs.getString("data_type");
                            // Convert the time format to yyyy-MM-dd HH:mm:ss
                            try {
                                // The time format returned by Hive is usually: Thu Apr 14 10:00:00 CST 2026
                                java.text.SimpleDateFormat dataFormat = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.ENGLISH);
                                java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                java.util.Date date = dataFormat.parse(createTime);
                                createTime = outputFormat.format(date);
                            } catch (Exception e) {
                                // Parsing failed; return the original time
                                log.error("Time conversion failed: {}", e.getMessage());
                            }
                        }
                    }
                    // Set the creation time (Hive does not support modification time, set it to the creation time)
                    metadata.setCreateTime(createTime);
                    metadata.setUpdateTime(null);

                    // Mapping storage engine types based on InputFormat
                    if (inputFormat.contains("OrcInputFormat")) {
                        metadata.setStorageEngine("ORC");
                    } else if (inputFormat.contains("ParquetInputFormat")) {
                        metadata.setStorageEngine("PARQUET");
                    } else if (inputFormat.contains("TextInputFormat")) {
                        metadata.setStorageEngine("TEXTFILE");
                    } else if (inputFormat.contains("AvroInputFormat")) {
                        metadata.setStorageEngine("AVRO");
                    } else if (inputFormat.contains("RCFileInputFormat")) {
                        metadata.setStorageEngine("RCFILE");
                    } else if (inputFormat.contains("SequenceFileInputFormat")) {
                        metadata.setStorageEngine("SEQUENCEFILE");
                    } else if (!inputFormat.isEmpty()) {
                        // Handle other storage formats
                        metadata.setStorageEngine(inputFormat);
                    } else {
                        metadata.setStorageEngine("Hive");
                    }
                } catch (SQLException e) {
                    log.warn("Failed to get Hive table creation time and storage engine: {}", e.getMessage());
                    metadata.setStorageEngine("Hive");
                }
            }
        } catch (Exception e) {
            log.error("Failed to fetch Hive table metadata in batch", e);
        }
        return metadata;
    }

    @Override
    public ColumnMetadata getColumnMetadata(McDbDO mcDbDO, String tableName, String columnName) {
        ColumnMetadata metadata = new ColumnMetadata();
        try {
            // Hive does not support auto-increment fields
            metadata.setAutoIncrement(false);
            // Hive does not support unique constraints
            metadata.setUnique(false);

            // Build connection information
            ConnectionInfo connectionInfo = buildConnectionInfo(mcDbDO);
            if (connectionInfo == null) {
                return metadata;
            }

            // Connect to the database and execute queries
            try (Connection conn = DriverManager.getConnection(connectionInfo.getUrl(), connectionInfo.getUsername(), connectionInfo.getPassword());
                 Statement stmt = conn.createStatement()) {
                String sql = "SHOW PARTITIONS " + tableName;
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    String partition = rs.getString(1);
                    if (partition != null && partition.contains("=")) {
                        String[] parts = partition.split("/");
                        for (String part : parts) {
                            if (part.contains("=")) {
                                String field = part.split("=")[0];
                                if (field.equals(columnName)) {
                                    metadata.setPartitionField(true);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to fetch Hive column metadata in batch", e);
        }
        return metadata;
    }

    /**
     * Parse the storage size string returned by Hive and convert it to the number of bytes
     * For example: "100.5 MB" -> 105480192
     */
    private static long parseHiveSize(String sizeStr) {
        if (sizeStr == null || sizeStr.isEmpty()) {
            return 0;
        }

        sizeStr = sizeStr.trim();
        // Extract the numeric part and the unit part
        String numberStr = sizeStr.replaceAll("[^0-9.]", "");
        String unitStr = sizeStr.replaceAll("[0-9.]", "").trim().toUpperCase();

        double number = Double.parseDouble(numberStr);
        long multiplier = 1;

        // Convert to bytes based on units
        switch (unitStr) {
            case "KB":
                multiplier = 1024;
                break;
            case "MB":
                multiplier = 1024 * 1024;
                break;
            case "GB":
                multiplier = 1024 * 1024 * 1024;
                break;
            case "TB":
                multiplier = 1024L * 1024 * 1024 * 1024;
                break;
            // Defaults to bytes
        }

        return (long) (number * multiplier);
    }
}
