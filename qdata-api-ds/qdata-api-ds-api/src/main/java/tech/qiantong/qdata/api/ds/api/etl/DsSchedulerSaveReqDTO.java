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

package tech.qiantong.qdata.api.ds.api.etl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <P>
 * Description: Scheduler save request DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-21 10:11
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsSchedulerSaveReqDTO {

    /**
     * {
     * "startTime":"2025-02-21 00:00:00",//start time defaults to current time, format yyyy-MM-dd HH:mm:ss
     * "endTime":"2125-02-21 00:00:00",//end time defaults to 100 years from now, format yyyy-MM-dd HH:mm:ss
     * "crontab":"0 0 * * * ? *",//cron expression (required)
     * "timezoneId":"Asia/Shanghai"//timezone defaults to Asia/Shanghai
     * }
     */
    private String schedule;

    /**
     * Task code (required)
     */
    private String processDefinitionCode;

    /**
     * Failure strategy, defaults to CONTINUE
     */
    private String failureStrategy;

    /**
     * Default default
     */
    private String workerGroup;

    /**
     * Default default
     */
    private String tenantCode;

}
