/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
