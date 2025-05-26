package tech.qiantong.qdata.module.system.api.message.dto;

import lombok.Data;

/**
 * 消息模板 DTO 对象 message_template
 *
 * @author qdata
 * @date 2024-10-31
 */
@Data
public class MessageTemplateReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 消息标题 */
    private String title;

    /** 消息模板内容 */
    private String content;

    /** 消息类别 */
    private Integer category;

    /** 消息等级 */
    private Integer msgLevel;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
