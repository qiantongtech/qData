package tech.qiantong.qdata.common.database.utils;


import org.apache.commons.collections4.MapUtils;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.MessageUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * Data connection tool methods
 */
public class DatabaseUtil {



    /**
     * Convert a string to type Long. If the string is empty or cannot be converted, 0L is returned.
     *
     * @param dataLength The string to be converted
     * @return converted Long type value
     */
    public static Long getStringToLong(String dataLength) {
        if (StringUtils.isEmpty(dataLength)) {
            return 0L;
        }
        try {
            return Long.parseLong(dataLength);
        } catch (NumberFormatException e) {
            // If the conversion fails, 0L is returned
            return 0L;
        }
    }

    /**
     * Escape single quotes in strings to avoid errors when splicing SQL
     */
    public static String escapeSingleQuotes(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''");
    }



    /**
     * If the input string is all lowercase, it will be converted to uppercase and returned; otherwise, the original string will be returned directly.
     *
     * @param input input string
     * @return If it is all lowercase, return all uppercase string; otherwise return the original string
     */
    public static String convertIfLowercase(String input) {
        if (input == null) {
            return null;
        }
        // If the string is the same as its lowercase counterpart, it means all lowercase
        if (input.equals(input.toLowerCase())) {
            return input.toUpperCase();
        }
        return input;
    }


    /**
     * Normalized database type
     *
     * @param dbType database type, supports: MySql, Oracle11, Oracle, DM8, Kingbase8
     * @return Standardized database type, return value is MYSQL, ORACLE, DM8, KINGBASE, Oracle11 also returns ORACLE
     * @throws IllegalArgumentException thrown when dbType is null or empty string
     */
    public static String getNormalizedDbType(String dbType) {
        if (dbType == null || dbType.isEmpty()) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "sys.error.database.type.empty", "Database type cannot be empty"));
        }
        if (DbType.MYSQL.getDb().equals(dbType)) {
            return "MYSQL";
        } else if (DbType.ORACLE.getDb().equals(dbType) || DbType.ORACLE_12C.getDb().equals(dbType)) {
            return "ORACLE";
        } else if (DbType.DM8.getDb().equals(dbType)) {
            return "DAMENG";
        } else if (DbType.KINGBASE8.getDb().equals(dbType)) {
            return "KINGBASE";
        }
        // Returns primitive type value by default
        return dbType;
    }


    /**
     * Create object
     *
     * @param datasource
     * @return
     */
    public static DbQueryProperty buildJobDatasource(Map<String, Object> datasource) {
        String ip = MapUtils.getString(datasource, "ip");
        Long port = MapUtils.getLong(datasource, "port");
        String datasourceConfig = MapUtils.getString(datasource, "datasourceConfig");
        String datasourceType = MapUtils.getString(datasource, "datasourceType");
//
//        JSONObject configJson;
//        try {
//            configJson = JSON.parseObject(datasourceConfig);
//        } catch (Exception e) {
// throw new DataQueryException("db.error.datasource.config.json", "The data source configuration format is wrong and should be legal JSON");
//        }
//        DbQueryProperty dbQueryProperty = new DbQueryProperty(datasourceType, ip,
//                configJson.getString("username"), configJson.getString("password"), port,
//                configJson.getString("dbname"), configJson.getString("sid"));
        DbQueryProperty dbQueryProperty = new DbQueryProperty(datasourceType, ip, port, datasourceConfig);

        return dbQueryProperty;
    }



    public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();
        for (int i = 1; i <= count; i++) {
            if (columnName.equalsIgnoreCase(meta.getColumnLabel(i))) {
                return true;
            }
        }
        return false;
    }
}
