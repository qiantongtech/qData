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

package tech.qiantong.qdata.pay.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.pay.config.AliPayConfig;
import tech.qiantong.qdata.pay.domain.*;
import tech.qiantong.qdata.pay.service.PayGatewayClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author qdata
 */
@Service
public class AlipayClientImpl implements PayGatewayClient {
    /** Alipay gateway address */
    @Value("${payment.alipay.gatewayUrl}")
    private  String GATEWAY_URL;
    @Value("${payment.alipay.returnUrl}")
    private  String RETURN_URL;
    private static final String FORMAT_JSON = "JSON";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String SIGN_TYPE_RSA2 = "RSA2";

    @Autowired
    private AliPayConfig myAliPayConfig;

    /**
     * How to use the front end
     *          if (response.status === 'ALIPAY_SUCCESS') {
     * //Create a container to place Alipay's payment form
     *             const formContainer = document.createElement('div');
     *             formContainer.innerHTML = response.paymentUrl;
     * //Add the form to the page and submit it
     *             document.body.appendChild(formContainer);
     * formContainer.querySelector('form').submit(); // Automatically submit the form
     *           }
     * @param payRequest contains the details of the payment request
     * @return
     */
    @Override
    public PaymentResponse initiatePayment(PaymentRequest payRequest) {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, myAliPayConfig.getAppId(),
                myAliPayConfig.getAppPrivateKey(), FORMAT_JSON, CHARSET_UTF8, myAliPayConfig.getAlipayPublicKey(), SIGN_TYPE_RSA2);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(myAliPayConfig.getNotifyUrl());
        request.setReturnUrl(RETURN_URL);

        // Set request parameters
        double amount = payRequest.getAmount() / 100.0;
        request.setBizContent("{\"out_trade_no\":\"" + payRequest.getOrderId() + "\","
                + "\"total_amount\":\"" + amount + "\","
                + "\"subject\":\"" + payRequest.getDescription() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String form = "";
        try {
            // Call SDK to generate form
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return new PaymentResponse("ALIPAY_FAILURE", null, payRequest.getOrderId());
        }
        // Return the payment form to the front end
        return new PaymentResponse("ALIPAY_SUCCESS", form, payRequest.getOrderId());
    }

    @Override
    public RefundResponse refund(RefundRequest refunRequest) {
        // Initialize Alipay client
        AlipayClient alipayClient = new DefaultAlipayClient(
                GATEWAY_URL,
                myAliPayConfig.getAppId(),
                myAliPayConfig.getAppPrivateKey(),
                FORMAT_JSON,
                CHARSET_UTF8,
                myAliPayConfig.getAlipayPublicKey(),
                SIGN_TYPE_RSA2
        );

        // Create a refund request object
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

        // Construct business parameters for refund request
        JSONObject bizContent = new JSONObject();
        // Order number, corresponding to Alipay transaction number
        bizContent.put("trade_no", refunRequest.getOrderId());
        // Refund amount
        double amount = refunRequest.getAmount() / 100.0;
        bizContent.put("refund_amount", amount);
        // Refund request number, which can be used to uniquely identify partial refunds
        bizContent.put("out_request_no", UUID.fastUUID().toString());

        // Optional parameters: If you need to return detailed refund information, you can add this section
        JSONArray queryOptions = new JSONArray();
        queryOptions.add("refund_detail_item_list");
        bizContent.put("query_options", queryOptions);

        // Set business parameters to the request object
        request.setBizContent(bizContent.toString());

        try {
            // Execute refund request and call Alipay API
            AlipayTradeRefundResponse response = alipayClient.execute(request);

            // Determine whether the refund is successful based on the API return result
            if (response.isSuccess()) {
                System.out.println("Refund succeeded");
                return new RefundResponse("ALIPAY_REFUND_SUCCESS", refunRequest.getOrderId());
            } else {
                System.out.println("Refund failed");
                return new RefundResponse("ALIPAY_REFUND_FAILURE", refunRequest.getOrderId());
            }
        } catch (Exception e) {
            // Handle possible exceptions
            e.printStackTrace();
            return new RefundResponse("ALIPAY_REFUND_ERROR", refunRequest.getOrderId());
        }
    }

    @Override
    public PaymentStatusResponse queryStatus(String paymentId) {
        // Call Alipay API to check payment status
        return new PaymentStatusResponse("ALIPAY_SUCCESS", paymentId);
    }

    @Override
    public Notification handleNotification(Map<String, String> parameters) {
        // Parse the fund_bill_list field, converting it into a list of FundBill objects
        String fundBillListJson = parameters.get("fund_bill_list");
        List<AlipayNotification.FundBill> fundBillList = parseFundBillList(fundBillListJson);
        return new AlipayNotification(
                parameters.get("gmt_create"),
                parameters.get("charset"),
                parameters.get("gmt_payment"),
                parameters.get("notify_time"),
                parameters.get("subject"),
                parameters.get("sign"),
                parameters.get("buyer_id"),
                parameters.get("invoice_amount"),
                parameters.get("version"),
                parameters.get("notify_id"),
                fundBillList,
                parameters.get("notify_type"),
                parameters.get("out_trade_no"),
                parameters.get("total_amount"),
                parameters.get("trade_status"),
                parameters.get("trade_no"),
                parameters.get("auth_app_id"),
                parameters.get("receipt_amount"),
                parameters.get("point_amount"),
                parameters.get("buyer_pay_amount"),
                parameters.get("app_id"),
                parameters.get("sign_type"),
                parameters.get("seller_id")
        );
    }

    // Use Fastjson to parse JSON string into FundBill list
    private List<AlipayNotification.FundBill> parseFundBillList(String fundBillListJson) {
        try {
            return JSON.parseObject(fundBillListJson, new TypeReference<List<AlipayNotification.FundBill>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
