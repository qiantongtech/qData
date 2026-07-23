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

package tech.qiantong.qdata.module.dpp.service.etl;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskAssetReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;
/**
 * Data Quality Log Service Interface
 *
 * @author qdata
 * @date 2025-07-19
 */
public interface IDppQualityLogService extends IService<DppQualityLogDO> {

    /**
     * Get data quality log pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data quality log pagination list
     */
    PageResult<DppQualityLogDO> getDppQualityLogPage(DppQualityLogPageReqVO pageReqVO);

    /**
     * Create data quality log
     *
     * @param createReqVO Data quality log info
     * @return Data quality log ID
     */
    Long createDppQualityLog(DppQualityLogSaveReqVO createReqVO);

    /**
     * Update data quality log
     *
     * @param updateReqVO Data quality log info
     */
    int updateDppQualityLog(DppQualityLogSaveReqVO updateReqVO);

    /**
     * Delete data quality log
     *
     * @param idList Data quality log ID list
     */
    int removeDppQualityLog(Collection<Long> idList);

    /**
     * Get data quality log detail
     *
     * @param id Data quality log ID
     * @return Data quality log
     */
    DppQualityLogDO getDppQualityLogById(Long id);
    DppQualityLogDO selectPrevLogByIdWithWrapper(Long id);

    /**
     * Get data quality log detail
     * Asset specific
     * @return Data quality log
     */
    DppQualityLogDO getDppQualityLogById(DppQualityTaskAssetReqVO dppQualityTaskAssetReqVO);

    /**
     * Get all data quality log list
     *
     * @return Data quality log list
     */
    List<DppQualityLogDO> getDppQualityLogList();

    /**
     * Get all data quality log Map
     *
     * @return Data quality log Map
     */
    Map<Long, DppQualityLogDO> getDppQualityLogMap();


    /**
     * Import data quality log data
     *
     * @param importExcelList Data quality log data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importDppQualityLog(List<DppQualityLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Send data quality log message
     * @param id
     */
    void sendMessage(Long id);

}
