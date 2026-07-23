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
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppEvaluateLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Evaluation rule result Service interface
 *
 * @author qdata
 * @date 2025-07-21
 */
public interface IDppEvaluateLogService extends IService<DppEvaluateLogDO> {

    /**
     * Get a paginated list of evaluation rule results
     *
     * @param pageReqVO paging request
     * @return paging list of evaluation rule results
     */
    PageResult<DppEvaluateLogDO> getDppEvaluateLogPage(DppEvaluateLogPageReqVO pageReqVO);

    /**
     * Create evaluation rule results
     *
     * @param createReqVO evaluation rule result information
     * @return evaluation rule result number
     */
    Long createDppEvaluateLog(DppEvaluateLogSaveReqVO createReqVO);

    /**
     * Update evaluation rule results
     *
     * @param updateReqVO evaluation rule result information
     */
    int updateDppEvaluateLog(DppEvaluateLogSaveReqVO updateReqVO);

    /**
     * Delete evaluation rule results
     *
     * @param idList evaluation rule result number
     */
    int removeDppEvaluateLog(Collection<Long> idList);

    /**
     * Get details of evaluation rules results
     *
     * @param id evaluation rule result number
     * @return evaluation rule results
     */
    DppEvaluateLogDO getDppEvaluateLogById(Long id);

    /**
     * Get a list of all evaluation rule results
     *
     * @return Evaluation rule result list
     */
    List<DppEvaluateLogDO> getDppEvaluateLogList();

    /**
     * Get all evaluation rule results Map
     *
     * @return Evaluation rule results Map
     */
    Map<Long, DppEvaluateLogDO> getDppEvaluateLogMap();


    /**
     * Import evaluation rule result data
     *
     * @param importExcelList Evaluation rule result data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    String importDppEvaluateLog(List<DppEvaluateLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
