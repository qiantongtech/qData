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

package tech.qiantong.qdata.module.att.controller.admin.tag.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 标签管理 创建/修改 Request VO ATT_TAG
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
