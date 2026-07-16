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
 * Description: DS scheduler related interfaces
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-21 10:06
 **/
public interface IDsEtlSchedulerService {

    /**
     * Create scheduler (can only be called after the task is released)
     */
    DsSchedulerRespDTO saveScheduler(DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO, String projectCode);

    /**
     * Update scheduler
     */
    DsSchedulerRespDTO updateScheduler(DsSchedulerUpdateReqDTO dsSchedulerUpdateReqDTO, String projectCode);

    /**
     * Online scheduler (can only be called after the task is released)
     *
     * @param projectCode
     * @param id          dsId in the scheduler table
     * @return
     */
    DsStatusRespDTO onlineScheduler(String projectCode, Long id);

    /**
     * Offline scheduler (can only be called after the task is released)
     *
     * @param projectCode
     * @param id          dsId in the scheduler table
     * @return
     */
    DsStatusRespDTO offlineScheduler(String projectCode, Long id);


    /**
     * Get scheduler info by task code
     *
     * @param taskCode
     * @return
     */
    DsSchedulerRespDTO getByTaskCode(String projectCode, String taskCode);
}
