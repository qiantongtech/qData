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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <P>
 * Handle execution logging.
 * </p>
 *
 * @author: FXB
 * @create: 2025-07-01 13:49
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DppEtlTaskInstanceLogStatusRespDTO {

    /**
     * Implementation details.
     * Handle execution logging.
     */
    private String status;

    /**
     * Implementation details.
     */
    private String log;

    /**
     * Handle node-related data and operations.
     */
    private List<DppEtlNodeInstanceRespDTO> nodeInstanceList;

    /**
     * DataX input record count.
     */
    private Long inputRecords;

    /**
     * DataX output record count.
     */
    private Long outputRecords;

    /**
     * DataX node name list.
     */
    private List<String> nodeNames;

    /**
     * Number of successful DataX nodes.
     */
    private Integer successCount;

    /**
     * Number of failed DataX nodes.
     */
    private Integer failedCount;

    /**
     * Number of stopped DataX nodes.
     */
    private Integer stoppedCount;
}
