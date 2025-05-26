package tech.qiantong.qdata.common.enums;

public enum RequestMethodEnum {

    GET("1", "get"),
    POST("2", "post");

    private final String key;

    private final String val;

    //根据value获取key

    RequestMethodEnum(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public String getVal() {
        return val;
    }

    /**
     * 根据给定的key查找对应的val。
     * 如果找不到与给定key匹配的枚举实例，则返回null。
     */
    public static String getValByKey(String key) {
        for (RequestMethodEnum method : RequestMethodEnum.values()) {
            if (method.getKey().equals(key)) {
                return method.getVal();
            }
        }
        // 如果没有找到匹配的键，则返回null
        return null;
    }

    //根据key，找value
    public static String getKeyByVal(String val) {
        for (RequestMethodEnum method : RequestMethodEnum.values()) {
            if (method.getVal().equals(val)) {
                return method.getKey();
            }
        }
        // 如果没有找到匹配的键，则返回null
        return null;
    }
}
