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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
