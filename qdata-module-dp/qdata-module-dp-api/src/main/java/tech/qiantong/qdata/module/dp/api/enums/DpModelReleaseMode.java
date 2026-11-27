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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dp.api.enums;


/**
 * <P>
 * Purpose: Logical Model Release Mode
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-26 15:39
 **/
public enum DpModelReleaseMode {

    DELETE_REBUILD("1", "Delete and Rebuild"),
    INCREMENT_RELEASE("2", "Incremental Release");
    private String code;
    private String desc;

    DpModelReleaseMode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * Get enum by code
     *
     * @param code
     * @return
     */
    public static DpModelReleaseMode getByCode(String code) {
        for (DpModelReleaseMode value : DpModelReleaseMode.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
