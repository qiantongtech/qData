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
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 标签与资产关联关系 Request VO 对象 ATT_TAG_ASSET_REL
 *
 * @author qdata
 * @date 2025-07-11
 */
@Schema(description = "标签与资产关联关系 Request VO")
@Data
public class AttTagAssetRelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "标签管理id", example = "")
    private String tagId;

    @Schema(description = "资产id", example = "")
    private String assetId;




}
