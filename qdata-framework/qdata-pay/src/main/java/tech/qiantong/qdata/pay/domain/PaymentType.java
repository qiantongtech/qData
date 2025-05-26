package tech.qiantong.qdata.pay.domain;

/**
 * 定义了系统中支持的支付方式。
 *
 * @author qdata
 */
public enum PaymentType {

    /**
     * 支付宝支付
     */
    ALIPAY,

    /**
     * 微信支付
     */
    WECHAT;

    /**
     * 根据输入的字符串值获取对应的支付类型。
     *
     * @param type 输入的字符串，如 "ALIPAY" 或 "WECHAT"
     * @return 返回对应的 PaymentType 枚举值，如果没有匹配则返回 null
     */
    public static PaymentType fromString(String type) {
        for (PaymentType paymentType : PaymentType.values()) {
            if (paymentType.name().equalsIgnoreCase(type)) {
                return paymentType;
            }
        }
        return null;
    }
}
