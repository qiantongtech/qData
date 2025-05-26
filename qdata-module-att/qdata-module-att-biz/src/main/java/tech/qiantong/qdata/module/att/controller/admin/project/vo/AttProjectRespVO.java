package tech.qiantong.qdata.module.att.controller.admin.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目 Response VO 对象 ATT_PROJECT
 *
 * @author shu
 * @date 2025-01-20
 */
@Schema(description = "项目 Response VO")
@Data
public class AttProjectRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "ID")
    @Schema(description = "ID", example = "")
    private Long id;

    @Excel(name = "项目名称")
    @Schema(description = "项目名称", example = "")
    private String name;

    @Excel(name = "项目编码")
    @Schema(description = "项目编码", example = "")
    private String code;

    @Excel(name = "项目管理员id")
    @Schema(description = "项目管理员id", example = "")
    private Long managerId;
    @Excel(name = "项目管理员")
    @Schema(description = "项目管理员", example = "")
    private String nickName;

    @Excel(name = "项目管理员电话")
    @Schema(description = "项目管理员电话", example = "")
    private String managerPhone;

    @Excel(name = "项目描述")
    @Schema(description = "项目描述", example = "")
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
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
