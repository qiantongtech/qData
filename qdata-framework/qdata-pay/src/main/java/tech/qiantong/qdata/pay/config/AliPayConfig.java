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
 */

package tech.qiantong.qdata.pay.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Data
@Component
@ConfigurationProperties(prefix = "payment.alipay")
public class AliPayConfig {
    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;
    private String returnUrl;
    private String gatewayHost;

    @PostConstruct
    public void init() {
        // 设置参数（全局只需设置一次）
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = this.gatewayHost;
        config.signType = "RSA2";
        config.appId = this.appId;
        config.merchantPrivateKey = this.appPrivateKey;
        config.alipayPublicKey = this.alipayPublicKey;
        config.notifyUrl = this.notifyUrl;
        Factory.setOptions(config);
        System.out.println("=======支付宝SDK初始化成功=======");
    }

}
