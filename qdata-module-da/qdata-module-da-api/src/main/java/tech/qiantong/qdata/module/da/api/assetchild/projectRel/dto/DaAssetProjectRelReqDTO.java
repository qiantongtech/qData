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

package tech.qiantong.qdata.module.da.api.assetchild.projectRel.dto;

import lombok.Data;

/**
 * Data Asset-Project Relation DTO DA_ASSET_PROJECT_REL
 *
 * @author qdata
 * @date 2025-04-18
 */
@Data
public class DaAssetProjectRelReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Asset ID */
    private Long assetId;

    /** Project ID */
    private Long projectId;

    /** Project Code */
    private String projectCode;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
