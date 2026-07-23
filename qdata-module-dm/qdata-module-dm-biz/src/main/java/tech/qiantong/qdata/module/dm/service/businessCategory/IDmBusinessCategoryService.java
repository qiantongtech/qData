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

package tech.qiantong.qdata.module.dm.service.businessCategory;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategorySaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessCategoryDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Business Category Service Interface
 *
 * @author qdata
 * @date 2026-04-08
 */
public interface IDmBusinessCategoryService extends IService<DmBusinessCategoryDO> {

    /**
     * Get business category page list
     *
     * @param pageReqVO Page request
     * @return Business category page list
     */
    PageResult<DmBusinessCategoryDO> getDmBusinessCategoryPage(DmBusinessCategoryPageReqVO pageReqVO);

    /**
     * Create business category
     *
     * @param createReqVO Business category information
     * @return Business category ID
     */
    Long createDmBusinessCategory(DmBusinessCategorySaveReqVO createReqVO);

    /**
     * Update business category
     *
     * @param updateReqVO Business category information
     */
    int updateDmBusinessCategory(DmBusinessCategorySaveReqVO updateReqVO);

    /**
     * Delete business category
     *
     * @param idList Business category IDs
     */
    int removeDmBusinessCategory(Collection<Long> idList);

    /**
     * Get business category details
     *
     * @param id Business category ID
     * @return Business category
     */
    DmBusinessCategoryDO getDmBusinessCategoryById(Long id);

    /**
     * Get all business categories
     *
     * @return Business category list
     */
    List<DmBusinessCategoryDO> getDmBusinessCategoryList(DmBusinessCategoryPageReqVO dmBusinessCategory);

    /**
     * Get all business categories as Map
     *
     * @return Business category Map
     */
    Map<Long, DmBusinessCategoryDO> getDmBusinessCategoryMap();


    /**
     * Import business category data
     *
     * @param importExcelList Business category data list
     * @param isUpdateSupport Whether to support update, if exists, update the data
     * @param operName Operation user
     * @return Result
     */
    String importDmBusinessCategory(List<DmBusinessCategoryRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

    /**
     * Update all codes under the specified parent ID
     *
     * @param pid
     */
    void changeCodeByPid(Long pid, String parentCode);
}
