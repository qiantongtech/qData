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

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class representing response data for payment status queries.
 * This class contains order payment status information returned by the payment gateway.
 *
 * @author qdata
 */
@Data
@AllArgsConstructor
public class PaymentStatusResponse {

    /**
     * Payment status, indicating the current payment status of the order.
     * For example, it can be represented by states such as "SUCCESS", "PENDING", and "FAILED".
     */
    private String status;

    /**
     * The order's unique identifier in the merchant's system.
     * Used to identify the order for which this payment status response is directed.
     */
    private String orderId;
}
