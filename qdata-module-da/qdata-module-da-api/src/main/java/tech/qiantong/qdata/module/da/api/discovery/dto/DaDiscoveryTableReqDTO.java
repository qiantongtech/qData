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

package tech.qiantong.qdata.module.da.api.discovery.dto;

import lombok.Data;

/**
 * Data Discovery Table DTO DA_DISCOVERY_TABLE
 *
 * @author qdata
 * @date 2025-02-11
 */
@Data
public class DaDiscoveryTableReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Task ID */
    private Long taskId;

    /** Table Name */
    private String tableName;

    /** Table Description */
    private String tableComment;

    /** Data Count */
    private Long dataCount;

    /** Field Count */
    private Long fieldCount;

    /** Change Flag */
    private String changeFlag;

    /** Status */
    private String status;

    /** Ignore Flag */
    private String ignoreFlag;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
