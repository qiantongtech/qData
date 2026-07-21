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

package tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * API service call log create/update Request VO DS_API_LOG
 *
 * @author lhs
 * @date 2025-02-12
 */
@Schema(description = "API服务调用日志 Response VO")
@Data
public class DsApiLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "类目ID", example = "")
    private Long catId;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "类目名称", example = "")
    private String catName;

    @Schema(description = "调用url", example = "")
    @Size(max = 256, message = "调用url长度不能超过256个字符")
    private String callerUrl;

    @Schema(description = "调用参数", example = "")
    @Size(max = 256, message = "调用参数长度不能超过256个字符")
    private String callerParams;

    @Schema(description = "调用开始时间", example = "")
    private Date callerStartDate;

    @Schema(description = "调用结束时间", example = "")
    private Date callerEndDate;

    @Schema(description = "调用数据量", example = "")
    private Long callerSize;

    @Schema(description = "调用耗时(毫秒)", example = "")
    private Long callerTime;

    @Schema(description = "信息记录", example = "")
    @Size(max = 256, message = "信息记录长度不能超过256个字符")
    private String msg;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
