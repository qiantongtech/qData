package tech.qiantong.qdata.module.att.controller.admin.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 项目 创建/修改 Request VO ATT_PROJECT
 *
 * @author shu
 * @date 2025-01-20
 */
@Schema(description = "项目 Response VO")
@Data
public class AttProjectSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "项目名称", example = "")
    @Size(max = 256, message = "项目名称长度不能超过256个字符")
    private String name;

    @Schema(description = "项目编码", example = "")
    private String code;

    @Schema(description = "项目管理员id", example = "")
    private Long managerId;

    @Schema(description = "历史项目管理员id", example = "")
    private Long managerHistoryId;

    @Schema(description = "有效状态", example = "")
    private Boolean validFlag;

    @Schema(description = "项目描述", example = "")
    @Size(max = 256, message = "项目描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
