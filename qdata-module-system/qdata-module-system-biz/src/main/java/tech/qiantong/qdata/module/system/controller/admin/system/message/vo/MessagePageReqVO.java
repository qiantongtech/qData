package tech.qiantong.qdata.module.system.controller.admin.system.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 消息 Request VO 对象 message
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "消息 Request VO")
@Data
public class MessagePageReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "发送人", example = "")
    private Long senderId;

    @Schema(description = "接收人", example = "")
    private Long receiverId;

    @Schema(description = "消息标题", example = "")
    private String title;

    @Schema(description = "消息模板内容", example = "")
    private String content;

    @Schema(description = "消息类别", example = "")
    private Integer category;

    @Schema(description = "消息等级", example = "")
    private Integer msgLevel;

    @Schema(description = "消息模块", example = "")
    private Integer module;

    @Schema(description = "实体类型", example = "")
    private Integer entityType;

    @Schema(description = "实体id", example = "")
    private Long entityId;

    @Schema(description = "消息链接", example = "")
    private String entityUrl;

    @Schema(description = "是否已读", example = "")
    private Integer hasRead;

    @Schema(description = "是否撤回", example = "")
    private Integer hasRetraction;

    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Schema(description = "删除标识")
    private Integer delFlag;

    private Date startTime;
    private Date endTime;

}
