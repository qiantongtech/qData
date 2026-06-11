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

package tech.qiantong.qdata.module.dg.api.dataLevel.dto;

import lombok.*;

/**
 * 数据分级 DTO 对象 DG_DATA_LEVEL
 *
 * @author qdata
 * @date 2026-04-03
 */
@Data
public class DgDataLevelRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 分级名称 */
    private String name;

    /** 分级缩写名 */
    private String shortname;

    /** 敏感等级 */
    private Long sensitiveLevel;

    /** 排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    private Boolean delFlag;


}
