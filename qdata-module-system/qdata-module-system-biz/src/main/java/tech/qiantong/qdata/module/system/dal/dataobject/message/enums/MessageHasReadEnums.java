package tech.qiantong.qdata.module.system.dal.dataobject.message.enums;

import java.util.Objects;

public enum MessageHasReadEnums {
    WD(0, "未读"),
    YD(1, "已读");
    public final Integer code;
    public final String info;

    MessageHasReadEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static MessageHasReadEnums get(Integer code) {
        for (MessageHasReadEnums v : values()) {
            if (v.eq(code)) {
                return v;
            }
        }
        return null;
    }

    // 根据code返回县市名称
    public static MessageHasReadEnums getName(String info) {
        for (MessageHasReadEnums v : values()) {
            if (v.like(info)) {
                return v;
            }
        }
        return null;
    }

    public boolean eq(Integer code) {
        return this.code.equals(code);
    }

    public boolean like(String info) {
        return this.info.equals(info);
    }

    public static String getInfo(Integer code) {
        return Objects.requireNonNull(MessageHasReadEnums.get(code)).getInfo();
    }

    public static Integer getCode(String info) {
        return Objects.requireNonNull(MessageHasReadEnums.getName(info)).getCode();
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
