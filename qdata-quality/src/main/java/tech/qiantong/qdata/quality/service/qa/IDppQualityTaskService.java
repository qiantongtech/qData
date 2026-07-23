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

package tech.qiantong.qdata.quality.service.qa;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data quality task Service interface
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface IDppQualityTaskService extends IService<DppQualityTaskDO> {

    /**
     * Get a paginated list of data quality tasks
     *
     * @param pageReqVO paging request
     * @return paginated list of data quality tasks
     */
    PageResult<DppQualityTaskDO> getDppQualityTaskPage(DppQualityTaskPageReqVO pageReqVO);

    /**
     * Create data quality tasks
     *
     * @param createReqVO data quality task information
     * @return data quality task number
     */
    Long createDppQualityTask(DppQualityTaskSaveReqVO createReqVO);

    /**
     * Update data quality tasks
     *
     * @param updateReqVO data quality task information
     */
    int updateDppQualityTask(DppQualityTaskSaveReqVO updateReqVO);

    /**
     * Delete data quality tasks
     *
     * @param idList data quality task number
     */
    int removeDppQualityTask(Collection<Long> idList);

    /**
     * Get data quality task details
     *
     * @param id data quality task number
     * @return data quality task
     */
    DppQualityTaskRespVO getDppQualityTaskById(Long id);

    /**
     * Get the full data quality task list
     *
     * @return Data quality task list
     */
    List<DppQualityTaskDO> getDppQualityTaskList();

    /**
     * Get a map of all data quality tasks
     *
     * @return Data quality task map
     */
    Map<Long, DppQualityTaskDO> getDppQualityTaskMap();


    /**
     * Import data quality task data
     *
     * @param importExcelList Data quality task data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    String importDppQualityTask(List<DppQualityTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
