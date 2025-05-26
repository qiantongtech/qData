package tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据资产申请 创建/修改 Request VO DA_ASSET_APPLY
 *
 * @author shu
 * @date 2025-03-19
 */
@Schema(description = "数据资产申请 Response VO")
@Data
public class DaAssetApplySaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    @Size(max = 256, message = "项目编码长度不能超过256个字符")
    private String projectCode;

    @Schema(description = "是申请过来的资产还是项目自己生成的资产0：申请，1：自创", example = "")
    private String sourceType;

    @Schema(description = "申请理由", example = "")
    @Size(max = 256, message = "申请理由长度不能超过256个字符")
    private String applyReason;

    @Schema(description = "审批理由", example = "")
    @Size(max = 256, message = "审批理由长度不能超过256个字符")
    private String approvalReason;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    @Schema(description = "资产名称", example = "")
    private String assetName;

    @Schema(description = "资产英文名称", example = "")
    private String assetTableName;

    @Schema(description = "资产类目", example = "")
    private String catAssetName;

    @Schema(description = "资产类目编码", example = "")
    private String catAssetCode;

    @Schema(description = "项目名称", example = "")
    private String projectName;

    @Schema(description = "主题名称", example = "")
    private String themeName;


}
