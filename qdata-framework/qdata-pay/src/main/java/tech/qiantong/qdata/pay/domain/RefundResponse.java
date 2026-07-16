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
 * Class represents the response data for a refund request.
 * This class contains the refund result information returned by the payment gateway.
 * @author qdata
 */
@Data
@AllArgsConstructor
public class RefundResponse {

    /**
     * Refund status, indicating whether the refund operation is successful or failed.
     */
    private String status;

    /**
     * The order's unique identifier in the merchant's system.
     */
    private String orderId;

}
