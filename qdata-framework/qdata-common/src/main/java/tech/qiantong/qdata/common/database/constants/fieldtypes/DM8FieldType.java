/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.common.database.constants.fieldtypes;

import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.utils.MD5Util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public enum DM8FieldType {
    // Short text type
    CHAR("CHAR", 32767, "1"),
    VARCHAR("VARCHAR", 32767, "255"),
    VARCHAR2("VARCHAR2", 32767, "255"),

    // Long text type
    TEXT("TEXT", Integer.MAX_VALUE, ""),
    CLOB("CLOB", Integer.MAX_VALUE, ""),
    NCLOB("NCLOB", Integer.MAX_VALUE, ""),

    // Integer type
    TINYINT("TINYINT", 3, ""),
    SMALLINT("SMALLINT", 5, ""),
    INT("INT", 10, ""),
    INTEGER("INTEGER", 10, ""),
    BIGINT("BIGINT", 19, ""),

    // Decimal type
    DECIMAL("DECIMAL", 38, "10,0"),
    NUMERIC("NUMERIC", 38, "10,0"),
    NUMBER("NUMBER", 38, "10,0"),

    // Floating point type
    FLOAT("FLOAT", 38, ""),
    DOUBLE("DOUBLE", 38, ""),
    REAL("REAL", 38, ""),

    // Boolean type
    BIT("BIT", 1, ""),
    BOOLEAN("BOOLEAN", 1, ""),

    // Datetime type
    DATE("DATE", 0, ""),
    TIME("TIME", 0, ""),
    TIMESTAMP("TIMESTAMP", 6, "6"),
    DATETIME("DATETIME", 6, "6"),
    INTERVAL_YEAR_TO_MONTH("INTERVAL YEAR TO MONTH", 0, ""),
    INTERVAL_DAY_TO_SECOND("INTERVAL DAY TO SECOND", 0, ""),

    // Binary type
    VARBINARY("VARBINARY", 32767, "255"),
    RAW("RAW", 32767, "255"),
    LONG_VARBINARY("LONG VARBINARY", Integer.MAX_VALUE, ""),

    // Large object type
    BLOB("BLOB", Integer.MAX_VALUE, ""),
    IMAGE("IMAGE", Integer.MAX_VALUE, ""),

    // JSON/XML type
    JSON("JSON", Integer.MAX_VALUE, ""),
    XML("XML", Integer.MAX_VALUE, ""),

    // Other types
    ROWID("ROWID", 0, ""),
    UUID("UUID", 36, "36"),
    GEOMETRY("GEOMETRY", Integer.MAX_VALUE, ""),
    POINT("POINT", 0, ""),
    LINE("LINE", 0, ""),
    POLYGON("POLYGON", 0, "");

    private final String type;
    private final int maxLength;
    private final String defaultValue; // Default values (length, parameters, etc.)

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
     * Get enumeration based on data type
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
     * Verify whether DbColumn conforms to the specification
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

        // 1. The column name cannot be empty and the length cannot exceed 30 characters.
        if (colName == null || colName.trim().isEmpty()) {
            errors.add("列名不能为空。");
        } else if (colName.length() > 30) {
            errors.add("字段 '" + colName + "' 长度不能超过30个字符。");
        }

        // 2. The data type must be a type supported by Dameng
        DM8FieldType fieldType = getTypeByName(dataType);
        if (fieldType == null) {
            errors.add("字段 '" + colName + "' 的数据类型 '" + dataType + "' 不支持。");
            return errors; // Unsupported types are returned directly.
        }

        // 3. Length check (only for types with length restrictions)
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

        // 4. Decimal place verification (only for decimal types)
        if (isDecimal(fieldType.getType())) {
            if (dataScale != null && !dataScale.matches("\\d+")) {
                errors.add("字段 '" + colName + "' 的小数位必须为正整数。");
            }
        }

        // 5. The primary key field is not allowed to be empty.
        if (Boolean.TRUE.equals(colKey) && Boolean.TRUE.equals(nullable)) {
            errors.add("字段 '" + colName + "' 作为主键时不能为空。");
        }

        // 6. Default value verification (mainly for time type)
        if (isTime(fieldType.getType()) && dataDefault != null) {
            if (!dataDefault.matches("^\\d{4}-\\d{2}-\\d{2}( \\d{2}:\\d{2}:\\d{2})?$")) {
                errors.add("字段 '" + colName + "' 的默认值格式错误，正确格式应为 'YYYY-MM-DD' 或 'YYYY-MM-DD HH:MI:SS'。");
            }
        }

        return errors;
    }

    /**
     * Determine whether the data type exists
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
     * Whether it is a decimal type
     */
    public static boolean isDecimal(String dataType) {
        return dataType != null && (
                dataType.equalsIgnoreCase(DECIMAL.getType()) ||
                        dataType.equalsIgnoreCase(NUMERIC.getType()) ||
                        dataType.equalsIgnoreCase(NUMBER.getType())
        );
    }

    /**
     * Whether it is time type
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
     * Mapping DM8 database column types
     */
    public static String mapDmColumnType(DbColumn col) {
        DM8FieldType typeEnum = getByType(col.getDataType());
        if (typeEnum == null) {
            return col.getDataType(); // If the type is not supported, the original value will be returned directly.
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
                return "TEXT"; // Dameng can also use CLOB
            default:
                return typeEnum.getType();
        }
    }


    /**
     * Determine whether it is a string type
     */
    private static final Set<DM8FieldType> STRING_TYPES = EnumSet.of(
            VARCHAR, VARCHAR2, CHAR, CLOB, TEXT
    );

    public static boolean isStringType(String columnType) {
        DM8FieldType type = getByType(columnType);
        return type != null && STRING_TYPES.contains(type);
    }
}
