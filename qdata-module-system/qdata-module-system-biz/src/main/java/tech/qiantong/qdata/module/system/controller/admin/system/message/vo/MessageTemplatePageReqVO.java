package tech.qiantong.qdata.module.system.controller.admin.system.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 消息模板 Request VO 对象 message_template
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "消息模板 Request VO")
@Data
public class MessageTemplatePageReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "消息标题", example = "")
    private String title;

    @Schema(description = "消息模板内容", example = "")
    private String content;

    @Schema(description = "消息类别", example = "")
    private Integer category;

    @Schema(description = "消息等级", example = "")
    private Integer msgLevel;

    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Schema(description = "删除标识")
    private Boolean delFlag;

}
