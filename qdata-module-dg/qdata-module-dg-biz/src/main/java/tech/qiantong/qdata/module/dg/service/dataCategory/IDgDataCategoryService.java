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

package tech.qiantong.qdata.module.dg.service.dataCategory;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategoryRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategorySaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategoryPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategoryTreeRespVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategory.DgDataCategoryDO;

/**
 * Data Category Service Interface
 *
 * @author qdata
 * @date 2026-04-07
 */
public interface IDgDataCategoryService extends IService<DgDataCategoryDO> {

    /**
     * Get data category paginated list
     *
     * @param pageReqVO Pagination request
     * @return Data category paginated list
     */
    PageResult<DgDataCategoryDO> getDgDataCategoryPage(DgDataCategoryPageReqVO pageReqVO);

    /**
     * Create data category
     *
     * @param createReqVO Data category information
     * @return Data category ID
     */
    Long createDgDataCategory(DgDataCategorySaveReqVO createReqVO);

    /**
     * Update data category
     *
     * @param updateReqVO Data category information
     */
    int updateDgDataCategory(DgDataCategorySaveReqVO updateReqVO);

    /**
     * Delete data category
     *
     * @param idList Data category IDs
     */
    int removeDgDataCategory(Collection<Long> idList);

    /**
     * Get data category details
     *
     * @param id Data category ID
     * @return Data category
     */
    DgDataCategoryDO getDgDataCategoryById(Long id);

    /**
     * Get all data category list
     *
     * @return Data category list
     */
    List<DgDataCategoryDO> getDgDataCategoryList();


    /**
     * Get all data category Map
     *
     * @return Data category Map
     */
    Map<Long, DgDataCategoryDO> getDgDataCategoryMap();


    /**
     * Import data category data
     *
     * @param importExcelList Data category data list
     * @param isUpdateSupport Whether to update support, if exists then update data
     * @param operName        Operator user
     * @return Result
     */
    String importDgDataCategory(List<DgDataCategoryRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Get data category tree list
     *
     * @return Tree list
     */
    List<DgDataCategoryTreeRespVO> selectTree(String type);

    /**
     * Get count by category code
     *
     * @param catCode
     * @return
     */
    Long getCountByCatCode(String catCode);

    /**
     * Batch update old CAT_CODE to new CAT_CODE
     *
     * @param oldCatCode Old category code
     * @param newCatCode New category code
     * @return Number of affected rows
     */
    int updateCatCode(String codeOld, String codeNew);

    List<DgDataCategoryDO> getDgDataCategoryList(DgDataCategoryPageReqVO dgDataCategory);
}
