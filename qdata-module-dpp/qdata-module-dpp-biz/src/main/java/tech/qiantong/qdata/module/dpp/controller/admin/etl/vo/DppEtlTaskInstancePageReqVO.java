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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 数据集成任务实例 Request VO 对象 DPP_ETL_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成任务实例 Request VO")
@Data
public class DppEtlTaskInstancePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "任务类型", example = "1：离线任务 2：实时任务 3：数据开发任务 4：作业任务")
    private String taskType;

    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "任务实例名称", example = "")
    private String name;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "任务编码", example = "")
    private String taskCode;

    @Schema(description = "任务版本", example = "")
    private Integer taskVersion;

    @Schema(description = "状态历史(json列表)", example = "")
    private String statusHistory;

    @Schema(description = "责任人", example = "")
    private String personCharge;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "调度时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scheduleTime;

    @Schema(description = "开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(description = "运行次数", example = "")
    private Integer runTimes;

    @Schema(description = "运行类型", example = "")
    private String commandType;

    @Schema(description = "最大重试次数", example = "")
    private Long maxTryTimes;

    @Schema(description = "失败策略", example = "")
    private String failureStrategy;

    @Schema(description = "是否是子任务", example = "")
    private String subTaskFlag;

    @Schema(description = "状态", example = "")
    private String status;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;


}
