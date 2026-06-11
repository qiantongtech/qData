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
 */

package tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 标签与资产关联关系 创建/修改 Request VO ATT_TAG_ASSET_REL
 *
 * @author qdata
 * @date 2025-07-11
 */
@Schema(description = "标签与资产关联关系 Response VO")
@Data
public class AttTagAssetRelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "标签管理id", example = "")
    @Size(max = 256, message = "标签管理id长度不能超过256个字符")
    private String tagId;
    private List<String> tagIds;

    @Schema(description = "资产id", example = "")
    @Size(max = 256, message = "资产id长度不能超过256个字符")
    private String assetId;


    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
