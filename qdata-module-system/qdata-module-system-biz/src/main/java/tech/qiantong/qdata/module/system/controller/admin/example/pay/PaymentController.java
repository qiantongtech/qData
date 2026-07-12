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
// * Payment
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
//    @Qualifier("alipayClientImpl") // Specify the bean name to inject
//    private PayGatewayClient alipayClient;
//
//    @Autowired
//    @Qualifier("weChatPayClientImpl") // Specify the bean name to inject
//    private PayGatewayClient wechatPayClient;
//
//    /**
//     * API endpoint to initiate a payment request.
//     * @param request Payment request details
//     * @return Payment response information
//     */
//    @PostMapping("/pay")
//    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest request) {
//        PaymentResponse response = paymentService.pay(request);
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * API endpoint to initiate a refund request.
//     * @param request Refund request details
//     * @return Refund response information
//     */
//    @PostMapping("/refund")
//    public ResponseEntity<RefundResponse> refund(@RequestBody RefundRequest request) {
//        RefundResponse response = paymentService.refund(request);
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * API endpoint to query payment status.
//     * @param paymentId Unique identifier of the payment order
//     * @return Payment status response information
//     */
//    @GetMapping("/status/{paymentId}")
//    public ResponseEntity<PaymentStatusResponse> queryStatus(@PathVariable String paymentId) {
//        PaymentStatusResponse response = paymentService.queryStatus(paymentId);
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * Handle Alipay callback notification.
//     * @param parameters All parameters from the callback notification
//     * @return Processing result
//     */
//    @RequestMapping("/alipay/notify")
//    public void handleAlipayNotification(@RequestParam Map<String, String> parameters) {
//        Notification notification = alipayClient.handleNotification(parameters);
//        System.out.println(notification);
//    }
//
//    /**
//     * Handle WeChat Pay callback notification.
//     * @param parameters All parameters from the callback notification
//     * @return Processing result
//     */
//    @RequestMapping("/wechat/notify")
//    public void handleWechatNotification(@RequestParam Map<String, String> parameters) {
//        Notification notification = wechatPayClient.handleNotification(parameters);
//        System.out.println(notification);
//    }
//
//}
