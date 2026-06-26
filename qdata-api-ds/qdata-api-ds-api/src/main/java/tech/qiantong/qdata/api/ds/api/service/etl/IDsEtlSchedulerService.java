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

package tech.qiantong.qdata.api.ds.api.service.etl;

import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsSchedulerRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsSchedulerSaveReqDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsSchedulerUpdateReqDTO;

/**
 * <P>
 * 用途:ds调度相关接口
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-21 10:06
 **/
public interface IDsEtlSchedulerService {

    /**
     * 创建调度器 (只有任务发布了才能调用该接口)
     */
    DsSchedulerRespDTO saveScheduler(DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO, String projectCode);

    /**
     * 修改调度器
     */
    DsSchedulerRespDTO updateScheduler(DsSchedulerUpdateReqDTO dsSchedulerUpdateReqDTO, String projectCode);

    /**
     * 上线调度器 (只有任务发布了才能调用该接口)
     *
     * @param projectCode
     * @param id          调度表中dsId
     * @return
     */
    DsStatusRespDTO onlineScheduler(String projectCode, Long id);

    /**
     * 下线调度器 (只有任务发布了才能调用该接口)
     *
     * @param projectCode
     * @param id          调度表中dsId
     * @return
     */
    DsStatusRespDTO offlineScheduler(String projectCode, Long id);


    /**
     * 根据任务编码获取调度信息
     *
     * @param taskCode
     * @return
     */
    DsSchedulerRespDTO getByTaskCode(String projectCode, String taskCode);
}
