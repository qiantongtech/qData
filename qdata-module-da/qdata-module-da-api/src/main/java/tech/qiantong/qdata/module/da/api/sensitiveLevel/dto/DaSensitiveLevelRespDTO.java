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
