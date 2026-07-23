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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDocumentCatDO;

import java.util.List;
import java.util.Map;

/**
 * Standard Information Category Management Service Interface
 *
 * @author qdata
 * @date 2025-08-21
 */
public interface IAttDocumentCatService extends IService<AttDocumentCatDO> {

    /**
     * Get Standard Information Category Management paginated list
     *
     * @param pageReqVO Pagination request
     * @return Standard Information Category Management paginated list
     */
    PageResult<AttDocumentCatDO> getAttDocumentCatPage(AttDocumentCatPageReqVO pageReqVO);

    /**
     * Get all Standard Information Category Management list
     *
     * @param pageReqVO Pagination request
     * @return Standard Information Category Management list
     */
    List<AttDocumentCatDO> getAttDocumentCatList(AttDocumentCatPageReqVO pageReqVO);

    /**
     * Create Standard Information Category Management
     *
     * @param createReqVO Standard Information Category Management info
     * @return Standard Information Category Management ID
     */
    Long createAttDocumentCat(AttDocumentCatSaveReqVO createReqVO);

    /**
     * Update Standard Document Category Management
     *
     * @param updateReqVO Standard Document Category Management info
     */
    int updateAttDocumentCat(AttDocumentCatSaveReqVO updateReqVO);

    /**
     * Delete Standard Document Category Management
     *
     * @param id Standard Document Category Management ID
     */
    int removeAttDocumentCat(Long id);

    /**
     * Get Standard Document Category Management details
     *
     * @param id Standard Document Category Management ID
     * @return Standard Document Category Management
     */
    AttDocumentCatDO getAttDocumentCatById(Long id);

    /**
     * Get all Standard Document Category Management list
     *
     * @return Standard Document Category Management list
     */
    List<AttDocumentCatDO> getAttDocumentCatList();

    /**
     * Get all Standard Document Category Management Map
     *
     * @return Standard Document Category Management Map
     */
    Map<Long, AttDocumentCatDO> getAttDocumentCatMap();

    /**
     * Check if Standard Document Category Management has child nodes
     *
     * @param id Standard Document Category Management ID
     * @return true if exists, false otherwise
     */
    boolean hasChildByAttDocumentCatId(Long id);

    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);


}
