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

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSqlTempDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Integration SQL Template Service Interface
 *
 * @author FXB
 * @date 2025-06-25
 */
public interface IDppEtlSqlTempService extends IService<DppEtlSqlTempDO> {

    /**
     * Get data integration SQL template pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data integration SQL template pagination list
     */
    PageResult<DppEtlSqlTempDO> getDppEtlSqlTempPage(DppEtlSqlTempPageReqVO pageReqVO);

    /**
     * Create data integration SQL template
     *
     * @param createReqVO Data integration SQL template info
     * @return Data integration SQL template ID
     */
    Long createDppEtlSqlTemp(DppEtlSqlTempSaveReqVO createReqVO);

    /**
     * Update data integration SQL template
     *
     * @param updateReqVO Data integration SQL template info
     */
    int updateDppEtlSqlTemp(DppEtlSqlTempSaveReqVO updateReqVO);

    /**
     * Delete data integration SQL template
     *
     * @param idList Data integration SQL template ID list
     */
    int removeDppEtlSqlTemp(Collection<Long> idList);

    /**
     * Get data integration SQL template detail
     *
     * @param id Data integration SQL template ID
     * @return Data integration SQL template
     */
    DppEtlSqlTempDO getDppEtlSqlTempById(Long id);

    /**
     * Get all data integration SQL template list
     *
     * @return Data integration SQL template list
     */
    List<DppEtlSqlTempDO> getDppEtlSqlTempList();

    /**
     * Get all data integration SQL template Map
     *
     * @return Data integration SQL template Map
     */
    Map<Long, DppEtlSqlTempDO> getDppEtlSqlTempMap();


    /**
     * Import data integration SQL template data
     *
     * @param importExcelList Data integration SQL template data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importDppEtlSqlTemp(List<DppEtlSqlTempRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
