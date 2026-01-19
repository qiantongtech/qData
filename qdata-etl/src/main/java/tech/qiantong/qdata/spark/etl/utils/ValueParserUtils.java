package tech.qiantong.qdata.spark.etl.utils;

/**
 * 字符串转化工具类
 */
public class ValueParserUtils {
    /**
     * 安全解析 int，非法返回 0
     */
    public static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 安全解析 long，非法返回 0L
     */
    public static long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * 安全解析 double，非法返回 0.0
     */
    public static double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * 安全解析 boolean，支持 true/1 识别为 true，其它为 false
     */
    public static boolean parseBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "1".equals(value);
    }

    /**
     * 转义字符串，用于正则匹配
     * @param str
     * @return
     */
    public static String escapeLiteral(String str) {
        return str.replaceAll("([\\\\.^$|?*+()\\[\\]{}])", "\\\\$1");
    }


    /**
     * SQL 字符串转义方法
     * // 替换单引号为两个单引号，防止 SQL 报错
     * @param str
     * @return
     */
    public static String escapeForSQL(String str) {
        if (str == null) return "";
        // 替换单引号为两个单引号，防止 SQL 报错
        return str.replace("'", "''");
    }

}
