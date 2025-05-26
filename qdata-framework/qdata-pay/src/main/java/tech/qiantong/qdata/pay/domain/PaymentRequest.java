package tech.qiantong.qdata.pay.domain;

import lombok.Data;

import java.util.Map;

/**
 * 表示发起支付请求时的请求数据。
 * 该类包含支付网关处理支付所需的所有必要信息。
 */
@Data
public class PaymentRequest {

    /**
     * 商户系统中的订单唯一标识符。
     * 用于将支付交易与商户订单关联。
     */
    private String orderId;

    /**
     * 支付的总金额，以货币的最小单位表示（例如人民币的分）。
     * 使用整数表示，以避免浮点数精度问题。
     */
    private long amount;

    /**
     * 用户选择的支付方式，例如支付宝（ALIPAY）或微信支付（WECHAT）。
     * 决定使用哪个支付网关处理交易。
     */
    private PaymentType paymentType;

    /**
     * 购买的商品或服务的简要描述。
     * 通常会显示在支付页面或凭证中，便于用户识别支付内容。
     */
    private String description;

    /**
     * 用户在商户系统中的唯一标识符。
     * 用于将支付记录与用户进行关联。
     */
    private String userId;

    /**
     * 发起支付请求的客户端IP地址。
     * 用于安全校验和防欺诈分析。
     */
    private String clientIp;

    /**
     * 支付成功后的异步回调通知URL。
     * 支付完成后通过此URL通知商户系统支付结果。
     */
    private String notifyUrl;

    /**
     * 支付成功后用户跳转的页面URL。
     * 支付成功后，支付网关会引导用户跳转至此URL。
     */
    private String returnUrl;

    /**
     * 扩展参数，允许传递额外的自定义业务信息。
     * 可用于在支付过程中传递特殊的业务需求。
     */
    private Map<String, String> extraParams;

}
