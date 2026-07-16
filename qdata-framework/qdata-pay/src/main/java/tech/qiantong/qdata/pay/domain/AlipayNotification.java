/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.pay.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Encapsulates the parameters of Alipay callback notification
 */
@Data
@AllArgsConstructor
public class AlipayNotification extends Notification{

    /** Transaction creation time */
    private String gmtCreate;

    /** Encoding format */
    private String charset;

    /** Transaction payment time */
    private String gmtPayment;

    /** Notification sending time */
    private String notifyTime;

    /** Product title */
    private String subject;

    /** Signature */
    private String sign;

    /** Buyer’s Alipay user number */
    private String buyerId;

    /** Invoicing amount */
    private String invoiceAmount;

    /** Interface version */
    private String version;

    /** Notification verification ID */
    private String notifyId;

    /** Payment channel information */
    private List<FundBill> fundBillList;

    /** Notification type */
    private String notifyType;

    /** Merchant order number */
    private String outTradeNo;

    /** Order amount */
    private String totalAmount;

    /** Transaction status */
    private String tradeStatus;

    /** Alipay transaction number */
    private String tradeNo;

    /** AppId of the authorizer */
    private String authAppId;

    /** Actual amount received */
    private String receiptAmount;

    /** Amount of Jifenbao */
    private String pointAmount;

    /** Payment amount */
    private String buyerPayAmount;

    /** The application ID assigned to the developer by Alipay */
    private String appId;

    /** Signature type */
    private String signType;

    /** Seller’s Alipay user number */
    private String sellerId;

    public static class FundBill {
        /** Payment amount */
        private String amount;

        /** Payment channel */
        private String fundChannel;

        // Constructors, Getters and Setters

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
