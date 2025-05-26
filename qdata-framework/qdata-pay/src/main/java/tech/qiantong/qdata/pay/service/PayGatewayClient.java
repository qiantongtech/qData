package tech.qiantong.qdata.pay.service;

import tech.qiantong.qdata.pay.domain.*;

import java.util.Map;

/**
 * 接口定义了与支付网关集成的基本操作。
 * 该接口包括发起支付、退款、查询支付状态以及处理回调的方法。
 * @author qdata
 */
public interface PayGatewayClient {

    /**
     * 发起支付请求。
     * @param request 包含支付请求的详细信息
     * @return 返回支付响应信息
     */
    PaymentResponse initiatePayment(PaymentRequest request);

    /**
     * 发起退款请求。
     * @param request 包含退款请求的详细信息
     * @return 返回退款响应信息
     */
    RefundResponse refund(RefundRequest request);

    /**
     * 查询支付状态。
     * @param paymentId 支付订单的唯一标识符
     * @return 返回支付状态响应信息
     */
    PaymentStatusResponse queryStatus(String paymentId);

    /**
     * 处理支付回调通知。
     * @param parameters 包含回调通知的所有参数
     */
    Notification handleNotification(Map<String, String> parameters);
}
