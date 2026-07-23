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

package tech.qiantong.qdata.module.dp.service.model;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpMaterializedMethodReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelMaterializedDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Materialized Model Record Service Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpModelMaterializedService extends IService<DpModelMaterializedDO> {

    /**
     * Get Materialized Model Record Paginated List
     *
     * @param pageReqVO Pagination Request
     * @return Materialized Model Record Paginated List
     */
    PageResult<DpModelMaterializedDO> getDpModelMaterializedPage(DpModelMaterializedPageReqVO pageReqVO);

    /**
     * Create Materialized Model Record
     *
     * @param createReqVO Materialized Model Record Information
     * @return Materialized Model Record ID
     */
    Long createDpModelMaterialized(DpModelMaterializedSaveReqVO createReqVO);

    /**
     * Update Materialized Model Record
     *
     * @param updateReqVO Materialized Model Record Information
     */
    int updateDpModelMaterialized(DpModelMaterializedSaveReqVO updateReqVO);

    /**
     * Delete Materialized Model Record
     *
     * @param idList Materialized Model Record ID
     */
    int removeDpModelMaterialized(Collection<Long> idList);

    /**
     * Get Materialized Model Record Details
     *
     * @param id Materialized Model Record ID
     * @return Materialized Model Record
     */
    DpModelMaterializedDO getDpModelMaterializedById(Long id);

    /**
     * Get All Materialized Model Record List
     *
     * @return Materialized Model Record List
     */
    List<DpModelMaterializedDO> getDpModelMaterializedList();

    /**
     * Get All Materialized Model Record Map
     *
     * @return Materialized Model Record Map
     */
    Map<Long, DpModelMaterializedDO> getDpModelMaterializedMap();


    /**
     * Import Materialized Model Record Data
     *
     * @param importExcelList Materialized Model Record Data List
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName        Operator
     * @return Result
     */
    String importDpModelMaterialized(List<DpModelMaterializedRespVO> importExcelList, boolean isUpdateSupport, String operName);

    Long createMaterializedTable(DpMaterializedMethodReqVO dpModelMaterialized);
}
