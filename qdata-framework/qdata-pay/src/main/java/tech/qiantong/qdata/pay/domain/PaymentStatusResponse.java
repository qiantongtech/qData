package tech.qiantong.qdata.pay.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 类表示支付状态查询的响应数据。
 * 该类包含支付网关返回的订单支付状态信息。
 *
 * @author qdata
 */
@Data
@AllArgsConstructor
public class PaymentStatusResponse {

    /**
     * 支付状态，表示订单的当前支付状态。
     * 例如，可以使用“SUCCESS”、“PENDING”、“FAILED”等状态来表示。
     */
    private String status;

    /**
     * 商户系统中的订单唯一标识符。
     * 用于标识该支付状态响应针对的订单。
     */
    private String orderId;
}
