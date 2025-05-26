package tech.qiantong.qdata.common.database.constants;

/**
 * <P>
 * 用途:判断类型
 * </p>
 *
 * @author: FXB
 * @create: 2024-07-31 15:56
 **/
public enum InterpretationType {

    GE("1", "大于等于", ">="),
    GT("2", "大于", ">"),
    LE("3", "小于等于", "<="),
    LT("4", "小于", "<"),
    EQ("5", "等于", "="),
    NE("6", "不等于", "!="),
    SCOPE("9", "范围", ""),
    OUTSIDE_RANGE("10", "范围外", "");

    /**
     * 数据库名称
     */
    private final String value;

    /**
     * 描述
     */
    private final String desc;

    /**
     * url
     */
    private final String symbol;


    public String getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getSymbol() {
        return this.symbol;
    }

    InterpretationType(String value, String desc, String symbol) {
        this.value = value;
        this.desc = desc;
        this.symbol = symbol;
    }

    /**
     * 获取判断类型
     */
    public static InterpretationType getInterpretationType(String dbType) {
        for (InterpretationType type : InterpretationType.values()) {
            if (type.value.equals(dbType)) {
                return type;
            }
        }
        return null;
    }
}
