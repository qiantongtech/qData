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
