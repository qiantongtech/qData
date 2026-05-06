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
 * Hive数据库方言实现
 */
@Slf4j
public class HiveDialect implements DatabaseDialect {

    @Override
    public String getStorageEngine(McDbDO mcDbDO) {
        try {
            // Hive使用HDFS存储，这里返回Hive
            return "Hive";
        } catch (Exception e) {
            log.error("获取Hive存储引擎失败", e);
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
        // Hive不支持自增字段，直接返回false
        log.info("Hive不支持自增字段");
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
     * 构建连接信息
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
            log.error("解密密码失败", e);
            return null;
        }

        return new ConnectionInfo(url, username, password);
    }

    /**
     * 连接信息类
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
            // 构建连接信息
            ConnectionInfo connectionInfo = buildConnectionInfo(mcDbDO);
            if (connectionInfo == null) {
                return dbMetadata;
            }

            // 连接数据库并执行查询
            try (Connection conn = DriverManager.getConnection(connectionInfo.getUrl(), connectionInfo.getUsername(), connectionInfo.getPassword())) {
                List<String> tables = getTableNames(conn, mcDbDO.getDbName());
                long totalSizeBytes = 0;
                for (String table : tables) {
                    totalSizeBytes += getTableSizeBytes(conn, table);
                }

                // 设置存储大小
                dbMetadata.setStorageSize((int) (totalSizeBytes / 1024.0 / 1024.0));
            }
        } catch (Exception e) {
            log.error("批量获取Hive数据库元数据失败", e);
        }
        return dbMetadata;
    }

    // 获取指定库中所有表名
    private static List<String> getTableNames(Connection conn, String dbName) throws SQLException {
        List<String> tables = new ArrayList<>();
        // 切换到指定数据库
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("USE " + dbName);
            // 获取所有表
            ResultSet rs = conn.getMetaData().getTables(null, dbName, null, new String[]{"TABLE"});
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }

    // 查询单个表的大小 (单位: 字节)
    private static long getTableSizeBytes(Connection conn, String tableName) throws SQLException {
        String sql = "DESCRIBE FORMATTED " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // DESCRIBE FORMATTED 返回三列: col_name, data_type, comment
                String colName = rs.getString("col_name");
                if ("Total Size".equals(colName)) {
                    String sizeStr = rs.getString("data_type").replaceAll(",", ""); // 移除数字中的逗号
                    return Long.parseLong(sizeStr);
                }
                String data_type = rs.getString("data_type");
            }
        }
        return 0;
    }
    // 查询表级注释
    private static String getTableComment(Connection conn, String tableName) throws SQLException {
        String sql = "DESCRIBE FORMATTED " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // DESCRIBE FORMATTED 返回三列: col_name, data_type, comment
                String data_type = rs.getString("data_type");
                if (StringUtils.isNotBlank(data_type)&&data_type.contains("comment")) {
                    String comment = rs.getString("comment");
                    return comment;
                }
            }
        }
        return "";
    }

    // 查询表创建时间
    private static String getTableCreateTime(Connection conn, String tableName) throws SQLException {
        String sql = "DESCRIBE FORMATTED " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // DESCRIBE FORMATTED 返回三列: col_name, data_type, comment
                String colName = rs.getString("col_name");
                if ("Created".equals(colName)) {
                    String originalTime = rs.getString("data_type");
                    // 转换时间格式为 yyyy-MM-dd HH:mm:ss
                    try {
                        // Hive返回的时间格式通常为：Thu Apr 14 10:00:00 CST 2026
                        java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.ENGLISH);
                        java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        java.util.Date date = inputFormat.parse(originalTime);
                        return outputFormat.format(date);
                    } catch (Exception e) {
                        // 解析失败，返回原始时间
                        log.error("时间转换失败：{}", e.getMessage());
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
            // 构建连接信息
            ConnectionInfo connectionInfo = buildConnectionInfo(mcDbDO);
            if (connectionInfo == null) {
                return metadata;
            }

            // 连接数据库并执行查询
            try (Connection conn = DriverManager.getConnection(connectionInfo.getUrl(), connectionInfo.getUsername(), connectionInfo.getPassword())) {
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

                // 获取表分区字段信息
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
                    log.warn("非hive分区表: {}", e.getMessage());
                    metadata.setPartitionFields("");
                }

                // 获取表存储大小
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SHOW TABLE EXTENDED LIKE '" + tableName + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    while (rs.next()) {
                        String line = rs.getString(1);
                        if (line != null && line.contains("totalFileSize:")) {
                            // 解析存储大小，直接获取字节数
                            int startIndex = line.indexOf("totalFileSize:") + "totalFileSize:".length();
                            String sizeStr = line.substring(startIndex).trim();
                            try {
                                Integer size = Integer.valueOf(sizeStr);
                                // 转换为MB，保留两位小数
                                metadata.setTableSize(size);
                            } catch (NumberFormatException e) {
                                log.warn("解析Hive表存储大小失败: {}", sizeStr);
                            }
                            break;
                        }
                    }
                }
                metadata.setTableComment(getTableComment(conn,tableName));

                // Hive不支持主键，设置为空
                metadata.setPrimaryKey("");

                // 使用DESCRIBE FORMATTED查询获取创建时间和InputFormat
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
                            // 转换时间格式为 yyyy-MM-dd HH:mm:ss
                            try {
                                // Hive返回的时间格式通常为：Thu Apr 14 10:00:00 CST 2026
                                java.text.SimpleDateFormat dataFormat = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.ENGLISH);
                                java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                java.util.Date date = dataFormat.parse(createTime);
                                createTime = outputFormat.format(date);
                            } catch (Exception e) {
                                // 解析失败，返回原始时间
                                log.error("时间转换失败：{}", e.getMessage());
                            }
                        }
                    }
                    // 设置创建时间（Hive不支持修改时间，设置为创建时间）
                    metadata.setCreateTime(createTime);
                    metadata.setUpdateTime(null);

                    // 根据InputFormat映射存储引擎类型
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
                        // 处理其他存储格式
                        metadata.setStorageEngine(inputFormat);
                    } else {
                        metadata.setStorageEngine("Hive");
                    }
                } catch (SQLException e) {
                    log.warn("获取Hive表创建时间和存储引擎失败: {}", e.getMessage());
                    metadata.setStorageEngine("Hive");
                }
            }
        } catch (Exception e) {
            log.error("批量获取Hive表元数据失败", e);
        }
        return metadata;
    }

    @Override
    public ColumnMetadata getColumnMetadata(McDbDO mcDbDO, String tableName, String columnName) {
        ColumnMetadata metadata = new ColumnMetadata();
        try {
            // Hive不支持自增字段
            metadata.setAutoIncrement(false);
            // Hive不支持唯一约束
            metadata.setUnique(false);

            // 构建连接信息
            ConnectionInfo connectionInfo = buildConnectionInfo(mcDbDO);
            if (connectionInfo == null) {
                return metadata;
            }

            // 连接数据库并执行查询
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
            log.error("批量获取Hive字段元数据失败", e);
        }
        return metadata;
    }

    /**
     * 解析Hive返回的存储大小字符串，转换为字节数
     * 例如："100.5 MB" -> 105480192
     */
    private static long parseHiveSize(String sizeStr) {
        if (sizeStr == null || sizeStr.isEmpty()) {
            return 0;
        }

        sizeStr = sizeStr.trim();
        // 提取数字部分和单位部分
        String numberStr = sizeStr.replaceAll("[^0-9.]", "");
        String unitStr = sizeStr.replaceAll("[0-9.]", "").trim().toUpperCase();

        double number = Double.parseDouble(numberStr);
        long multiplier = 1;

        // 根据单位转换为字节
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
            // 默认为字节
        }

        return (long) (number * multiplier);
    }
}
