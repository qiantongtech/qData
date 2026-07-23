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

import java.util.Map;

/**
 * Represents the request data when initiating a payment request.
 * This class contains all the necessary information required by the payment gateway to process the payment.
 */
@Data
public class PaymentRequest {

    /**
     * The order's unique identifier in the merchant's system.
     * Used to associate payment transactions with merchant orders.
     */
    private String orderId;

    /**
     * The total amount paid, expressed in the smallest unit of currency (for example, RMB cents).
     * Use integer representation to avoid floating point precision issues.
     */
    private long amount;

    /**
     * The payment method selected by the user, such as Alipay (ALIPAY) or WeChat Pay (WECHAT).
     * Decide which payment gateway to use to process the transaction.
     */
    private PaymentType paymentType;

    /**
     * A brief description of the goods or services purchased.
     * It is usually displayed on the payment page or voucher to facilitate users to identify the payment content.
     */
    private String description;

    /**
     * The user's unique identifier in the merchant's system.
     * Used to associate payment records with users.
     */
    private String userId;

    /**
     * The IP address of the client that initiated the payment request.
     * Used for security verification and anti-fraud analysis.
     */
    private String clientIp;

    /**
     * Asynchronous callback notification URL after successful payment.
     * After the payment is completed, the merchant system will be notified of the payment result through this URL.
     */
    private String notifyUrl;

    /**
     * The URL of the page that the user will jump to after successful payment.
     * After the payment is successful, the payment gateway will direct the user to jump to this URL.
     */
    private String returnUrl;

    /**
     * Extension parameters allow passing additional custom business information.
     * Can be used to convey special business requirements during the payment process.
     */
    private Map<String, String> extraParams;

}
