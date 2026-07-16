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

package tech.qiantong.qdata.pay.service.impl;

import org.springframework.stereotype.Service;
import tech.qiantong.qdata.pay.domain.*;
import tech.qiantong.qdata.pay.service.PayGatewayClient;

import java.util.Map;

@Service
public class WeChatPayClientImpl implements PayGatewayClient {

    @Override
    public PaymentResponse initiatePayment(PaymentRequest request) {
        // Call WeChat payment API to implement payment logic
        return new PaymentResponse("WECHAT_SUCCESS", "https://wechat.com/pay", request.getOrderId());
    }

    @Override
    public RefundResponse refund(RefundRequest request) {
        // Call WeChat payment API to implement refund logic
        return new RefundResponse("WECHAT_REFUND_SUCCESS", request.getOrderId());
    }

    @Override
    public PaymentStatusResponse queryStatus(String paymentId) {
        // Call WeChat payment API to check payment status
        return new PaymentStatusResponse("WECHAT_SUCCESS", paymentId);
    }

    @Override
    public Notification handleNotification(Map<String, String> parameters) {
        // Handling WeChat payment callbacks
        // Implement signature verification and other logic here
        return null;
    }
}
