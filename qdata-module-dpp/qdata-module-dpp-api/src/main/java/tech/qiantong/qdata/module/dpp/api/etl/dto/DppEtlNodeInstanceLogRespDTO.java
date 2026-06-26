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

package tech.qiantong.qdata.module.dpp.api.etl.dto;

import lombok.Data;

import java.util.Date;

/**
 * 数据集成节点实例-日志 DTO 对象 DPP_ETL_NODE_INSTANCE_LOG
 *
 * @author qdata
 * @date 2025-08-05
 */
@Data
public class DppEtlNodeInstanceLogRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID节点实例id */
    private Long nodeInstanceId;

    /** 时间 */
    private Date tm;

    /** 任务类型 */
    private String taskType;

    /** 节点id */
    private Long nodeId;

    /** 节点编码 */
    private String nodeCode;

    /** 任务实例id */
    private Long taskInstanceId;

    /** 日志内容 */
    private String logContent;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
