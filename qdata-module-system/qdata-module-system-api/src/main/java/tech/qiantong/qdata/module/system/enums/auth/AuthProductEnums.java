package tech.qiantong.qdata.module.system.enums.auth;

/**
 * 认证平台类型
 *
 * @author Ming
 */
public enum AuthProductEnums {
    ANIVIA(0, "冰凤框架"),
    ALIPAY(1,"支付宝"),
    WECHAT(2,"微信");

    public final Integer code;
    public final String info;

    AuthProductEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static AuthProductEnums get(Integer code) {
        for (AuthProductEnums v : values()) {
            if (v.eq(String.valueOf(code))) {
                return v;
            }
        }
        return null;
    }

    // 根据code返回县市名称
    public static AuthProductEnums getNme(String info) {
        for (AuthProductEnums v : values()) {
            if (v.like(info)) {
                return v;
            }
        }
        return null;
    }

    public boolean eq(String code) {
        return this.code.equals(code);
    }

    public boolean like(String info) {
        return this.info.equals(info);
    }

    public static String getInfo(Integer code) {
        return AuthProductEnums.get(code)== null ? null : AuthProductEnums.get(code).info;
    }

    public static Integer getCode(String info) {
        AuthProductEnums nameEnums = AuthProductEnums.getNme(info);
        if (nameEnums == null) {
            return null;
        }else {
            return nameEnums.getCode();
        }

    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

}
