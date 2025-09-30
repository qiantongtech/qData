package tech.qiantong.qdata.module.system.controller.admin.system.message.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 消息 创建/修改 Request VO message
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "消息 Response VO")
@Data
public class MessageSaveReqVO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "发送人", example = "")
    private Long senderId;


    @Schema(description = "接收人", example = "")
    private Long receiverId;

    @Schema(description = "消息模块", example = "")
    @NotNull(message = "消息模块不能为空")
    private Integer module;


    @Schema(description = "实体类型", example = "")
    private Integer entityType;


    @Schema(description = "实体id", example = "")
    private Long entityId;

    @Schema(description = "消息链接", example = "")
    @Size(max = 256, message = "消息链接长度不能超过256个字符")
    private String entityUrl;

    private Integer delFlag;
    private String id;
    private Integer hasRead;

    @Schema(description = "创建者id", example = "")
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建者
     */
    @Schema(description = "创建者", example = "")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

}
