package tech.qiantong.qdata.spark.etl.utils;

/**
 * String conversion tool class
 */
public class ValueParserUtils {
    /**
     * Safe parsing of int, illegal return of 0
     */
    public static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Safely parse long, illegally return 0L
     */
    public static long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * Safely parses double, returns 0.0 illegally
     */
    public static double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Safely parse boolean, support true/1 to be recognized as true, others as false
     */
    public static boolean parseBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "1".equals(value);
    }

    /**
     * Escape string for regular matching
     * @param str
     * @return
     */
    public static String escapeLiteral(String str) {
        return str.replaceAll("([\\\\.^$|?*+()\\[\\]{}])", "\\\\$1");
    }


    /**
     * SQL string escaping methods
     * //Replace the single quote with two single quotes to prevent SQL errors.
     * @param str
     * @return
     */
    public static String escapeForSQL(String str) {
        if (str == null) return "";
        // Replace single quotes with two single quotes to prevent SQL errors.
        return str.replace("'", "''");
    }

}
