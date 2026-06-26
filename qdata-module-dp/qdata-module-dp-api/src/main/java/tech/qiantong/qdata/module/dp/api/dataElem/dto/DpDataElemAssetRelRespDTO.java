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

package tech.qiantong.qdata.module.dp.api.dataElem.dto;

import lombok.Data;

/**
 * 数据元数据资产关联信息 DTO 对象 DP_DATA_ELEM_ASSET_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpDataElemAssetRelRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 数据元类型 */
    private String dataElemType;

    /** 数据元id */
    private String dataElemId;

    /** 资产id(数据表id) */
    private String assetId;

    /** 数据表 */
    private String tableName;

    /** 关联字段id */
    private String columnId;

    /** 关联字段 */
    private String columnName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
