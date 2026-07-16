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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttCleanCatDO;

import java.util.List;
import java.util.Map;

/**
 * Cleaning Rule Category Service Interface
 *
 * @author qdata
 * @date 2025-08-11
 */
public interface IAttCleanCatService extends IService<AttCleanCatDO> {

    /**
     * Get Cleaning Rule Category paginated list
     *
     * @param pageReqVO Page request
     *  Cleaning Rule Category paginated list
     */
    PageResult<AttCleanCatDO> getAttCleanCatPage(AttCleanCatPageReqVO pageReqVO);

    /**
     * Create Cleaning Rule Category
     *
     * @param createReqVO Cleaning Rule Category info
     *  Cleaning Rule Category ID
     */
    Long createAttCleanCat(AttCleanCatSaveReqVO createReqVO);

    /**
     * Update Cleaning Rule Category
     *
     * @param updateReqVO Cleaning Rule Category info
     */
    int updateAttCleanCat(AttCleanCatSaveReqVO updateReqVO);

    /**
     * Delete Cleaning Rule Category
     *
     * @param idList Cleaning Rule Category ID
     */
    int removeAttCleanCat(Long idList);

    /**
     * Get Cleaning Rule Category details
     *
     * @param id Cleaning Rule Category ID
     * @return Cleaning Rule Category
     */
    AttCleanCatDO getAttCleanCatById(Long id);

    /**
     * Get all Cleaning Rule Category list
     *
     * @return Cleaning Rule Category list
     */
    List<AttCleanCatDO> getAttCleanCatList(AttCleanCatPageReqVO attCleanCat);
    List<AttCleanCatDO> getAttCleanCatList();

    /**
     * Get all Cleaning Rule Category Map
     *
     * @return Cleaning Rule Category Map
     */
    Map<Long, AttCleanCatDO> getAttCleanCatMap();


    /**
     * Import Cleaning Rule Category data
     *
     * @param importExcelList Cleaning Rule Category data list
     * @param isUpdateSupport Whether to support update, if already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importAttCleanCat(List<AttCleanCatRespVO> importExcelList, boolean isUpdateSupport, String operName);


    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

}
