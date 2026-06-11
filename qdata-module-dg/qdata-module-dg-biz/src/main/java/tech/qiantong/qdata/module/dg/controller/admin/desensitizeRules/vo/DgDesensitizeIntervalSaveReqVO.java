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

package tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 脱敏区间 创建/修改 Request VO DG_DESENSITIZE_INTERVAL
 *
 * @author qdata
 * @date 2026-04-10
 */
@Schema(description = "脱敏区间 Response VO")
@Data
public class DgDesensitizeIntervalSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "脱敏规则ID", example = "")
    private Long desensitizeRuleId;

    @Schema(description = "区间号", example = "")
    private Long intervalNo;

    @Schema(description = "起始值", example = "")
    private Long startNum;

    @Schema(description = "末尾值", example = "")
    private Long endNum;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;
}
