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

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-26 15:27
 **/
@Data
public class DppEtlTaskInstanceTreeListReqVO extends PageParam {

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "任务实例名称", example = "")
    private String name;

    @Schema(description = "右查询名称", example = "")
    private String jobName;

    @Schema(description = "执行状态", example = "")
    private String status;

    @Schema(description = "执行开始时间(格式 yyyy-MM-dd)", example = "")
    private String startTime;

    @Schema(description = "执行结束时间(格式 yyyy-MM-dd)", example = "")
    private String endTime;
}
