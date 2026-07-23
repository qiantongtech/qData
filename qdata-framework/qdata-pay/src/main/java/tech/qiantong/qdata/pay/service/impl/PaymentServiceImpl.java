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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.pay.domain.*;
import tech.qiantong.qdata.pay.service.PayGatewayClient;
import tech.qiantong.qdata.pay.service.PaymentService;
import tech.qiantong.qdata.common.utils.MessageUtils;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    @Qualifier("alipayClientImpl") // Specify the name of the bean to be injected
    private PayGatewayClient alipayClient;

    @Autowired
    @Qualifier("weChatPayClientImpl") // Specify the name of the bean to be injected
    private PayGatewayClient wechatPayClient;

    @Override
    public PaymentResponse pay(PaymentRequest request) {
        // Select the appropriate payment gateway client based on the payment type
        if (request.getPaymentType() == PaymentType.ALIPAY) {
            return alipayClient.initiatePayment(request);
        } else if (request.getPaymentType() == PaymentType.WECHAT) {
            return wechatPayClient.initiatePayment(request);
        } else {
            throw new UnsupportedOperationException(MessageUtils.messageWithFallback(
                    "sys.error.payment.type.unsupported", "Unsupported payment type: {0}",
                    request.getPaymentType()));
        }
    }

    @Override
    public RefundResponse refund(RefundRequest request) {
        // Select the appropriate refund operation based on the payment type
        if (request.getPaymentType() == PaymentType.ALIPAY) {
            return alipayClient.refund(request);
        } else if (request.getPaymentType() == PaymentType.WECHAT) {
            return wechatPayClient.refund(request);
        } else {
            throw new UnsupportedOperationException(MessageUtils.messageWithFallback(
                    "sys.error.payment.type.unsupported", "Unsupported payment type: {0}",
                    request.getPaymentType()));
        }
    }

    @Override
    public PaymentStatusResponse queryStatus(String paymentId) {
        // Assuming that the payment type can be inferred from the payment ID, the actual situation may require further processing
        if (paymentId.startsWith("ALIPAY")) {
            return alipayClient.queryStatus(paymentId);
        } else if (paymentId.startsWith("WECHAT")) {
            return wechatPayClient.queryStatus(paymentId);
        } else {
            throw new UnsupportedOperationException(MessageUtils.messageWithFallback(
                    "sys.error.payment.id.type.unsupported", "Unsupported payment ID type: {0}", paymentId));
        }
    }
}
