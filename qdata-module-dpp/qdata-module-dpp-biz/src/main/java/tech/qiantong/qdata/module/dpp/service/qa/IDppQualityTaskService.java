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

package tech.qiantong.qdata.module.dpp.service.qa;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.*;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Quality Task Service Interface
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface IDppQualityTaskService extends IService<DppQualityTaskDO> {

    /**
     * Get data quality task pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data quality task pagination list
     */
    PageResult<DppQualityTaskDO> getDppQualityTaskPage(DppQualityTaskPageReqVO pageReqVO);

    /**
     * Create data quality task
     *
     * @param createReqVO Data quality task info
     * @return Data quality task ID
     */
    Long createDppQualityTask(DppQualityTaskSaveReqVO createReqVO);

    /**
     * Update data quality task
     *
     * @param updateReqVO Data quality task info
     */
    int updateDppQualityTask(DppQualityTaskSaveReqVO updateReqVO);

    /**
     * Delete data quality task
     *
     * @param idList Data quality task ID list
     */
    int removeDppQualityTask(Collection<Long> idList);

    /**
     * Get data quality task detail
     *
     * @param id Data quality task ID
     * @return Data quality task
     */
    DppQualityTaskRespVO getDppQualityTaskById(Long id);

    DppQualityTaskRespVO getQualityTaskAsset(DppQualityTaskAssetReqVO dppQualityTaskAssetReqVO);

    /**
     * Get all data quality task list
     *
     * @return Data quality task list
     */
    List<DppQualityTaskDO> getDppQualityTaskList();

    /**
     * Get all data quality task Map
     *
     * @return Data quality task Map
     */
    Map<Long, DppQualityTaskDO> getDppQualityTaskMap();


    /**
     * Import data quality task data
     *
     * @param importExcelList Data quality task data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importDppQualityTask(List<DppQualityTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Verify if data format is correct
     * @param dppQualityTaskEvaluate
     */
    String verifyInterfaceValue(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate);

    AjaxResult startDppQualityTask(Long id);

    boolean updateDppQualityTaskStatus(DppQualityTaskSaveReqVO daDiscoveryTask);

    JSONObject validationErrorDataSql(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate);

    JSONObject validationValidDataSql(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate);

    boolean updateDaDiscoveryTaskCronExpression(DppQualityTaskSaveReqVO daDiscoveryTask);
}
