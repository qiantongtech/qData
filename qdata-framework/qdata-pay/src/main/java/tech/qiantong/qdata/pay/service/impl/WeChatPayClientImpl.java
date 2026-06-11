/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
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
