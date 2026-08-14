/*
 * Copyright (c) 2025-present Jiangsu Qiantong Technology Co., Ltd.
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

package tech.qiantong.qdata.module.dpp.service.etl;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessInstance;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.ExecuteType;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskInstanceLogStatusRespDTO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.*;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceDO;

/**
 * Data Integration Task Instance Service Interface
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlTaskInstanceService extends IService<DppEtlTaskInstanceDO> {

    /**
     * Get data integration task instance pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data integration task instance pagination list
     */
    PageResult<DppEtlTaskInstanceDO> getDppEtlTaskInstancePage(DppEtlTaskInstancePageReqVO pageReqVO);

    DppEtlTaskInstanceRespVO getDppEtlTaskInstanceById(DppEtlTaskInstancePageReqVO pageReqVO);

    /**
     * Create data integration task instance
     *
     * @param createReqVO Data integration task instance info
     * @return Data integration task instance ID
     */
    Long createDppEtlTaskInstance(DppEtlTaskInstanceSaveReqVO createReqVO);

    /**
     * Update data integration task instance
     *
     * @param updateReqVO Data integration task instance info
     */
    int updateDppEtlTaskInstance(DppEtlTaskInstanceSaveReqVO updateReqVO);

    /**
     * Delete data integration task instance
     *
     * @param idList Data integration task instance ID list
     */
    int removeDppEtlTaskInstance(Collection<Long> idList);

    /**
     * Get data integration task instance detail
     *
     * @param id Data integration task instance ID
     * @return Data integration task instance
     */
    DppEtlTaskInstanceDO getDppEtlTaskInstanceById(Long id);

    /**
     * Get all data integration task instance list
     *
     * @return Data integration task instance list
     */
    List<DppEtlTaskInstanceDO> getDppEtlTaskInstanceList();

    /**
     * Get all data integration task instance Map
     *
     * @return Data integration task instance Map
     */
    Map<Long, DppEtlTaskInstanceDO> getDppEtlTaskInstanceMap();


    /**
     * Import data integration task instance data
     *
     * @param importExcelList Data integration task instance data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName        Operator
     * @return Result
     */
    String importDppEtlTaskInstance(List<DppEtlTaskInstanceRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Create task instance
     *
     * @param processInstance
     */
    Boolean createTaskInstance(ProcessInstance processInstance);

    /**
     * Update task instance
     *
     * @param processInstance
     * @return
     */
    Boolean updateTaskInstance(ProcessInstance processInstance);

    /**
     * Get data by dsId
     *
     * @param dsId
     * @return
     */
    DppEtlTaskInstanceDO getByDsId(Long dsId);

    /**
     * Get task instance ID by DS process instance ID
     *
     * @param dsId
     * @return
     */
    Long getIdByDsId(Long dsId);

    PageResult<DppEtlTaskInstanceTreeListRespVO> treeList(DppEtlTaskInstanceTreeListReqVO reqVO);

    /**
     * Execute command
     *
     * @param taskInstanceId
     * @param executeType
     * @return
     */
    AjaxResult execute(Long taskInstanceId, ExecuteType executeType);

    /**
     * Get sub-task list
     *
     * @param taskInstanceId
     * @param nodeInstanceId
     * @return
     */
    List<DppEtlTaskInstanceTreeListRespVO> subNodelist(Long taskInstanceId, Long nodeInstanceId);

    /**
     * Get log by instance ID
     * @param taskInstanceId
     * @return
     */
    DppEtlTaskInstanceLogStatusRespDTO getLogByTaskInstanceId(Long taskInstanceId);

    DppEtlTaskInstanceLogDetailRespVO getLogDetailByTaskInstanceId(Long taskInstanceId);

    DppEtlTaskInstanceStatisticsRespVO getStatistics(
            Long projectId, String projectCode, Long taskId, String taskType);

    /**
     * Get running task instance
     * @param taskId
     * @return
     */
    Long getRunTaskInstance(Long taskId);

    /**
     * Get data integration task detail info by task instance ID
     * @param id
     * @return
     */
    DppEtlTaskUpdateQueryRespVO getTaskInfo(Long id);

    /**
     * Get the last task instance by task code
     * @param code
     * @return
     */
    DppEtlTaskInstanceDO getLastTaskInstanceByTaskCode(String code);
}
