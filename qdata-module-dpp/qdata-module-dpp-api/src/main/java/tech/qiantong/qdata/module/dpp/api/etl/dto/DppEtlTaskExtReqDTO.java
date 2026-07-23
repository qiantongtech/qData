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

package tech.qiantong.qdata.module.dpp.api.etl.dto;

import lombok.Data;

/**
 * Data Integration Task Extension DTO - DPP_ETL_TASK_EXT
 *
 * @author qdata
 * @date 2025-04-16
 */
@Data
public class DppEtlTaskExtReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Data Aggregation Task ID */
    private Long taskId;

    /** Data Aggregation Node ID */
    private Long etlNodeId;

    /** Data Aggregation Node Name */
    private String etlNodeName;

    /** Data Aggregation Node Code */
    private String etlNodeCode;

    /** Data Aggregation Node Version */
    private Long etlNodeVersion;

    /** Data Aggregation Node Relation ID */
    private Long etlRelationId;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
