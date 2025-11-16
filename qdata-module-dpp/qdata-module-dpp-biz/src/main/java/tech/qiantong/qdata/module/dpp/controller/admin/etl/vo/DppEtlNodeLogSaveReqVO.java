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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据集成节点-日志 创建/修改 Request VO DPP_ETL_NODE_LOG
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成节点-日志 Response VO")
@Data
public class DppEtlNodeLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    /** 任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：作业任务 */
    private String taskType;

    @Schema(description = "节点类型", example = "")
    @Size(max = 256, message = "节点类型长度不能超过256个字符")
    private String type;

    @Schema(description = "节点名称", example = "")
    @Size(max = 256, message = "节点名称长度不能超过256个字符")
    private String name;

    @Schema(description = "节点编码", example = "")
    @Size(max = 256, message = "节点编码长度不能超过256个字符")
    private String code;

    @Schema(description = "节点版本", example = "")
    private Long version;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    @Size(max = 256, message = "项目编码长度不能超过256个字符")
    private String projectCode;

    @Schema(description = "节点参数", example = "")
    @Size(max = 256, message = "节点参数长度不能超过256个字符")
    private String parameters;

    @Schema(description = "任务优先级", example = "")
    @Size(max = 256, message = "任务优先级长度不能超过256个字符")
    private String priority;

    @Schema(description = "失败重试次数", example = "")
    private Long failRetryTimes;

    @Schema(description = "失败重试间隔", example = "")
    private Long failRetryInterval;

    @Schema(description = "超时时间", example = "")
    private Long timeout;

    @Schema(description = "延迟执行时间", example = "")
    private Long delayTime;

    @Schema(description = "CPU配额", example = "")
    private Long cpuQuota;

    @Schema(description = "最大内存", example = "")
    private Long memoryMax;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "组件类型", example = "")
    private String componentType;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

}
