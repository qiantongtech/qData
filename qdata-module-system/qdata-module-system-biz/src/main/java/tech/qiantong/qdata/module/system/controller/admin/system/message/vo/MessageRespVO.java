package tech.qiantong.qdata.module.system.controller.admin.system.message.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.util.Date;

/**
 * 消息 Response VO 对象 message
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "消息 Response VO")
@Data
public class MessageRespVO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Excel(name = "发送人")
    @Schema(description = "发送人", example = "")
    private Long senderId;

    @Excel(name = "接收人")
    @Schema(description = "接收人", example = "")
    private Long receiverId;

    @Excel(name = "消息标题")
    @Schema(description = "消息标题", example = "")
    private String title;

    @Excel(name = "消息模板内容")
    @Schema(description = "消息模板内容", example = "")
    private String content;

    @Excel(name = "消息类别")
    @Schema(description = "消息类别", example = "")
    private Integer category;

    @Excel(name = "消息等级")
    @Schema(description = "消息等级", example = "")
    private Integer msgLevel;

    @Excel(name = "消息模块")
    @Schema(description = "消息模块", example = "")
    private Integer module;

    @Excel(name = "实体类型")
    @Schema(description = "实体类型", example = "")
    private Integer entityType;

    @Excel(name = "实体id")
    @Schema(description = "实体id", example = "")
    private Long entityId;

    @Excel(name = "消息链接")
    @Schema(description = "消息链接", example = "")
    private String entityUrl;

    @Excel(name = "是否已读")
    @Schema(description = "是否已读", example = "")
    private Integer hasRead;

    @Excel(name = "是否撤回")
    @Schema(description = "是否撤回", example = "")
    private Integer hasRetraction;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Schema(description = "删除标识")
    private Integer delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
