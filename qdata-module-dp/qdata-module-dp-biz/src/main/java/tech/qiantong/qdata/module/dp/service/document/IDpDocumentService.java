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

package tech.qiantong.qdata.module.dp.service.document;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.document.vo.*;
import tech.qiantong.qdata.module.dp.dal.dataobject.document.DpDocumentDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Standard Document Registration Service Interface
 *
 * @author qdata
 * @date 2025-08-21
 */
public interface IDpDocumentService extends IService<DpDocumentDO> {

    /**
     * Get Standard Document Registration Paginated List
     *
     * @param pageReqVO Pagination Request
     * @return Standard Document Registration Paginated List
     */
    PageResult<DpDocumentDO> getDpDocumentPage(DpDocumentPageReqVO pageReqVO);

    /**
     * Get All Standard Document Registration List
     *
     * @return Standard Document Registration List
     */
    List<DpDocumentDO> getDpDocumentList(DpDocumentPageReqVO pageReqVO);

    /**
     * Create Standard Document Registration
     *
     * @param createReqVO Standard Document Registration Information
     * @return Standard Document Registration ID
     */
    Long createDpDocument(DpDocumentSaveReqVO createReqVO);

    /**
     * Update Standard Document Registration
     *
     * @param updateReqVO Standard Document Registration Information
     */
    int updateDpDocument(DpDocumentSaveReqVO updateReqVO);

    /**
     * Delete Standard Document Registration
     *
     * @param idList Standard Document Registration ID
     */
    int removeDpDocument(Collection<Long> idList);

    /**
     * Get Standard Document Registration Details
     *
     * @param id Standard Document Registration ID
     * @return Standard Document Registration
     */
    DpDocumentDO getDpDocumentById(Long id);

    /**
     * Get All Standard Document Registration List
     *
     * @return Standard Document Registration List
     */
    List<DpDocumentDO> getDpDocumentList();

    /**
     * Get All Standard Document Registration Map
     *
     * @return Standard Document Registration Map
     */
    Map<Long, DpDocumentDO> getDpDocumentMap();


    /**
     * Import Standard Document Registration Data
     *
     * @param importExcelList Standard Document Registration Data List
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName Operator
     * @return Result
     */
    String importDpDocument(List<DpDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Standard Search Paginated List
     *
     * @param dpDocument
     * @return
     */
    PageResult<DpDocumentSearchRespVO> getDpDocumentSearchPage(DpDocumentSearchReqVO dpDocument);
}
