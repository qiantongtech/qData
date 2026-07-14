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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <P>
 * 用途:实例日志响应数据
 * </p>
 *
 * @author: FXB
 * @create: 2025-07-01 13:49
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DppEtlTaskInstanceLogStatusRespDTO {

    /**
     * 状态;0：提交成功 1：正在执行 2:准备暂停 3：暂停 4：准备停止 5：停止 6：失败 7：成功 12：延时执行  14：串行等待  15 ：准备锁定 16：锁定
     * 5：停止 6：失败 7：成功 时停止日志轮询
     */
    private String status;

    /**
     * 状态 1:进行中 2:已结束
     */
    private String log;

    /**
     * 节点实例列表
     */
    private List<DppEtlNodeInstanceRespDTO> nodeInstanceList;

    /**
     * DataX输入数据量
     */
    private Long inputRecords;

    /**
     * DataX输出数据量
     */
    private Long outputRecords;

    /**
     * DataX节点名称列表
     */
    private List<String> nodeNames;

    /**
     * DataX成功节点数量
     */
    private Integer successCount;

    /**
     * DataX失败节点数量
     */
    private Integer failedCount;

    /**
     * DataX停止节点数量
     */
    private Integer stoppedCount;
}
