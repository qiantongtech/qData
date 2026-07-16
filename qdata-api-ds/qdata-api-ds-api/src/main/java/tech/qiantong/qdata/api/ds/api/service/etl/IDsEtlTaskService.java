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
import tech.qiantong.qdata.api.ds.api.etl.DsStartTaskReqDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsTaskSaveReqDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsTaskSaveRespDTO;

/**
 * <P>
 * Description: DS data integration task related interfaces
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 16:47
 **/
public interface IDsEtlTaskService {
    /**
     * Create task
     *
     * @param dsTaskSaveReqDTO
     * @param projectCode      project code
     * @return
     */
    DsTaskSaveRespDTO createTask(DsTaskSaveReqDTO dsTaskSaveReqDTO, Long projectCode);

    /**
     * Update task
     *
     * @param dsTaskSaveReqDTO
     * @param projectCode      project code
     * @param taskCode         task code
     * @return
     */
    DsTaskSaveRespDTO updateTask(DsTaskSaveReqDTO dsTaskSaveReqDTO, String projectCode, String taskCode);

    /**
     * Release or offline task
     *
     * @param releaseState releaseState status ONLINE: online OFFLINE: offline
     * @param projectCode  project code
     * @param code         task code
     * @return Note: After online, the scheduler also needs to be put online; when offline, the interface will handle the scheduler going offline simultaneously
     */
    DsStatusRespDTO releaseTask(String releaseState, String projectCode, String code);


    /**
     * Delete task
     *
     * @param projectCode project code
     * @param code        task code
     * @return Note: Only offline tasks can be deleted
     */
    DsStatusRespDTO deleteTask(String projectCode, String code);

    /**
     * Start task
     *
     * @param dsStartTaskReqDTO
     * @param projectCode      project code
     * @return
     */
    DsStatusRespDTO startTask(DsStartTaskReqDTO dsStartTaskReqDTO, String projectCode);


    /**
     *
     * @param code
     * @param projectCode
     * @return
     */
    DsTaskSaveRespDTO batchCopy(String code, String projectCode);

}
