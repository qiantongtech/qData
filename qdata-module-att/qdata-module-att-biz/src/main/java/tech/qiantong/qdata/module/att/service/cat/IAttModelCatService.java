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

package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttModelCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Logical Model Category Management Service Interface
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IAttModelCatService extends IService<AttModelCatDO> {

    /**
     * Get Logical Model Category Management paginated list
     *
     * @param pageReqVO Pagination request
     * @return Logical Model Category Management paginated list
     */
    PageResult<AttModelCatDO> getAttModelCatPage(AttModelCatPageReqVO pageReqVO);

    /**
     * Create Logical Model Category Management
     *
     * @param createReqVO Logical Model Category Management info
     * @return Logical Model Category Management ID
     */
    Long createAttModelCat(AttModelCatSaveReqVO createReqVO);

    /**
     * Update Logical Model Category Management
     *
     * @param updateReqVO Logical Model Category Management info
     */
    int updateAttModelCat(AttModelCatSaveReqVO updateReqVO);

    /**
     * Delete Logical Model Category Management
     *
     * @param idList Logical Model Category Management ID list
     */
    int removeAttModelCat(Collection<Long> idList);
    int removeAttModelCat(Long id);

    /**
     * Get Logical Model Category Management details
     *
     * @param id Logical Model Category Management ID
     * @return Logical Model Category Management
     */
    AttModelCatDO getAttModelCatById(Long id);

    /**
     * Get all Logical Model Category Management list
     *
     * @return Logical Model Category Management list
     */
    List<AttModelCatDO> getAttModelCatList();

    /**
     * Get all Logical Model Category Management list
     *
     * @param reqVO Page request
     * @return Logical Model Category Management list
     */
    List<AttModelCatDO> getAttModelCatList(AttModelCatPageReqVO reqVO);

    /**
     * Get all Logical Model Category Management Map
     *
     * @return Logical Model Category Management Map
     */
    Map<Long, AttModelCatDO> getAttModelCatMap();


    /**
     * Import Logical Model Category Management data
     *
     * @param importExcelList Logical Model Category Management data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importAttModelCat(List<AttModelCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);
}
