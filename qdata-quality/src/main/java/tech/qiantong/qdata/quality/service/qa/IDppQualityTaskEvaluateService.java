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
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluatePageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluateRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluateSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskEvaluateDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data quality task-evaluation rules Service interface
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface IDppQualityTaskEvaluateService extends IService<DppQualityTaskEvaluateDO> {

    /**
     * Get a paginated list of data quality tasks-evaluation rules
     *
     * @param pageReqVO paging request
     * @return Data quality task-evaluation rule paginated list
     */
    PageResult<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluatePage(DppQualityTaskEvaluatePageReqVO pageReqVO);

    /**
     * Create data quality tasks-evaluation rules
     *
     * @param createReqVO Data quality task-evaluation rule information
     * @return Data quality task-evaluation rule number
     */
    Long createDppQualityTaskEvaluate(DppQualityTaskEvaluateSaveReqVO createReqVO);

    /**
     * Update data quality task-evaluation rules
     *
     * @param updateReqVO Data quality task-evaluation rule information
     */
    int updateDppQualityTaskEvaluate(DppQualityTaskEvaluateSaveReqVO updateReqVO);

    /**
     * Delete data quality task-evaluation rules
     *
     * @param idList Data quality task-evaluation rule number
     */
    int removeDppQualityTaskEvaluate(Collection<Long> idList);

    /**
     * Get data quality task-evaluation rule details
     *
     * @param id data quality task-evaluation rule number
     * @return Data quality task-evaluation rules
     */
    DppQualityTaskEvaluateDO getDppQualityTaskEvaluateById(Long id);

    List<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateList(List<Long> idList);

    /**
     * Get a list of all data quality tasks-evaluation rules
     *
     * @return Data quality task-evaluation rule list
     */
    List<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateList();

    /**
     * Get all data quality tasks-evaluation rules map
     *
     * @return Data quality task-evaluation rules Map
     */
    Map<Long, DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateMap();


    /**
     * Import data quality task-evaluation rule data
     *
     * @param importExcelList Data quality task-evaluation rule data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    String importDppQualityTaskEvaluate(List<DppQualityTaskEvaluateRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
