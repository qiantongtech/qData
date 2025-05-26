package tech.qiantong.qdata.pay.domain;

import lombok.Data;

/**
 * 类表示退款请求的数据。
 * 该类包含发起退款操作所需的所有信息。
 *
 * @author qdata
 */
@Data
public class RefundRequest {

    /**
     * 商户系统中的订单唯一标识符。
     * 用于标识需要退款的订单。
     */
    private String orderId;

    /**
     * 退款金额，以货币的最小单位表示（例如人民币的分）。
     * 通常退款金额不应超过原支付金额。
     */
    private long amount;

    /**
     * 支付方式，例如支付宝（ALIPAY）或微信支付（WECHAT）。
     * 用于指定通过哪个支付网关处理退款。
     */
    private PaymentType paymentType;
}
