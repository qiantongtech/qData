package tech.qiantong.qdata.common.database.constants.fieldtypes;

import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.utils.MD5Util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public enum DM8FieldType {
    // 短文本类型
    CHAR("CHAR", 32767, "1"),
    VARCHAR("VARCHAR", 32767, "255"),
    VARCHAR2("VARCHAR2", 32767, "255"),

    // 长文本类型
    TEXT("TEXT", Integer.MAX_VALUE, ""),
    CLOB("CLOB", Integer.MAX_VALUE, ""),
    NCLOB("NCLOB", Integer.MAX_VALUE, ""),

    // 整数类型
    TINYINT("TINYINT", 3, ""),
    SMALLINT("SMALLINT", 5, ""),
    INT("INT", 10, ""),
    INTEGER("INTEGER", 10, ""),
    BIGINT("BIGINT", 19, ""),

    // 小数类型
    DECIMAL("DECIMAL", 38, "10,0"),
    NUMERIC("NUMERIC", 38, "10,0"),
    NUMBER("NUMBER", 38, "10,0"),

    // 浮点数类型
    FLOAT("FLOAT", 38, ""),
    DOUBLE("DOUBLE", 38, ""),
    REAL("REAL", 38, ""),

    // 布尔类型
    BIT("BIT", 1, ""),
    BOOLEAN("BOOLEAN", 1, ""),

    // 日期时间类型
    DATE("DATE", 0, ""),
    TIME("TIME", 0, ""),
    TIMESTAMP("TIMESTAMP", 6, "6"),
    DATETIME("DATETIME", 6, "6"),
    INTERVAL_YEAR_TO_MONTH("INTERVAL YEAR TO MONTH", 0, ""),
    INTERVAL_DAY_TO_SECOND("INTERVAL DAY TO SECOND", 0, ""),

    // 二进制类型
    VARBINARY("VARBINARY", 32767, "255"),
    RAW("RAW", 32767, "255"),
    LONG_VARBINARY("LONG VARBINARY", Integer.MAX_VALUE, ""),

    // 大对象类型
    BLOB("BLOB", Integer.MAX_VALUE, ""),
    IMAGE("IMAGE", Integer.MAX_VALUE, ""),

    // JSON/XML 类型
    JSON("JSON", Integer.MAX_VALUE, ""),
    XML("XML", Integer.MAX_VALUE, ""),

    // 其他类型
    ROWID("ROWID", 0, ""),
    UUID("UUID", 36, "36"),
    GEOMETRY("GEOMETRY", Integer.MAX_VALUE, ""),
    POINT("POINT", 0, ""),
    LINE("LINE", 0, ""),
    POLYGON("POLYGON", 0, "");

    private final String type;
    private final int maxLength;
    private final String defaultValue; // 默认值（长度、参数等）

    DM8FieldType(String type, int maxLength, String defaultValue) {
        this.type = type;
        this.maxLength = maxLength;
        this.defaultValue = defaultValue;
    }

    public String getType() {
        return type;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * 根据数据类型获取枚举
     */
    public static DM8FieldType getByType(String dataType) {
        for (DM8FieldType type : DM8FieldType.values()) {
            if (type.getType().equalsIgnoreCase(dataType)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 校验 DbColumn 是否符合规范
     */
    public static List<String> validateColumn(DbColumn column) {
        List<String> errors = new ArrayList<>();

        if (column == null) {
            errors.add("列对象不能为空。");
            return errors;
        }

        String colName = column.getColName();
        String dataType = column.getDataType();
        String dataLength = column.getDataLength();
        String dataScale = column.getDataScale();
        Boolean colKey = column.getColKey();
        Boolean nullable = column.getNullable();
        String dataDefault = column.getDataDefault();

        // 1. 列名不能为空，且长度不能超过30个字符
        if (colName == null || colName.trim().isEmpty()) {
            errors.add("列名不能为空。");
        } else if (colName.length() > 30) {
            errors.add("字段 '" + colName + "' 长度不能超过30个字符。");
        }

        // 2. 数据类型必须为达梦支持的类型
        DM8FieldType fieldType = getTypeByName(dataType);
        if (fieldType == null) {
            errors.add("字段 '" + colName + "' 的数据类型 '" + dataType + "' 不支持。");
            return errors; // 不支持的类型，直接返回
        }

        // 3. 长度校验（仅对有长度限制的类型）
        if (fieldType.getMaxLength() > 0) {
            if (dataLength == null || !dataLength.matches("\\d+")) {
                errors.add("字段 '" + colName + "' 的数据长度必须为正整数。");
            } else {
                int length = Integer.parseInt(dataLength);
                if (length > fieldType.getMaxLength()) {
                    errors.add("字段 '" + colName + "' 的数据长度不能超过 " + fieldType.getMaxLength() + "。");
                }
            }
        }

        // 4. 小数位校验（仅对小数类型）
        if (isDecimal(fieldType.getType())) {
            if (dataScale != null && !dataScale.matches("\\d+")) {
                errors.add("字段 '" + colName + "' 的小数位必须为正整数。");
            }
        }

        // 5. 主键字段不允许为空
        if (Boolean.TRUE.equals(colKey) && Boolean.TRUE.equals(nullable)) {
            errors.add("字段 '" + colName + "' 作为主键时不能为空。");
        }

        // 6. 默认值校验（主要针对时间类型）
        if (isTime(fieldType.getType()) && dataDefault != null) {
            if (!dataDefault.matches("^\\d{4}-\\d{2}-\\d{2}( \\d{2}:\\d{2}:\\d{2})?$")) {
                errors.add("字段 '" + colName + "' 的默认值格式错误，正确格式应为 'YYYY-MM-DD' 或 'YYYY-MM-DD HH:MI:SS'。");
            }
        }

        return errors;
    }

    /**
     * 判断数据类型是否存在
     */
    public static DM8FieldType getTypeByName(String dataType) {
        for (DM8FieldType type : DM8FieldType.values()) {
            if (type.getType().equalsIgnoreCase(dataType)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 是否为小数类型
     */
    public static boolean isDecimal(String dataType) {
        return dataType != null && (
                dataType.equalsIgnoreCase(DECIMAL.getType()) ||
                        dataType.equalsIgnoreCase(NUMERIC.getType()) ||
                        dataType.equalsIgnoreCase(NUMBER.getType())
        );
    }

    /**
     * 是否为时间类型
     */
    public static boolean isTime(String dataType) {
        return dataType != null && (
                dataType.equalsIgnoreCase(DATE.getType()) ||
                        dataType.equalsIgnoreCase(TIME.getType()) ||
                        dataType.equalsIgnoreCase(TIMESTAMP.getType()) ||
                        dataType.equalsIgnoreCase(DATETIME.getType())
        );
    }


    /**
     *  映射 DM8 数据库列类型
     */
    public static String mapDmColumnType(DbColumn col) {
        DM8FieldType typeEnum = getByType(col.getDataType());
        if (typeEnum == null) {
            return col.getDataType(); // 不支持的类型，直接返回原值
        }

        Long length = MD5Util.getStringToLong(col.getDataLength());
        Long scale = MD5Util.getStringToLong(col.getDataScale());

        switch (typeEnum) {
            case VARCHAR:
            case VARCHAR2:
                return typeEnum.getType() + "(" + (length != null ? length : typeEnum.getDefaultValue()) + ")";
            case CHAR:
                return "CHAR(" + (length != null ? length : typeEnum.getDefaultValue()) + ")";
            case INT:
            case INTEGER:
                return "INT";
            case BIGINT:
                return "BIGINT";
            case DECIMAL:
                return "DECIMAL(" + (length != null ? length : "10") + "," + (scale != null ? scale : "0") + ")";
            case DATE:
            case DATETIME:
                return "TIMESTAMP";
            case TEXT:
            case CLOB:
                return "TEXT"; // 达梦也可使用 CLOB
            default:
                return typeEnum.getType();
        }
    }


    /**
     * 判断是否为字符串类型
     */
    private static final Set<DM8FieldType> STRING_TYPES = EnumSet.of(
            VARCHAR, VARCHAR2, CHAR, CLOB, TEXT
    );

    public static boolean isStringType(String columnType) {
        DM8FieldType type = getByType(columnType);
        return type != null && STRING_TYPES.contains(type);
    }
}
