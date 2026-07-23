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

package tech.qiantong.qdata.module.dg.service.whitelist;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;
/**
 * Desensitize Whitelist User Relationship Service Interface
 *
 * @author qdata
 * @date 2026-04-09
 */
public interface IDgDesensitizeUserRelService extends IService<DgDesensitizeUserRelDO> {

    /**
     * Get desensitize whitelist user relationship paginated list
     *
     * @param pageReqVO Pagination request
     * @return Desensitize whitelist user relationship paginated list
     */
    PageResult<DgDesensitizeUserRelDO> getDgDesensitizeUserRelPage(DgDesensitizeUserRelPageReqVO pageReqVO);

    /**
     * Create desensitize whitelist user relationship
     *
     * @param createReqVO Desensitize whitelist user relationship information
     * @return Desensitize whitelist user relationship ID
     */
    Long createDgDesensitizeUserRel(DgDesensitizeUserRelSaveReqVO createReqVO);

    /**
     * Update desensitize whitelist user relationship
     *
     * @param updateReqVO Desensitize whitelist user relationship information
     */
    int updateDgDesensitizeUserRel(DgDesensitizeUserRelSaveReqVO updateReqVO);

    /**
     * Delete desensitize whitelist user relationship
     *
     * @param idList Desensitize whitelist user relationship IDs
     */
    int removeDgDesensitizeUserRel(Collection<Long> idList);

    /**
     * Get desensitize whitelist user relationship details
     *
     * @param id Desensitize whitelist user relationship ID
     * @return Desensitize whitelist user relationship
     */
    DgDesensitizeUserRelDO getDgDesensitizeUserRelById(Long id);

    /**
     * Get all desensitize whitelist user relationship list
     *
     * @return Desensitize whitelist user relationship list
     */
    List<DgDesensitizeUserRelDO> getDgDesensitizeUserRelList();

    /**
     * Get all desensitize whitelist user relationship Map
     *
     * @return Desensitize whitelist user relationship Map
     */
    Map<Long, DgDesensitizeUserRelDO> getDgDesensitizeUserRelMap();


    /**
     * Import desensitize whitelist user relationship data
     *
     * @param importExcelList Desensitize whitelist user relationship data list
     * @param isUpdateSupport Whether to update support, if already exists, update the data
     * @param operName        Operator user
     * @return Result
     */
    String importDgDesensitizeUserRel(List<DgDesensitizeUserRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
