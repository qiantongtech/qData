/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
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
