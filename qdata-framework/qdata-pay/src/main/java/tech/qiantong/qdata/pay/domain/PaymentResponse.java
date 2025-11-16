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

/**
 * 类表示支付请求的响应数据。
 * 该类包含支付网关返回的结果信息。
 *
 * @author qdata
 */
@Data
@AllArgsConstructor
public class PaymentResponse {

    /**
     * 支付状态，表示支付是否成功或失败。
     * 例如，“SUCCESS”表示成功，“FAILED”表示失败。
     */
    private String status;

    /**
     * 支付跳转链接，用户可以通过此链接完成支付。
     * 对于需要用户跳转的支付方式（如PC网页支付），此链接尤为重要。
     */
    private String paymentUrl;

    /**
     * 商户系统中的订单唯一标识符。
     * 用于标识该支付响应针对的订单。
     */
    private String orderId;

}
