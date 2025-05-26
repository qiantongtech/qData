package tech.qiantong.qdata.module.system.api.message.dto;

import lombok.Data;

/**
 * 消息 DTO 对象 message
 *
 * @author qdata
 * @date 2024-10-31
 */
@Data
public class MessageReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 发送人 */
    private Long senderId;

    /** 接收人 */
    private Long receiverId;

    /** 消息标题 */
    private String title;

    /** 消息模板内容 */
    private String content;

    /** 消息类别 */
    private Integer category;

    /** 消息等级 */
    private Integer msgLevel;

    /** 消息模块 */
    private Integer module;

    /** 实体类型 */
    private Integer entityType;

    /** 实体id */
    private Long entityId;

    /** 消息链接 */
    private String entityUrl;

    /** 是否已读 */
    private Integer hasRead;

    /** 是否撤回 */
    private Integer hasRetraction;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
