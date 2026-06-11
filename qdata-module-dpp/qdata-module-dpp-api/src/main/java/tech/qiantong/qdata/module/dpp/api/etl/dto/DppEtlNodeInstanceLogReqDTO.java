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
public class DppEtlNodeInstanceLogReqDTO {

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
