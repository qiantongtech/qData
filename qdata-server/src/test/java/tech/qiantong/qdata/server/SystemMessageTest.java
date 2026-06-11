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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessageSaveReqVO;
import tech.qiantong.qdata.module.system.service.message.impl.MessageServiceImpl;

import java.util.HashMap;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = QDataApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SystemMessageTest {

    @Autowired
    private MessageServiceImpl iMessageService;
    /**
     *
     * 消息测试
     * Long templateId, MessageSaveReqVO messageSaveReqVO, Object entity
     */
    @Test
    public void testInternalMessageSend() {
        MessageSaveReqVO messageSaveReqVO = new MessageSaveReqVO();
        messageSaveReqVO.setSenderId(1L);
        messageSaveReqVO.setReceiverId(731L);
        Map<String, Object> map = new HashMap<>();
        map.put("test","àáâäǎæãåā");


        iMessageService.send(1L,messageSaveReqVO,map);




    }



}
