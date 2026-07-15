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

package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskRespDTO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.*;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Integration Task Service Interface
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlTaskService extends IService<DppEtlTaskDO> {

    /**
     * Get data integration task pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data integration task pagination list
     */
    PageResult<DppEtlTaskDO> getDppEtlTaskPage(DppEtlTaskPageReqVO pageReqVO);

    /**
     * Create data integration task
     *
     * @param createReqVO Data integration task info
     * @return Data integration task ID
     */
    Long createDppEtlTask(DppEtlTaskSaveReqVO createReqVO);

    /**
     * Update data integration task
     *
     * @param updateReqVO Data integration task info
     */
    int updateDppEtlTask(DppEtlTaskSaveReqVO updateReqVO);

    /**
     * Delete data integration task
     *
     * @param idList Data integration task ID list
     */
    int removeDppEtlTask(Collection<Long> idList);

    /**
     * Get data integration task detail
     *
     * @param id Data integration task ID
     * @return Data integration task
     */
    DppEtlTaskRespVO getDppEtlTaskById(Long id);

    /**
     * Get all data integration task list
     *
     * @return Data integration task list
     */
    List<DppEtlTaskDO> getDppEtlTaskList();

    /**
     * Get all data integration task Map
     *
     * @return Data integration task Map
     */
    Map<Long, DppEtlTaskDO> getDppEtlTaskMap();


    /**
     * Import data integration task data
     *
     * @param importExcelList Data integration task data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importDppEtlTask(List<DppEtlTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);

    PageResult<DppEtlTaskRespVO> getDppEtlTaskPageList(DppEtlTaskPageReqVO dppEtlTask);

    DppEtlTaskSaveReqVO createProcessDefinition(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    Long getNodeUniqueKey(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    Map<String, Object> updateReleaseTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    DppEtlTaskSaveReqVO updateProcessDefinition(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    Map<String, Object> releaseTaskCrontab(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    DppEtlTaskUpdateQueryRespVO getuUpdateQueryInfo(Long id);

    /**
     * Get task ID by task code
     *
     * @param taskCode
     * @return
     */
    Long getTaskIdByTaskCode(String taskCode);

    /**
     * Get task info by task code
     *
     * @param taskCode
     * @return
     */
    DppEtlTaskRespDTO getTaskByTaskCode(String taskCode);

    List<DppEtlTaskTreeRespVO> getDppEtlTaskListTree(DppEtlTaskPageReqVO dppEtlTask);

    /**
     * 启动任务
     * @param id
     * @return
     */
    AjaxResult startDppEtlTask(Long id);

    /**
     * 启动任务数据集成
     * @param id
     * @return
     */
    void startDppEtlTaskDataIntegration(Long id);

    /**
     * 启动任务数据开发
     *
     * @param id
     */
    void startDppEtlTaskDataDevelopment(Long id);

    List<DppEtlTaskRespVO> getSubTaskStatusList(DppEtlTaskPageReqVO dppEtlTask);

    Map<String, Object> updateReleaseJobTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    Map<String, Object> updateReleaseSchedule(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    /**
     * Create ETL task
     * @param dppEtlNewNodeSaveReqVO
     * @return
     */
    DppEtlTaskSaveReqVO createEtlTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    /**
     * Update ETL task
     * @param dppEtlNewNodeSaveReqVO
     * @return
     */
    DppEtlTaskSaveReqVO updateEtlTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);
    DppEtlNewNodeSaveReqVO createEtlTaskFront(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    DppEtlTaskSaveReqVO createEtlTaskFrontPostposition(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    DppEtlTaskUpdateQueryRespVO getupdateQueryFront(Long id);

    DppEtlTaskSaveReqVO copyCreateEtl(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);
}
