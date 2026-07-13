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
 * Class represents the response data for a payment request.
 * This class contains the result information returned by the payment gateway.
 *
 * @author qdata
 */
@Data
@AllArgsConstructor
public class PaymentResponse {

    /**
     * Payment status, indicating whether the payment was successful or failed.
     * For example, "SUCCESS" indicates success and "FAILED" indicates failure.
     */
    private String status;

    /**
     * Payment jump link, users can complete payment through this link.
     * This link is particularly important for payment methods that require users to jump (such as PC web payment).
     */
    private String paymentUrl;

    /**
     * The order's unique identifier in the merchant's system.
     * Used to identify the order for which this payment response is directed.
     */
    private String orderId;

}
