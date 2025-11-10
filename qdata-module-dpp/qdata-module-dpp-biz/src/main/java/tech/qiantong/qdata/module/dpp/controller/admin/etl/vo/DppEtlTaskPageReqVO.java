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

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据集成任务 Request VO 对象 DPP_ETL_TASK
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成任务 Request VO")
@Data
public class DppEtlTaskPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "任务类型", example = "1：离线任务 2：实时任务 3：数据开发任务 4：作业任务")
    private String type;

    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Schema(description = "任务名称", example = "")
    private String name;

    @Schema(description = "任务的执行策略", example = "")
    private String executionType;

    @Schema(description = "任务编码", example = "")
    private String code;

    @Schema(description = "任务版本", example = "")
    private Long version;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "责任人", example = "")
    private String personCharge;

    @Schema(description = "节点坐标信息", example = "")
    private String locations;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "超时时间", example = "")
    private Long timeout;

    @Schema(description = "抽取量", example = "")
    private Long extractionCount;

    @Schema(description = "写入量", example = "")
    private Long writeCount;

    @Schema(description = "任务状态", example = "")
    private String status;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;

    /** 类目编码 */
    private String catCode;
}
