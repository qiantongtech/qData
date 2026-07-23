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
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelColumnDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Logical Model Column/Property Information Service Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpModelColumnService extends IService<DpModelColumnDO> {

    /**
     * Get Logical Model Column/Property Information Paginated List
     *
     * @param pageReqVO Pagination Request
     * @return Logical Model Column/Property Information Paginated List
     */
    PageResult<DpModelColumnDO> getDpModelColumnPage(DpModelColumnPageReqVO pageReqVO);

    /**
     * Create Logical Model Column/Property Information
     *
     * @param createReqVO Logical Model Column/Property Information
     * @return Logical Model Column/Property Information ID
     */
    Long createDpModelColumn(DpModelColumnSaveReqVO createReqVO);

    /**
     * Update Logical Model Column/Property Information
     *
     * @param updateReqVO Logical Model Column/Property Information
     */
    int updateDpModelColumn(DpModelColumnSaveReqVO updateReqVO);

    /**
     * Delete Logical Model Column/Property Information
     *
     * @param idList Logical Model Column/Property Information ID
     */
    int removeDpModelColumn(Collection<Long> idList);

    /**
     * Batch Delete Logical Model Column/Property Information
     *
     * @param modelIdList Logical Model ID
     */
    int removeDpModelColumnByModelId(Collection<Long> modelIdList);

    /**
     * Get Logical Model Column/Property Information Details
     *
     * @param id Logical Model Column/Property Information ID
     * @return Logical Model Column/Property Information
     */
    DpModelColumnDO getDpModelColumnById(Long id);

    /**
     * Get All Logical Model Column/Property Information List
     *
     * @return Logical Model Column/Property Information List
     */
    List<DpModelColumnDO> getDpModelColumnList();
    List<DpModelColumnDO> getDpModelColumnList(DpModelColumnSaveReqVO createReqVO);

    long countByDpModelColumn(DpModelColumnSaveReqVO createReqVO);

    /**
     * Get All Logical Model Column/Property Information Map
     *
     * @return Logical Model Column/Property Information Map
     */
    Map<Long, DpModelColumnDO> getDpModelColumnMap();


    /**
     * Import Logical Model Column/Property Information Data
     *
     * @param importExcelList Logical Model Column/Property Information Data List
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName Operator
     * @return Result
     */
    String importDpModelColumn(List<DpModelColumnRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Batch Insert Logical Model Column/Property Information Data
     *
     * @param dpModelColumnList Logical Model Column/Property Information Data List
     * @return Result
     */
    Boolean createDpModelColumnList(List<DpModelColumnSaveReqVO> dpModelColumnList);

    /**
     * Batch Update and Insert Logical Model Column/Property Information Data
     *
     * @param dpModelColumnList Logical Model Column/Property Information Data List
     * @return Result
     */
    Boolean updateDpModelColumnList(List<DpModelColumnSaveReqVO> dpModelColumnList);
}
