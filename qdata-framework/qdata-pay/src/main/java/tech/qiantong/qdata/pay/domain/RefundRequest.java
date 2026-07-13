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

import lombok.Data;

/**
 * Class represents the data for a refund request.
 * This class contains all the information needed to initiate a refund operation.
 *
 * @author qdata
 */
@Data
public class RefundRequest {

    /**
     * The order's unique identifier in the merchant's system.
     * Used to identify orders that require refunds.
     */
    private String orderId;

    /**
     * Refund amount, expressed in the smallest unit of currency (for example, RMB cents).
     * Normally the refund amount should not exceed the original payment amount.
     */
    private long amount;

    /**
     * Payment method, such as Alipay (ALIPAY) or WeChat Pay (WECHAT).
     * Used to specify which payment gateway to process refunds through.
     */
    private PaymentType paymentType;
}
