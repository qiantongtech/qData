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

package tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeIntervalDO;

/**
 * 脱敏规则 创建/修改 Request VO DG_DESENSITIZE_RULE
 *
 * @author qdata
 * @date 2026-04-10
 */
@Schema(description = "脱敏规则 Response VO")
@Data
public class DgDesensitizeRuleSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "分级名称", example = "")
    @Size(max = 256, message = "分级名称长度不能超过256个字符")
    private String name;

    @Schema(description = "数据分类ID", example = "")
    private Long dataCategoryId;

    @Schema(description = "应用场景;1：数据资产  2：数据查询  3：数据服务", example = "")
    @Size(max = 256, message = "应用场景;1：数据资产  2：数据查询  3：数据服务长度不能超过256个字符")
    private String applicationScene;

    @Schema(description = "脱敏方式;1：底层脱敏  2：展示脱敏", example = "")
    @Size(max = 256, message = "脱敏方式;1：底层脱敏  2：展示脱敏长度不能超过256个字符")
    private String maskType;

    @Schema(description = "替换规则", example = "")
    @Size(max = 256, message = "替换规则长度不能超过256个字符")
    private String replaceRule;

    @Schema(description = "替换内容", example = "")
    @Size(max = 256, message = "替换内容长度不能超过256个字符")
    private String replaceContent;

    @Schema(description = "脱敏区间", example = "")
    private List<DgDesensitizeIntervalDO> intervalList;

    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;
}
