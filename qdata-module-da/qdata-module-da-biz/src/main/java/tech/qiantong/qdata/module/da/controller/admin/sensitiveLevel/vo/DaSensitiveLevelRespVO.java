package tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 敏感等级 Response VO 对象 DA_SENSITIVE_LEVEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "敏感等级 Response VO")
@Data
public class DaSensitiveLevelRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "敏感级别")
    @Schema(description = "敏感级别", example = "")
    private String sensitiveLevel;

    @Excel(name = "敏感规则")
    @Schema(description = "敏感规则", example = "")
    private String sensitiveRule;

    @Excel(name = "起始字符位置")
    @Schema(description = "起始字符位置", example = "")
    private Long startCharLoc;

    @Excel(name = "截止字符位置")
    @Schema(description = "截止字符位置", example = "")
    private Long endCharLoc;

    @Excel(name = "遮盖字符")
    @Schema(description = "遮盖字符", example = "")
    private String maskCharacter;

    @Excel(name = "上下线标识")
    @Schema(description = "上下线标识", example = "")
    private String onlineFlag;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
