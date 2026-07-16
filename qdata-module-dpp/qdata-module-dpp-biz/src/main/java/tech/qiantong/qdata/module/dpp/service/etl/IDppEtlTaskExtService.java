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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskExtPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskExtRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskExtSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskExtDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Integration Task Extended Data Service Interface
 *
 * @author qdata
 * @date 2025-04-16
 */
public interface IDppEtlTaskExtService extends IService<DppEtlTaskExtDO> {

    /**
     * Get data integration task extended data pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data integration task extended data pagination list
     */
    PageResult<DppEtlTaskExtDO> getDppEtlTaskExtPage(DppEtlTaskExtPageReqVO pageReqVO);

    /**
     * Create data integration task extended data
     *
     * @param createReqVO Data integration task extended data info
     * @return Data integration task extended data ID
     */
    Long createDppEtlTaskExt(DppEtlTaskExtSaveReqVO createReqVO);

    /**
     * Update data integration task extended data
     *
     * @param updateReqVO Data integration task extended data info
     */
    int updateDppEtlTaskExt(DppEtlTaskExtSaveReqVO updateReqVO);

    /**
     * Delete data integration task extended data
     *
     * @param idList Data integration task extended data ID list
     */
    int removeDppEtlTaskExt(Collection<Long> idList);

    /**
     * Get data integration task extended data detail
     *
     * @param id Data integration task extended data ID
     * @return Data integration task extended data
     */
    DppEtlTaskExtDO getDppEtlTaskExtById(Long id);

    /**
     * Get all data integration task extended data list
     *
     * @return Data integration task extended data list
     */
    List<DppEtlTaskExtDO> getDppEtlTaskExtList();

    /**
     * Get all data integration task extended data Map
     *
     * @return Data integration task extended data Map
     */
    Map<Long, DppEtlTaskExtDO> getDppEtlTaskExtMap();


    /**
     * Import data integration task extended data
     *
     * @param importExcelList Data integration task extended data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName        Operator
     * @return Result
     */
    String importDppEtlTaskExt(List<DppEtlTaskExtRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Get info by task ID
     *
     * @param taskId
     * @return
     */
    DppEtlTaskExtDO getByTaskId(Long taskId);

}
