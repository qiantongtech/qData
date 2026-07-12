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

package tech.qiantong.qdata.module.att.controller.admin.rule.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * Cleaning Rule Request VO ATT_CLEAN_RULE
 *
 * @author qdata
 * @date 2025-01-20
 */
@Schema(description = "清洗规则 Request VO")
@Data
public class AttCleanRulePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "规则名称", example = "")
    private String name;

    @Schema(description = "规则编码", example = "")
    private String code;

    @Schema(description = "规则类型", example = "")
    private String type;
    @Schema(description = "规则级别", example = "")
    private String level;
    @Schema(description = "策略标识", example = "")
    private String strategyKey;
    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;



    /** Category code */
    private String catCode;

    @TableField(exist = false)
    private String catID;

    @TableField(exist = false)
    private String catName;



}
