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
 * Desensitization Interval DTO Object DG_DESENSITIZE_INTERVAL
 *
 * @author qdata
 * @date 2026-04-10
 */
@Data
public class DgDesensitizeIntervalRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 脱敏规则ID */
    private Long desensitizeRuleId;

    /** 区间号 */
    private Long intervalNo;

    /** 起始值 */
    private Long startNum;

    /** 末尾值 */
    private Long endNum;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    private Boolean delFlag;


}
