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

package tech.qiantong.qdata.module.att.controller.admin.rule.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 清洗规则 Request VO 对象 ATT_CLEAN_RULE
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



    /** 类目编码 */
    private String catCode;

    @TableField(exist = false)
    private String catID;

    @TableField(exist = false)
    private String catName;



}
