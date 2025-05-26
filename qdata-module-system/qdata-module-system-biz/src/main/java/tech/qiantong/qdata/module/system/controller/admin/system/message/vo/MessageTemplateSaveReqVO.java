package tech.qiantong.qdata.module.system.controller.admin.system.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 消息模板 创建/修改 Request VO message_template
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "消息模板 Response VO")
@Data
public class MessageTemplateSaveReqVO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;


    @Schema(description = "消息标题", example = "")
    @NotBlank(message = "消息标题不能为空")
    @Size(max = 256, message = "消息标题长度不能超过256个字符")
    private String title;


    @Schema(description = "消息模板内容", example = "")
    @NotBlank(message = "消息模板内容不能为空")
    @Size(max = 256, message = "消息模板内容长度不能超过256个字符")
    private String content;


    @Schema(description = "消息类别", example = "")
    @NotNull(message = "消息类别不能为空")
    private Integer category;


    @Schema(description = "消息等级", example = "")
    @NotNull(message = "消息等级不能为空")
    private Integer msgLevel;

}
