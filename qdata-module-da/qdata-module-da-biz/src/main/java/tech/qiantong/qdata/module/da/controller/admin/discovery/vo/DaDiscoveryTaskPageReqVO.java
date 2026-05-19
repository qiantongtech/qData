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

package tech.qiantong.qdata.module.da.controller.admin.discovery.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 数据发现任务 Request VO 对象 DA_DISCOVERY_TASK
 *
 * @author qdata
 * @date 2025-02-11
 */
@Schema(description = "数据发现任务 Request VO")
@Data
public class DaDiscoveryTaskPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "任务名称", example = "")
    private String name;

    @Schema(description = "数据连接id", example = "")
    private Long datasourceId;

    @Schema(description = "任务状态", example = "")
    private String status;

    @Schema(description = "cron执行表达式", example = "")
    private String cronExpression;

    @Schema(description = "联系人", example = "")
    private String contact;

    @Schema(description = "邮箱", example = "")
    private String email;

    @Schema(description = "联系人ID", example = "")
    private Long contactId;

    /** 上次变化表数 */
    @Schema(description = "上次变化表数", example = "")
    private Long lastTableCount;

    @Schema(description = "联系电话", example = "")
    private String contactNumber;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "定时任务调度表id", example = "")
    private Long systemJobId;

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
}
