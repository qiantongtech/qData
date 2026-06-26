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

package tech.qiantong.qdata.module.dg.api.dataCategory.dto;

import lombok.*;

/**
 * 数据分类 DTO 对象 DG_DATA_CATEGORY
 *
 * @author qdata
 * @date 2026-04-07
 */
@Data
public class DgDataCategoryReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 类目id */
    private Long catId;

    /** 类目编码 */
    private String catCode;

    /** 分类名称 */
    private String name;

    /** 分类名称缩写名 */
    private String shortName;

    /** 数据分级 */
    private Long dataLevelId;

    /** 任务优先级;HIGHEST,HIGH,MEDIUM,LOW,LOWEST */
    private String priority;

    /** 描述 */
    private String description;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    private Boolean delFlag;


}
