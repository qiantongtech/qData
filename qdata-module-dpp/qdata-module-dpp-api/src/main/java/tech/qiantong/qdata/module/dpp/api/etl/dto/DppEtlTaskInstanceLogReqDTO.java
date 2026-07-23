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

import java.util.Date;

/**
 * Data Integration Task Instance Log DTO - DPP_ETL_TASK_INSTANCE_LOG
 *
 * @author qdata
 * @date 2025-08-05
 */
@Data
public class DppEtlTaskInstanceLogReqDTO {

    private static final long serialVersionUID = 1L;

    /** Task Instance ID */
    private Long taskInstanceId;

    /** Time */
    private Date tm;

    /** Task Type */
    private String taskType;

    /** Task ID */
    private Long taskId;

    /** Task Code */
    private String taskCode;

    /** Log Content */
    private String logContent;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
