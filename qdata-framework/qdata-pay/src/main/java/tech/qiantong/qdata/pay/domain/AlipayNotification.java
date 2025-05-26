package tech.qiantong.qdata.pay.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 封装支付宝回调通知的参数
 */
@Data
@AllArgsConstructor
public class AlipayNotification extends Notification{

    /** 交易创建时间 */
    private String gmtCreate;

    /** 编码格式 */
    private String charset;

    /** 交易付款时间 */
    private String gmtPayment;

    /** 通知的发送时间 */
    private String notifyTime;

    /** 商品标题 */
    private String subject;

    /** 签名 */
    private String sign;

    /** 买家支付宝用户号 */
    private String buyerId;

    /** 开票金额 */
    private String invoiceAmount;

    /** 接口版本 */
    private String version;

    /** 通知校验ID */
    private String notifyId;

    /** 支付渠道信息 */
    private List<FundBill> fundBillList;

    /** 通知类型 */
    private String notifyType;

    /** 商户订单号 */
    private String outTradeNo;

    /** 订单金额 */
    private String totalAmount;

    /** 交易状态 */
    private String tradeStatus;

    /** 支付宝交易号 */
    private String tradeNo;

    /** 授权方的AppId */
    private String authAppId;

    /** 实收金额 */
    private String receiptAmount;

    /** 集分宝金额 */
    private String pointAmount;

    /** 付款金额 */
    private String buyerPayAmount;

    /** 支付宝分配给开发者的应用ID */
    private String appId;

    /** 签名类型 */
    private String signType;

    /** 卖家支付宝用户号 */
    private String sellerId;

    public static class FundBill {
        /** 支付金额 */
        private String amount;

        /** 支付渠道 */
        private String fundChannel;

        // 构造器、Getters 和 Setters

        public FundBill(String amount, String fundChannel) {
            this.amount = amount;
            this.fundChannel = fundChannel;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getFundChannel() {
            return fundChannel;
        }

        public void setFundChannel(String fundChannel) {
            this.fundChannel = fundChannel;
        }
    }
}
