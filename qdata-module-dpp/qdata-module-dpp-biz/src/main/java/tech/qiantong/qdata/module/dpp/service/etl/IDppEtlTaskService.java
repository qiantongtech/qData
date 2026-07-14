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
 * Handle task-related data and operations.
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlTaskService extends IService<DppEtlTaskDO> {

    /**
     * Handle task-related data and operations.
     *
     * @param pageReqVO parameter value
     * @return the operation result
     */
    PageResult<DppEtlTaskDO> getDppEtlTaskPage(DppEtlTaskPageReqVO pageReqVO);

    /**
     * Handle task-related data and operations.
     *
     * @param createReqVO parameter value
     * @return the operation result
     */
    Long createDppEtlTask(DppEtlTaskSaveReqVO createReqVO);

    /**
     * Handle task-related data and operations.
     *
     * @param updateReqVO parameter value
     */
    int updateDppEtlTask(DppEtlTaskSaveReqVO updateReqVO);

    /**
     * Handle task-related data and operations.
     *
     * @param idList parameter value
     */
    int removeDppEtlTask(Collection<Long> idList);

    /**
     * Handle task-related data and operations.
     *
     * @param id parameter value
     * @return the operation result
     */
    DppEtlTaskRespVO getDppEtlTaskById(Long id);

    /**
     * Handle task-related data and operations.
     *
     * @return the operation result
     */
    List<DppEtlTaskDO> getDppEtlTaskList();

    /**
     * Handle task-related data and operations.
     *
     * @return the operation result
     */
    Map<Long, DppEtlTaskDO> getDppEtlTaskMap();


    /**
     * Handle task-related data and operations.
     *
     * @param importExcelList parameter value
     * @param isUpdateSupport parameter value
     * @param operName parameter value
     * @return the operation result
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
     * Handle task-related data and operations.
     *
     * @param taskCode
     * @return
     */
    Long getTaskIdByTaskCode(String taskCode);

    /**
     * Handle task-related data and operations.
     *
     * @param taskCode
     * @return
     */
    DppEtlTaskRespDTO getTaskByTaskCode(String taskCode);

    List<DppEtlTaskTreeRespVO> getDppEtlTaskListTree(DppEtlTaskPageReqVO dppEtlTask);

    /**
     * Start the task.
     * @param id
     * @return
     */
    AjaxResult startDppEtlTask(Long id);

    /**
     * Handle task-related data and operations.
     * @param id
     * @return
     */
    void startDppEtlTaskDataIntegration(Long id);

    /**
     * Handle task-related data and operations.
     *
     * @param id
     */
    void startDppEtlTaskDataDevelopment(Long id);

    List<DppEtlTaskRespVO> getSubTaskStatusList(DppEtlTaskPageReqVO dppEtlTask);

    Map<String, Object> updateReleaseJobTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    Map<String, Object> updateReleaseSchedule(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    /**
     * Handle task-related data and operations.
     * @param dppEtlNewNodeSaveReqVO
     * @return
     */
    DppEtlTaskSaveReqVO createEtlTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    /**
     * Handle task-related data and operations.
     * @param dppEtlNewNodeSaveReqVO
     * @return
     */
    DppEtlTaskSaveReqVO updateEtlTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);
    DppEtlNewNodeSaveReqVO createEtlTaskFront(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    DppEtlTaskSaveReqVO createEtlTaskFrontPostposition(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    DppEtlTaskUpdateQueryRespVO getupdateQueryFront(Long id);

    DppEtlTaskSaveReqVO copyCreateEtl(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);
}
