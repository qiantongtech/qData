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

//package tech.qiantong.qdata.module.system.controller.admin.example.pay;
//
//import tech.qiantong.qdata.pay.domain.*;
//import tech.qiantong.qdata.pay.service.PayGatewayClient;
//import tech.qiantong.qdata.pay.service.PaymentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
///**
// * 支付
// * @author qdata
// */
//@RestController
//@RequestMapping("/payment")
//public class PaymentController {
//
//    @Autowired
//    private PaymentService paymentService;
//
//    @Autowired
//    @Qualifier("alipayClientImpl") // 指定要注入的Bean名称
//    private PayGatewayClient alipayClient;
//
//    @Autowired
//    @Qualifier("weChatPayClientImpl") // 指定要注入的Bean名称
//    private PayGatewayClient wechatPayClient;
//
//    /**
//     * 发起支付请求的API接口。
//     * @param request 包含支付请求的详细信息
//     * @return 返回支付响应信息
//     */
//    @PostMapping("/pay")
//    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest request) {
//        PaymentResponse response = paymentService.pay(request);
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 发起退款请求的API接口。
//     * @param request 包含退款请求的详细信息
//     * @return 返回退款响应信息
//     */
//    @PostMapping("/refund")
//    public ResponseEntity<RefundResponse> refund(@RequestBody RefundRequest request) {
//        RefundResponse response = paymentService.refund(request);
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 查询支付状态的API接口。
//     * @param paymentId 支付订单的唯一标识符
//     * @return 返回支付状态响应信息
//     */
//    @GetMapping("/status/{paymentId}")
//    public ResponseEntity<PaymentStatusResponse> queryStatus(@PathVariable String paymentId) {
//        PaymentStatusResponse response = paymentService.queryStatus(paymentId);
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 处理支付宝支付回调通知
//     * @param parameters 包含回调通知的所有参数
//     * @return 返回处理结果
//     */
//    @RequestMapping("/alipay/notify")
//    public void handleAlipayNotification(@RequestParam Map<String, String> parameters) {
//        Notification notification = alipayClient.handleNotification(parameters);
//        System.out.println(notification);
//    }
//
//    /**
//     * 处理微信支付回调通知
//     * @param parameters 包含回调通知的所有参数
//     * @return 返回处理结果
//     */
//    @RequestMapping("/wechat/notify")
//    public void handleWechatNotification(@RequestParam Map<String, String> parameters) {
//        Notification notification = wechatPayClient.handleNotification(parameters);
//        System.out.println(notification);
//    }
//
//}
