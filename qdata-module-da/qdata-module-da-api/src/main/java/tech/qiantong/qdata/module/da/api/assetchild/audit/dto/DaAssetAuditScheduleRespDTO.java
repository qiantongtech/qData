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
 */

package tech.qiantong.qdata.module.da.api.assetchild.audit.dto;

import lombok.Data;

/**
 * 资产稽查调度 DTO 对象 DA_ASSET_AUDIT_SCHEDULE
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
public class DaAssetAuditScheduleRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产ID */
    private Long assetId;

    /** 是否开启稽查调度（0否1是） */
    private String scheduleFlag;

    /** cron执行表达式 */
    private String cronExpression;

    /** 节点id */
    private Long nodeId;

    /** 节点编码 */
    private String nodeCode;

    /** 任务id */
    private Long taskId;

    /** 任务编码 */
    private String taskCode;

    /** 定时任务调度表id */
    private Long systemJobId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
