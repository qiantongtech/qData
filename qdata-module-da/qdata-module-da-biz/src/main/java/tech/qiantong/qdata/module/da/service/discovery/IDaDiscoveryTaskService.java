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

package tech.qiantong.qdata.module.da.service.discovery;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTaskDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Discovery Task Service Interface
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface IDaDiscoveryTaskService extends IService<DaDiscoveryTaskDO> {

    /**
     * Get data discovery task page list
     *
     * @param pageReqVO page request
     * @return data discovery task page list
     */
    PageResult<DaDiscoveryTaskDO> getDaDiscoveryTaskPage(DaDiscoveryTaskPageReqVO pageReqVO);
    PageResult<DaDiscoveryTaskRespVO> getDaDiscoveryTaskListPage(DaDiscoveryTaskPageReqVO pageReqVO);

    /**
     * Create data discovery task
     *
     * @param createReqVO data discovery task info
     * @return data discovery task ID
     */
    Long createDaDiscoveryTask(DaDiscoveryTaskSaveReqVO createReqVO);

    /**
     * Update data discovery task
     *
     * @param updateReqVO data discovery task info
     */
    int updateDaDiscoveryTask(DaDiscoveryTaskSaveReqVO updateReqVO);
    int updateDaDiscoveryTask(DaDiscoveryTaskRespVO updateReqVO);

    /**
     * Delete data discovery task
     *
     * @param idList data discovery task ID list
     */
    int removeDaDiscoveryTask(Collection<Long> idList);

    /**
     * Get data discovery task details
     *
     * @param id data discovery task ID
     * @return data discovery task
     */
    DaDiscoveryTaskRespVO getDaDiscoveryTaskById(Long id);

    /**
     * Get all data discovery task list
     *
     * @return data discovery task list
     */
    List<DaDiscoveryTaskDO> getDaDiscoveryTaskList();

    /**
     * Get all data discovery task Map
     *
     * @return data discovery task Map
     */
    Map<Long, DaDiscoveryTaskDO> getDaDiscoveryTaskMap();


    /**
     * Import data discovery task data
     *
     * @param importExcelList data discovery task data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaDiscoveryTask(List<DaDiscoveryTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);

    boolean runDaDiscoveryTask(Long taskId);

    boolean updateDaDiscoveryTaskStatus(DaDiscoveryTaskSaveReqVO daDiscoveryTask);

    boolean updateDaDiscoveryTaskCronExpression(DaDiscoveryTaskSaveReqVO daDiscoveryTask);

    AjaxResult startDaDiscoveryTask(Long id);
}
