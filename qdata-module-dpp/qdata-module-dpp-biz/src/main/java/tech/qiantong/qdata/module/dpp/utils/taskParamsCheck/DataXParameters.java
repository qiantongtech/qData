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

package tech.qiantong.qdata.module.dpp.utils.taskParamsCheck;

import org.apache.commons.lang3.StringUtils;

public class DataXParameters extends AbstractParameters {

    private String someDataXParam;

    @Override
    public boolean checkParameters() {
        // 假设某个参数不能为空
        return StringUtils.isNotEmpty(someDataXParam);
    }

    // Getter and Setter
    public String getSomeDataXParam() {
        return someDataXParam;
    }

    public void setSomeDataXParam(String someDataXParam) {
        this.someDataXParam = someDataXParam;
    }
}
