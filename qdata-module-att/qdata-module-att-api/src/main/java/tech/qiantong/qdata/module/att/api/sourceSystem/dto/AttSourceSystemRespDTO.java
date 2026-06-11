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

package tech.qiantong.qdata.module.att.api.sourceSystem.dto;

import lombok.Data;

/**
 * 来源系统 DTO 对象 ATT_SOURCE_SYSTEM
 *
 * @author qdata
 * @date 2026-04-03
 */
@Data
public class AttSourceSystemRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 系统名称 */
    private String name;

    /** 系统类型 */
    private String type;

    /** 排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 负责人 */
    private String responsiblePerson;

    /** 对接人 */
    private String contactPerson;

    /** 删除标志;1：已删除，0：未删除 */
    private Boolean delFlag;


}
