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

package tech.qiantong.qdata.module.da.api.assetColumn.dto;

import lombok.Data;

/**
 * 数据资产字段 DTO 对象 DA_ASSET_COLUMN
 *
 * @author lhs
 * @date 2025-01-21
 */
@Data
public class DaAssetColumnRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产id */
    private String assetId;

    /** 字段名称/英文名称 */
    private String columnName;

    /** 字段注释/中文名称 */
    private String columnComment;

    /** 数据类型 */
    private String columnType;

    /** 长度 */
    private Long columnLength;

    /** 小数位 */
    private Long columnScale;

    /** 是否必填 */
    private String nullableFlag;

    /** 是否主键 */
    private String pkFlag;

    /** 默认值 */
    private String defaultValue;

    /** 是否代码 */
    private String dataElemCodeFlag;

    /** 代码id */
    private String dataElemCodeId;

    /** 敏感等级id */
    private String sensitiveLevelId;

    /** 关联数据元 */
    private String relDataElmeFlag;

    /** 关联清洗规则 */
    private String relCleanFlag;

    /** 关联稽查规则 */
    private String relAuditFlag;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
