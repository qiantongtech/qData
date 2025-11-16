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

package tech.qiantong.qdata.server;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.qiantong.qdata.common.httpClient.HeaderEntity;
import tech.qiantong.qdata.common.httpClient.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * HttpUtils 测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = QDataApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpUtilsTest {

    @Test
    public void testSendGetComm() {
        String url = "https://www.baidu.com";
        List<HeaderEntity> headers = new ArrayList<>();
        try {
            HttpUtils.ResponseObject responseObject = HttpUtils.sendGet(url, headers);
            System.out.println(responseObject.getBody().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    public void testSendGetUtils() {
        String url = "https://www.baidu.com";
        List<HeaderEntity> headers = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        try {
            String s = tech.qiantong.qdata.common.utils.http.HttpUtils.sendGet(url, "{}");
            System.out.println(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


}
