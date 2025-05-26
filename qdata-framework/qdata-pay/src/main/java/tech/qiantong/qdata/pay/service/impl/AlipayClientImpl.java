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
    /** 支付宝网关地址 */
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
     *          前端的使用方法
     *          if (response.status === 'ALIPAY_SUCCESS') {
     *             // 创建一个容器，用于放置支付宝的支付表单
     *             const formContainer = document.createElement('div');
     *             formContainer.innerHTML = response.paymentUrl;
     *             // 将表单添加到页面并提交
     *             document.body.appendChild(formContainer);
     *             formContainer.querySelector('form').submit(); // 自动提交表单
     *           }
     * @param payRequest 包含支付请求的详细信息
     * @return
     */
    @Override
    public PaymentResponse initiatePayment(PaymentRequest payRequest) {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, myAliPayConfig.getAppId(),
                myAliPayConfig.getAppPrivateKey(), FORMAT_JSON, CHARSET_UTF8, myAliPayConfig.getAlipayPublicKey(), SIGN_TYPE_RSA2);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(myAliPayConfig.getNotifyUrl());
        request.setReturnUrl(RETURN_URL);

        // 设置请求参数
        double amount = payRequest.getAmount() / 100.0;
        request.setBizContent("{\"out_trade_no\":\"" + payRequest.getOrderId() + "\","
                + "\"total_amount\":\"" + amount + "\","
                + "\"subject\":\"" + payRequest.getDescription() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return new PaymentResponse("ALIPAY_FAILURE", null, payRequest.getOrderId());
        }
        // 返回支付表单给前端
        return new PaymentResponse("ALIPAY_SUCCESS", form, payRequest.getOrderId());
    }

    @Override
    public RefundResponse refund(RefundRequest refunRequest) {
        // 初始化支付宝客户端
        AlipayClient alipayClient = new DefaultAlipayClient(
                GATEWAY_URL,
                myAliPayConfig.getAppId(),
                myAliPayConfig.getAppPrivateKey(),
                FORMAT_JSON,
                CHARSET_UTF8,
                myAliPayConfig.getAlipayPublicKey(),
                SIGN_TYPE_RSA2
        );

        // 创建退款请求对象
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

        // 构建退款请求的业务参数
        JSONObject bizContent = new JSONObject();
        // 订单号，对应支付宝交易号
        bizContent.put("trade_no", refunRequest.getOrderId());
        // 退款金额
        double amount = refunRequest.getAmount() / 100.0;
        bizContent.put("refund_amount", amount);
        // 退款请求号，可用于部分退款时的唯一标识
        bizContent.put("out_request_no", UUID.fastUUID().toString());

        // 可选参数：如果需要返回详细的退款信息，可以添加此部分
        JSONArray queryOptions = new JSONArray();
        queryOptions.add("refund_detail_item_list");
        bizContent.put("query_options", queryOptions);

        // 设置业务参数到请求对象中
        request.setBizContent(bizContent.toString());

        try {
            // 执行退款请求，调用支付宝API
            AlipayTradeRefundResponse response = alipayClient.execute(request);

            // 根据API返回结果判断退款是否成功
            if (response.isSuccess()) {
                System.out.println("退款成功");
                return new RefundResponse("ALIPAY_REFUND_SUCCESS", refunRequest.getOrderId());
            } else {
                System.out.println("退款失败");
                return new RefundResponse("ALIPAY_REFUND_FAILURE", refunRequest.getOrderId());
            }
        } catch (Exception e) {
            // 处理可能的异常情况
            e.printStackTrace();
            return new RefundResponse("ALIPAY_REFUND_ERROR", refunRequest.getOrderId());
        }
    }

    @Override
    public PaymentStatusResponse queryStatus(String paymentId) {
        // 调用支付宝API查询支付状态
        return new PaymentStatusResponse("ALIPAY_SUCCESS", paymentId);
    }

    @Override
    public Notification handleNotification(Map<String, String> parameters) {
        // 解析 fund_bill_list 字段，将其转换为 FundBill 对象的列表
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

    // 使用 Fastjson 解析 JSON 字符串为 FundBill 列表
    private List<AlipayNotification.FundBill> parseFundBillList(String fundBillListJson) {
        try {
            return JSON.parseObject(fundBillListJson, new TypeReference<List<AlipayNotification.FundBill>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
