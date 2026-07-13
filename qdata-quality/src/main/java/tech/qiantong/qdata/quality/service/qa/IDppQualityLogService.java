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
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data quality log Service interface
 *
 * @author qdata
 * @date 2025-07-19
 */
public interface IDppQualityLogService extends IService<DppQualityLogDO> {

    /**
     * Get a paginated list of data quality logs
     *
     * @param pageReqVO paging request
     * @return Data quality log paginated list
     */
    PageResult<DppQualityLogDO> getDppQualityLogPage(DppQualityLogPageReqVO pageReqVO);

    /**
     * Create data quality log
     *
     * @param createReqVO data quality log information
     * @return data quality log number
     */
    Long createDppQualityLog(DppQualityLogSaveReqVO createReqVO);

    /**
     * Update data quality log
     *
     * @param updateReqVO data quality log information
     */
    int updateDppQualityLog(DppQualityLogSaveReqVO updateReqVO);

    /**
     * Delete data quality logs
     *
     * @param idList data quality log number
     */
    int removeDppQualityLog(Collection<Long> idList);

    /**
     * Get data quality log details
     *
     * @param id data quality log number
     * @return data quality log
     */
    DppQualityLogDO getDppQualityLogById(Long id);

    /**
     * Get a list of all data quality logs
     *
     * @return Data quality log list
     */
    List<DppQualityLogDO> getDppQualityLogList();

    /**
     * Get all data quality log map
     *
     * @return Data quality log Map
     */
    Map<Long, DppQualityLogDO> getDppQualityLogMap();


    /**
     * Import data quality log data
     *
     * @param importExcelList Data quality log data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    String importDppQualityLog(List<DppQualityLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
