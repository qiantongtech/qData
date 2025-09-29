package tech.qiantong.qdata.module.system.controller.admin.system.message.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class MessageSend {

    @NotNull(message = "templateId null")
    private Long templateId;

    @NotNull(message = "receiverId null")
    private Long receiverId;

    @NotNull(message = "data null")
    private Map<String, Object> data;

}
