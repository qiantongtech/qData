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
 * Purpose: Instance log status response data
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
     * Status; 0: Submitted Successfully 1: Running 2: Preparing Pause 3: Paused 4: Preparing Stop 5: Stopped 6: Failed 7: Success 12: Delayed Execution 14: Serial Waiting 15: Preparing Lock 16: Locked
     * 5: Stopped 6: Failed 7: Success stop log polling
     */
    private String status;

    /**
     * Log Status 1: In Progress 2: Completed
     */
    private String log;

    /**
     * Node Instance List
     */
    private List<DppEtlNodeInstanceRespDTO> nodeInstanceList;

    /**
     * DataX输入数据量
     */
    private Long inputRecords;

    /**
     * DataX输出数据量
     */
    private Long outputRecords;

    /**
     * DataX节点名称列表
     */
    private List<String> nodeNames;

    /**
     * DataX成功节点数量
     */
    private Integer successCount;

    /**
     * DataX失败节点数量
     */
    private Integer failedCount;

    /**
     * DataX停止节点数量
     */
    private Integer stoppedCount;
}
