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
 * 用途:调度器新增请求参数DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-21 10:11
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsSchedulerUpdateReqDTO {

    /**
     * 调度id（对应是咱们调度表中的ds_id）
     */
    private Long id;

    /**
     * {
     * "startTime":"2025-02-21 00:00:00",//开始时间直接默认当前时间 格式yyyy-MM-dd HH:mm:ss
     * "endTime":"2125-02-21 00:00:00",//结束时间直接默认当前时间的100年后 格式yyyy-MM-dd HH:mm:ss
     * "crontab":"0 0 * * * ? *",//cron表达式（必填）
     * "timezoneId":"Asia/Shanghai"//时区直接默认为 Asia/Shanghai
     * }
     */
    private String schedule;

    /**
     * 任务编码（必填）
     */
    private String processDefinitionCode;

    /**
     * 失败策略默认为 CONTINUE
     */
    private String failureStrategy;

    /**
     * 默认 default
     */
    private String workerGroup;

    /**
     * 默认 default
     */
    private String tenantCode;

}
