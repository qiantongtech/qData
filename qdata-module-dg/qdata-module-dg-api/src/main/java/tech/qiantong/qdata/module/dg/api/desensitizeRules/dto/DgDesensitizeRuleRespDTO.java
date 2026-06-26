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

package tech.qiantong.qdata.module.dg.api.desensitizeRules.dto;

import lombok.*;

/**
 * 脱敏规则 DTO 对象 DG_DESENSITIZE_RULE
 *
 * @author qdata
 * @date 2026-04-10
 */
@Data
public class DgDesensitizeRuleRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 分级名称 */
    private String name;

    /** 数据分类ID */
    private Long dataCategoryId;

    /** 应用场景;1：数据资产  2：数据查询  3：数据服务 */
    private String applicationScene;

    /** 脱敏方式;1：底层脱敏  2：展示脱敏 */
    private String maskType;

    /** 替换规则 */
    private String replaceRule;

    /** 替换内容 */
    private String replaceContent;

    /** 排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    private Boolean delFlag;


}
