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

package tech.qiantong.qdata.module.dg.service.dataCategoryCat;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategoryCat.DgDataCategoryCatDO;

/**
 * Data Category - Category Service Interface
 *
 * @author FXB
 * @date 2026-04-07
 */
public interface IDgDataCategoryCatService extends IService<DgDataCategoryCatDO> {

    /**
     * Get data category-category paginated list
     *
     * @param pageReqVO Pagination request
     * @return Data category-category paginated list
     */
    PageResult<DgDataCategoryCatDO> getDgDataCategoryCatPage(DgDataCategoryCatPageReqVO pageReqVO);

    /**
     * Create data category-category
     *
     * @param createReqVO Data category-category information
     * @return Data category-category ID
     */
    Long createDgDataCategoryCat(DgDataCategoryCatSaveReqVO createReqVO);

    /**
     * Update data category-category
     *
     * @param updateReqVO Data category-category information
     */
    int updateDgDataCategoryCat(DgDataCategoryCatSaveReqVO updateReqVO);

    /**
     * Delete data category-category
     *
     * @param idList Data category-category IDs
     */
    int removeDgDataCategoryCat(Collection<Long> idList);

    /**
     * Get data category-category details
     *
     * @param id Data category-category ID
     * @return Data category-category
     */
    DgDataCategoryCatDO getDgDataCategoryCatById(Long id);

    /**
     * Get all data category-category list
     *
     * @return Data category-category list
     */
    List<DgDataCategoryCatDO> getDgDataCategoryCatList();
    /**
     * Get all data category-category Map
     *
     * @return Data category-category Map
     */
    Map<Long, DgDataCategoryCatDO> getDgDataCategoryCatMap();


    /**
     * Import data category-category data
     *
     * @param importExcelList Data category-category data list
     * @param isUpdateSupport Whether to update support, if exists then update data
     * @param operName        Operator user
     * @return Result
     */
    String importDgDataCategoryCat(List<DgDataCategoryCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

    /**
     * Change all codes under the specified pid
     *
     * @param pid
     */
    void changeCodeByPid(Long pid, String parentCode);

}
