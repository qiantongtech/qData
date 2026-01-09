package tech.qiantong.qdata.common.database.utils;


import org.apache.commons.collections4.MapUtils;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.utils.StringUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * 数据连接工具方法
 */
public class DatabaseUtil {



    /**
     * 将字符串转换为 Long 类型。如果字符串为空或无法转换，则返回 0L。
     *
     * @param dataLength 要转换的字符串
     * @return 转换后的 Long 类型值
     */
    public static Long getStringToLong(String dataLength) {
        if (StringUtils.isEmpty(dataLength)) {
            return 0L;
        }
        try {
            return Long.parseLong(dataLength);
        } catch (NumberFormatException e) {
            // 如果转换失败，则返回 0L
            return 0L;
        }
    }

    /**
     * 转义字符串中的单引号，避免拼接 SQL 时出错
     */
    public static String escapeSingleQuotes(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''");
    }



    /**
     * 如果输入的字符串全部为小写，则转换为大写返回，否则直接返回原字符串。
     *
     * @param input 输入字符串
     * @return 如果是全小写，返回全大写字符串；否则返回原字符串
     */
    public static String convertIfLowercase(String input) {
        if (input == null) {
            return null;
        }
        // 如果字符串与它的小写形式相同，说明全为小写
        if (input.equals(input.toLowerCase())) {
            return input.toUpperCase();
        }
        return input;
    }


    /**
     * 规范化数据库类型
     *
     * @param dbType 数据库类型，支持：MySql、Oracle11、Oracle、DM8、Kingbase8
     * @return 规范化后的数据库类型，返回值为 MYSQL、ORACLE、DM8、KINGBASE，其中 Oracle11 也返回 ORACLE
     * @throws IllegalArgumentException 当 dbType 为 null 或空字符串时抛出异常
     */
    public static String getNormalizedDbType(String dbType) {
        if (dbType == null || dbType.isEmpty()) {
            throw new IllegalArgumentException("数据库类型不能为空");
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
        // 默认返回原始类型的值
        return dbType;
    }


    /**
     * 创建对象
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
//            throw new DataQueryException("数据源配置格式错误，应为合法的 JSON");
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
