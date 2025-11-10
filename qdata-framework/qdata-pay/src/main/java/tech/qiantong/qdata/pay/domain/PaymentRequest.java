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
