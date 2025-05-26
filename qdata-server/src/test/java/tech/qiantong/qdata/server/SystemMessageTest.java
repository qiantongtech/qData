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
