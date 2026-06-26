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

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    @Qualifier("alipayClientImpl") // 指定要注入的Bean名称
    private PayGatewayClient alipayClient;

    @Autowired
    @Qualifier("weChatPayClientImpl") // 指定要注入的Bean名称
    private PayGatewayClient wechatPayClient;

    @Override
    public PaymentResponse pay(PaymentRequest request) {
        // 根据支付类型选择相应的支付网关客户端
        if (request.getPaymentType() == PaymentType.ALIPAY) {
            return alipayClient.initiatePayment(request);
        } else if (request.getPaymentType() == PaymentType.WECHAT) {
            return wechatPayClient.initiatePayment(request);
        } else {
            throw new UnsupportedOperationException("不支持的支付类型: " + request.getPaymentType());
        }
    }

    @Override
    public RefundResponse refund(RefundRequest request) {
        // 根据支付类型选择相应的退款操作
        if (request.getPaymentType() == PaymentType.ALIPAY) {
            return alipayClient.refund(request);
        } else if (request.getPaymentType() == PaymentType.WECHAT) {
            return wechatPayClient.refund(request);
        } else {
            throw new UnsupportedOperationException("不支持的支付类型: " + request.getPaymentType());
        }
    }

    @Override
    public PaymentStatusResponse queryStatus(String paymentId) {
        // 假设支付ID可以推断出支付类型，实际情况可能需要进一步处理
        if (paymentId.startsWith("ALIPAY")) {
            return alipayClient.queryStatus(paymentId);
        } else if (paymentId.startsWith("WECHAT")) {
            return wechatPayClient.queryStatus(paymentId);
        } else {
            throw new UnsupportedOperationException("不支持的支付ID类型: " + paymentId);
        }
    }
}
