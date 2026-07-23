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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSchedulerDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Integration Scheduling Info Service Interface
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlSchedulerService extends IService<DppEtlSchedulerDO> {

    /**
     * Get data integration scheduling info pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data integration scheduling info pagination list
     */
    PageResult<DppEtlSchedulerDO> getDppEtlSchedulerPage(DppEtlSchedulerPageReqVO pageReqVO);

    /**
     * Create data integration scheduling info
     *
     * @param createReqVO Data integration scheduling info
     * @return Data integration scheduling info ID
     */
    Long createDppEtlScheduler(DppEtlSchedulerSaveReqVO createReqVO);
    DppEtlSchedulerDO createDppEtlSchedulerNew(DppEtlSchedulerSaveReqVO createReqVO);

    /**
     * Update data integration scheduling info
     *
     * @param updateReqVO Data integration scheduling info
     */
    int updateDppEtlScheduler(DppEtlSchedulerSaveReqVO updateReqVO);

    /**
     * Delete data integration scheduling info
     *
     * @param idList Data integration scheduling info ID list
     */
    int removeDppEtlScheduler(Collection<Long> idList);

    /**
     * Get data integration scheduling info detail
     *
     * @param id Data integration scheduling info ID
     * @return Data integration scheduling info
     */
    DppEtlSchedulerDO getDppEtlSchedulerById(Long id);

    DppEtlSchedulerDO getDppEtlSchedulerById(DppEtlSchedulerPageReqVO pageReqVO);

    /**
     * Get all data integration scheduling info list
     *
     * @return Data integration scheduling info list
     */
    List<DppEtlSchedulerDO> getDppEtlSchedulerList();

    /**
     * Get all data integration scheduling info Map
     *
     * @return Data integration scheduling info Map
     */
    Map<Long, DppEtlSchedulerDO> getDppEtlSchedulerMap();


    /**
     * Import data integration scheduling info data
     *
     * @param importExcelList Data integration scheduling info data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importDppEtlScheduler(List<DppEtlSchedulerRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
