package tech.qiantong.qdata.pay.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 类表示支付请求的响应数据。
 * 该类包含支付网关返回的结果信息。
 *
 * @author qdata
 */
@Data
@AllArgsConstructor
public class PaymentResponse {

    /**
     * 支付状态，表示支付是否成功或失败。
     * 例如，“SUCCESS”表示成功，“FAILED”表示失败。
     */
    private String status;

    /**
     * 支付跳转链接，用户可以通过此链接完成支付。
     * 对于需要用户跳转的支付方式（如PC网页支付），此链接尤为重要。
     */
    private String paymentUrl;

    /**
     * 商户系统中的订单唯一标识符。
     * 用于标识该支付响应针对的订单。
     */
    private String orderId;

}
