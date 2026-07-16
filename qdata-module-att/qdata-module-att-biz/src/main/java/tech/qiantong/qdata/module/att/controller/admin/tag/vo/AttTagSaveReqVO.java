/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.att.controller.admin.tag.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * Tag Management Create/Update Request VO ATT_TAG
 *
 * @author qdata
 * @date 2025-07-11
 */
@Schema(description = "标签管理 Response VO")
@Data
public class AttTagSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "名称", example = "")
    @Size(max = 256, message = "名称长度不能超过256个字符")
    private String name;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "类目编码", example = "")
    @Size(max = 256, message = "类目编码长度不能超过256个字符")
    private String catCode;

    @Schema(description = "类目名称", example = "")
    @Size(max = 256, message = "类目编码长度不能超过256个字符")
    private String catName;

    @Schema(description = "资产数量", example = "")
    private Long aeestCount;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "扩展信息别名", example = "")
    @Size(max = 256, message = "扩展信息别名长度不能超过256个字符")
    private String alias;

    @Schema(description = "近义词", example = "")
    @Size(max = 256, message = "近义词长度不能超过256个字符")
    private String nearSynonyms;

    @Schema(description = "同义词", example = "")
    @Size(max = 256, message = "同义词长度不能超过256个字符")
    private String synonyms;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
