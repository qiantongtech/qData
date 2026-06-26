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

/**
 * 数据集成任务节点关系 DTO 对象 DPP_ETL_TASK_NODE_REL
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlTaskNodeRelRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 项目id */
    private Long projectId;

    /** 项目编码 */
    private String projectCode;

    /** 任务id */
    private Long taskId;

    /** 任务编码 */
    private String taskCode;

    /** 任务版本 */
    private Long taskVersion;

    /** 前节点id */
    private Long preNodeId;

    /** 前节点编码 */
    private String preNodeCode;

    /** 前节点版本 */
    private Long preNodeVersion;

    /** 后节点id */
    private Long postNodeId;

    /** 后节点编码 */
    private String postNodeCode;

    /** 后节点版本 */
    private Long postNodeVersion;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
