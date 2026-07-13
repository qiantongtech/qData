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

package tech.qiantong.qdata.pay.service;

import tech.qiantong.qdata.pay.domain.*;

import java.util.Map;

/**
 * The interface defines the basic operations for integrating with payment gateways.
 * This interface includes methods for initiating payment, refunding, querying payment status, and handling callbacks.
 * @author qdata
 */
public interface PayGatewayClient {

    /**
     * Initiate a payment request.
     * @param request contains details of the payment request
     * @return returns payment response information
     */
    PaymentResponse initiatePayment(PaymentRequest request);

    /**
     * Initiate a refund request.
     * @param request Contains details of the refund request
     * @return returns refund response information
     */
    RefundResponse refund(RefundRequest request);

    /**
     * Check payment status.
     * @param paymentId unique identifier of the payment order
     * @return Returns payment status response information
     */
    PaymentStatusResponse queryStatus(String paymentId);

    /**
     * Handle payment callback notifications.
     * @param parameters contains all parameters of the callback notification
     */
    Notification handleNotification(Map<String, String> parameters);
}
