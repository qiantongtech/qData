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

package tech.qiantong.qdata.module.da.controller.admin.discovery.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * Data Discovery Node Instance Log Response VO object DA_DISCOVERY_LOG_BODY
 *
 * @author qdata
 * @date 2025-10-15
 */
@Schema(description = "数据发现节点实例-日志 Response VO")
@Data
public class DaDiscoveryLogBodyRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "时间")
    @Schema(description = "时间; 日志入库的时间", example = "2025-10-15 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tm;

    @Excel(name = "任务id")
    @Schema(description = "任务id", example = "123")
    private Long taskId;

    @Excel(name = "日志内容")
    @Schema(description = "日志内容", example = "任务执行完成")
    private String logContent;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效;0：无效，1：有效", example = "1")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志;1：已删除，0：未删除", example = "0")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "admin")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "1")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2025-10-15 10:05:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "admin")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "1")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "2025-10-15 10:10:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "定时任务日志")
    private String remark;
}
