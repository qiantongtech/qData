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

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Data Discovery Node Instance Log Create/Update Request VO object DA_DISCOVERY_LOG_BODY
 *
 * @author qdata
 * @date 2025-10-15
 */
@Schema(description = "数据发现节点实例-日志 创建/修改 Request VO")
@Data
public class DaDiscoveryLogBodySaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "时间; 日志入库的时间", example = "2025-10-15 10:00:00")
    private Date tm;

    @Schema(description = "任务id", example = "123")
    private Long taskId;

    @Schema(description = "日志内容", example = "任务执行完成")
    @Size(max = 4000, message = "日志内容长度不能超过4000个字符")
    private String logContent;

    @Schema(description = "是否有效;0：无效，1：有效", example = "1")
    private Boolean validFlag;

    @Schema(description = "删除标志;1：已删除，0：未删除", example = "0")
    private Boolean delFlag;

    @Schema(description = "备注", example = "定时任务日志")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;

    @Schema(description = "任务编码", example = "DISCOVERY_TASK_001")
    @TableField(exist = false)
    private String taskCode;

    @Schema(description = "任务名称", example = "数据发现任务A")
    @TableField(exist = false)
    private String taskName;
}
