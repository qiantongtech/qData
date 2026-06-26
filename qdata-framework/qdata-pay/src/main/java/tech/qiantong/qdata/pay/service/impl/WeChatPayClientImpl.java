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
        // 调用微信支付API实现支付逻辑
        return new PaymentResponse("WECHAT_SUCCESS", "https://wechat.com/pay", request.getOrderId());
    }

    @Override
    public RefundResponse refund(RefundRequest request) {
        // 调用微信支付API实现退款逻辑
        return new RefundResponse("WECHAT_REFUND_SUCCESS", request.getOrderId());
    }

    @Override
    public PaymentStatusResponse queryStatus(String paymentId) {
        // 调用微信支付API查询支付状态
        return new PaymentStatusResponse("WECHAT_SUCCESS", paymentId);
    }

    @Override
    public Notification handleNotification(Map<String, String> parameters) {
        // 处理微信支付回调
        // 在此实现签名验证等逻辑
        return null;
    }
}
