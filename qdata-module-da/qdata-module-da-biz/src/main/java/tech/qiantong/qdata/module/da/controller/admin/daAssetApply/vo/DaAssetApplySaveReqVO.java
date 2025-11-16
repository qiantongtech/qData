/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
