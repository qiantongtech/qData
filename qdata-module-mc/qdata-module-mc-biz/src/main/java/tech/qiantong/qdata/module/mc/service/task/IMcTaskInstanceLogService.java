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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceLogDO;

import java.util.Collection;
import java.util.List;

/**
 * Collection task instance-Log Service interface
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface IMcTaskInstanceLogService extends IService<McTaskInstanceLogDO> {

    /**
     * Obtain collection task instance-log paging list
     *
     * @param pageReqVO paging request
     * @return Collection task instance-log paging list
     */
    PageResult<McTaskInstanceLogDO> getMcTaskInstanceLogPage(McTaskInstanceLogPageReqVO pageReqVO);

    /**
     * Create a collection task instance-log
     *
     * @param createReqVO collection task instance-log information
     * @return collection task instance-log number
     */
    Long createMcTaskInstanceLog(McTaskInstanceLogSaveReqVO createReqVO);

    /**
     * Update collection task instance-log
     *
     * @param updateReqVO collection task instance-log information
     */
    int updateMcTaskInstanceLog(McTaskInstanceLogSaveReqVO updateReqVO);

    /**
     * Delete collection task instance-log
     *
     * @param idList collection task instance-log number
     */
    int removeMcTaskInstanceLog(Collection<Long> idList);

    /**
     * Obtain collection task instance-log details
     *
     * @param id collection task instance-log number
     * @return collection task instance-log
     */
    McTaskInstanceLogDO getMcTaskInstanceLogById(Long id);

    /**
     * Get all collection task instances-log list
     *
     * @return Collection task instance-log list
     */
    List<McTaskInstanceLogDO> getMcTaskInstanceLogList();

    void taskInstanceLogAppend(Long taskInstanceId, Long taskId, String logStr);

    int saveOrUpdateByPk(McTaskInstanceLogDO entity);
}
