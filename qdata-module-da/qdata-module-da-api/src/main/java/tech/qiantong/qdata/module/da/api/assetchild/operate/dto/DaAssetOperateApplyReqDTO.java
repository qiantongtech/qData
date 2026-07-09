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

package tech.qiantong.qdata.module.da.api.assetchild.operate.dto;

import lombok.Data;

import java.util.Date;

/**
 * Data Asset Operation Apply DTO DA_ASSET_OPERATE_APPLY
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
public class DaAssetOperateApplyReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Asset ID */
    private Long assetId;

    /** Datasource ID */
    private Long datasourceId;

    /** Table Name */
    private String tableName;

    /** Table Comment/Table Description */
    private String tableComment;

    /** Operation Type */
    private String operateType;

    /** Operation JSON Data */
    private String operateJson;

    /** Operation Time */
    private Date operateTime;

    /** Execute Flag */
    private String executeFlag;

    /** Execute Time */
    private Date executeTime;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
