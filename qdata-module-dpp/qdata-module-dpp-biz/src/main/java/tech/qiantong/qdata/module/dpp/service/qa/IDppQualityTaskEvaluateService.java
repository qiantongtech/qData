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

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluatePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluateRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluateSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskEvaluateDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Quality Task Evaluation Rule Service Interface
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface IDppQualityTaskEvaluateService extends IService<DppQualityTaskEvaluateDO> {

    /**
     * Get data quality task evaluation rule pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data quality task evaluation rule pagination list
     */
    PageResult<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluatePage(DppQualityTaskEvaluatePageReqVO pageReqVO);

    /**
     * Create data quality task evaluation rule
     *
     * @param createReqVO Data quality task evaluation rule info
     * @return Data quality task evaluation rule ID
     */
    Long createDppQualityTaskEvaluate(DppQualityTaskEvaluateSaveReqVO createReqVO);

    /**
     * Update data quality task evaluation rule
     *
     * @param updateReqVO Data quality task evaluation rule info
     */
    int updateDppQualityTaskEvaluate(DppQualityTaskEvaluateSaveReqVO updateReqVO);

    /**
     * Delete data quality task evaluation rule
     *
     * @param idList Data quality task evaluation rule ID list
     */
    int removeDppQualityTaskEvaluate(Collection<Long> idList);

    /**
     * Get data quality task evaluation rule detail
     *
     * @param id Data quality task evaluation rule ID
     * @return Data quality task evaluation rule
     */
    DppQualityTaskEvaluateDO getDppQualityTaskEvaluateById(Long id);

    /**
     * Get all data quality task evaluation rule list
     *
     * @return Data quality task evaluation rule list
     */
    List<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateList();

    /**
     * Get all data quality task evaluation rule Map
     *
     * @return Data quality task evaluation rule Map
     */
    Map<Long, DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateMap();


    /**
     * Import data quality task evaluation rule data
     *
     * @param importExcelList Data quality task evaluation rule data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importDppQualityTaskEvaluate(List<DppQualityTaskEvaluateRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
