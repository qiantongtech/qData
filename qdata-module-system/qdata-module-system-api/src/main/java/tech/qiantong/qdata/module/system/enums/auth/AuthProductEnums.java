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

package tech.qiantong.qdata.module.system.enums.auth;

/**
 * Authentication Platform Type
 *
 * @author Ming
 */
public enum AuthProductEnums {
    ANIVIA(0, "Anivia Framework"),
    ALIPAY(1,"Alipay"),
    WECHAT(2,"WeChat");

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

    // Return enum by info name
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
