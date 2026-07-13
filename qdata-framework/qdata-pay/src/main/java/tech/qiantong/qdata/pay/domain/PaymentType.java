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

package tech.qiantong.qdata.pay.domain;

/**
 * Defines the payment methods supported in the system.
 *
 * @author qdata
 */
public enum PaymentType {

    /**
     * Alipay payment
     */
    ALIPAY,

    /**
     * WeChat Pay
     */
    WECHAT;

    /**
     * Get the corresponding payment type based on the input string value.
     *
     * @param type input string, such as "ALIPAY" or "WECHAT"
     * @return Returns the corresponding PaymentType enumeration value, or null if there is no match
     */
    public static PaymentType fromString(String type) {
        for (PaymentType paymentType : PaymentType.values()) {
            if (paymentType.name().equalsIgnoreCase(type)) {
                return paymentType;
            }
        }
        return null;
    }
}
