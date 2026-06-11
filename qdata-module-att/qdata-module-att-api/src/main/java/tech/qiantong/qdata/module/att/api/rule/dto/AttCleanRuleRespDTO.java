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

package tech.qiantong.qdata.module.att.api.rule.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 清洗规则 DTO 对象 ATT_CLEAN_RULE
 *
 * @author qdata
 * @date 2025-01-20
 */
@Data
public class AttCleanRuleRespDTO {

    private static final long serialVersionUID = 1L;

    /** 规则ID */
    private Long id;

    /** 规则名称 */
    private String name;

    /** 规则类型 */
    private String type;

    /** 规则级别 */
    private String level;

    /** 示例 */
    private String example;

    /** 规则描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    /** 类目编码 */
    private String catCode;

    @TableField(exist = false)
    private String catID;

    @TableField(exist = false)
    private String catName;

}
