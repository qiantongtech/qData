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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
