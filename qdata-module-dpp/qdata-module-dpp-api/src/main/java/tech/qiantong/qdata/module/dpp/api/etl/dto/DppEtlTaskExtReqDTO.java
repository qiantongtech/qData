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

/**
 * 数据集成任务-扩展数据 DTO 对象 DPP_ETL_TASK_EXT
 *
 * @author qdata
 * @date 2025-04-16
 */
@Data
public class DppEtlTaskExtReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 数据汇聚任务id */
    private Long taskId;

    /** 数据汇聚节点id */
    private Long etlNodeId;

    /** 数据汇聚节点名称 */
    private String etlNodeName;

    /** 数据汇聚节点编码 */
    private String etlNodeCode;

    /** 数据汇聚节点版本 */
    private Long etlNodeVersion;

    /** 数据汇聚节点关系id */
    private Long etlRelationId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
