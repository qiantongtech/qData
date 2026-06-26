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

package tech.qiantong.qdata.module.da.api.sensitiveLevel.dto;

import lombok.Data;

/**
 * 敏感等级 DTO 对象 DA_SENSITIVE_LEVEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DaSensitiveLevelRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 敏感级别 */
    private String sensitiveLevel;

    /** 敏感规则 */
    private String sensitiveRule;

    /** 起始字符位置 */
    private Long startCharLoc;

    /** 截止字符位置 */
    private Long endCharLoc;

    /** 遮盖字符 */
    private String maskCharacter;

    /** 上下线标识 */
    private String onlineFlag;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
