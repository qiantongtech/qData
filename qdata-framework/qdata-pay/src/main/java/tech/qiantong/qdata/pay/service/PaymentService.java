package tech.qiantong.qdata.pay.service;

import tech.qiantong.qdata.pay.domain.*;

/**
 * 定义了支付模块的核心业务操作。
 * 该接口包括发起支付、退款、查询支付状态以及处理支付回调的方法。
 * @author qdata
 */
public interface PaymentService {

    /**
     * 发起支付请求。
     * @param request 包含支付请求的详细信息
     * @return 返回支付响应信息
     */
    PaymentResponse pay(PaymentRequest request);

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
}
