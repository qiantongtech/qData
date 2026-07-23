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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttApiCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Service Category Management Service Interface
 *
 * @author qdata
 * @date 2025-03-11
 */
public interface IAttApiCatService extends IService<AttApiCatDO> {

    /**
     * Get Data Service Category Management paginated list
     *
     * @param pageReqVO Page request
     *  Data Service Category Management paginated list
     */
    PageResult<AttApiCatDO> getAttApiCatPage(AttApiCatPageReqVO pageReqVO);

    /**
     * Create Data Service Category Management
     *
     * @param createReqVO Data Service Category Management info
     *  Data Service Category Management ID
     */
    Long createAttApiCat(AttApiCatSaveReqVO createReqVO);

    /**
     * Update Data Service Category Management
     *
     * @param updateReqVO Data Service Category Management info
     */
    int updateAttApiCat(AttApiCatSaveReqVO updateReqVO);

    /**
     * Delete Data Service Category Management
     *
     * @param idList Data Service Category Management ID list
     */
    int removeAttApiCat(Collection<Long> idList);

    /**
     * Get Data Service Category Management details
     *
     * @param id Data Service Category Management ID
     *  Data Service Category Management
     */
    AttApiCatDO getAttApiCatById(Long id);

    /**
     * Get all Data Service Category Management list
     *
     *  Data Service Category Management list
     */
    List<AttApiCatDO> getAttApiCatList();

    /**
     * Get all Data Service Category Management list
     *
     *  Data Service Category Management list
     */
    List<AttApiCatDO> getAttApiCatList(AttApiCatPageReqVO pageReqVO);

    /**
     * Get all Data Service Category Management Map
     *
     *  Data Service Category Management Map
     */
    Map<Long, AttApiCatDO> getAttApiCatMap();


    /**
     * Import Data Service Category Management data
     *
     * @param importExcelList Data Service Category Management data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName Operator
     *  Result
     */
    String importAttApiCat(List<AttApiCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);
}
