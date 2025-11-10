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
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
public class DsSchedulerSaveReqDTO {

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
