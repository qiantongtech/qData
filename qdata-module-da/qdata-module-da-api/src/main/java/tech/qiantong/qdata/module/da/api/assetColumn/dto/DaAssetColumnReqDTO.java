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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
public class DaAssetColumnReqDTO {

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
