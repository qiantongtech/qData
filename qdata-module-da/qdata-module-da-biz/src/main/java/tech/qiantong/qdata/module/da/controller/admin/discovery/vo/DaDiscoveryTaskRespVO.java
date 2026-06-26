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
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据发现任务 Response VO 对象 DA_DISCOVERY_TASK
 *
 * @author qdata
 * @date 2025-02-11
 */
@Schema(description = "数据发现任务 Response VO")
@Data
public class DaDiscoveryTaskRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "任务名称")
    @Schema(description = "任务名称", example = "")
    private String name;

    @Excel(name = "数据连接id")
    @Schema(description = "数据连接id", example = "")
    private Long datasourceId;

    @Excel(name = "任务状态 0:上线 1:下线")
    @Schema(description = "任务状态", example = "")
    private String status;

    @Excel(name = "cron执行表达式")
    @Schema(description = "cron执行表达式", example = "")
    private String cronExpression;

    @Excel(name = "联系人")
    @Schema(description = "联系人", example = "")
    private String contact;

    @Schema(description = "邮箱", example = "")
    private String email;

    @Excel(name = "联系人ID")
    @Schema(description = "联系人ID", example = "")
    private Long contactId;

    /** 上次变化表数 */
    @Excel(name = "上次变化表数")
    @Schema(description = "上次变化表数", example = "")
    private Long lastTableCount;

    @Excel(name = "联系电话")
    @Schema(description = "联系电话", example = "")
    private String contactNumber;

    @Excel(name = "类目编码")
    @Schema(description = "类目编码", example = "")
    private String catCode;

    @TableField(exist = false)
    private String catName;

    @Excel(name = "数据源类型")
    @Schema(description = "数据源类型", example = "")
    @TableField(exist = false)
    private String datasourceType;

    @Excel(name = "数据源名称")
    @Schema(description = "数据源名称", example = "")
    @TableField(exist = false)
    private String datasourceName;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Excel(name = "定时任务调度表id")
    @Schema(description = "定时任务调度表id", example = "")
    private Long systemJobId;

    @Excel(name = "最后执行时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后执行时间", example = "")
    private Date lastExecuteTime;

    /** 节点id */
    @Schema(description = "节点id", example = "")
    private Long nodeId;

    /** 节点编码 */
    @Schema(description = "节点编码", example = "")
    private String nodeCode;

    /** 任务id */
    @Schema(description = "任务id", example = "")
    private Long taskId;

    /** 任务编码 */
    @Schema(description = "任务编码", example = "")
    private String taskCode;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    /**
     * 待提交
     */
    @TableField(exist = false)
    private long countPending;

    /**
     * 已提交
     */
    @TableField(exist = false)
    private long countSubmitted;
    /**
     * 已忽略
     */
    @TableField(exist = false)
    private long countIgnoreFlag;

    @TableField(exist = false)
    private String ip;


    @TableField(exist = false)
    private String misfirePolicy;
    @TableField(exist = false)
    private String jobGroup;
    @TableField(exist = false)
    private String concurrent;
}
