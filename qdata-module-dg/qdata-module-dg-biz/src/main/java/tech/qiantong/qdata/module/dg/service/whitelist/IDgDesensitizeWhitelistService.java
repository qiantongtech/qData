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
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeWhitelistDO;
/**
 * Desensitize Whitelist Service Interface
 *
 * @author qdata
 * @date 2026-04-09
 */
public interface IDgDesensitizeWhitelistService extends IService<DgDesensitizeWhitelistDO> {

    /**
     * Get desensitize whitelist paginated list
     *
     * @param pageReqVO Pagination request
     * @return Desensitize whitelist paginated list
     */
    PageResult<DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistPage(DgDesensitizeWhitelistPageReqVO pageReqVO);

    /**
     * Create desensitize whitelist
     *
     * @param createReqVO Desensitize whitelist information
     * @return Desensitize whitelist ID
     */
    Long createDgDesensitizeWhitelist(DgDesensitizeWhitelistSaveReqVO createReqVO);

    /**
     * Update desensitize whitelist
     *
     * @param updateReqVO Desensitize whitelist information
     */
    int updateDgDesensitizeWhitelist(DgDesensitizeWhitelistSaveReqVO updateReqVO);

    /**
     * Delete desensitize whitelist
     *
     * @param idList Desensitize whitelist IDs
     */
    int removeDgDesensitizeWhitelist(Collection<Long> idList);

    /**
     * Get desensitize whitelist details
     *
     * @param id Desensitize whitelist ID
     * @return Desensitize whitelist
     */
    DgDesensitizeWhitelistDO getDgDesensitizeWhitelistById(Long id);

    /**
     * Get all desensitize whitelist list
     *
     * @return Desensitize whitelist list
     */
    List<DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistList();

    /**
     * Get all desensitize whitelist Map
     *
     * @return Desensitize whitelist Map
     */
    Map<Long, DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistMap();


    /**
     * Import desensitize whitelist data
     *
     * @param importExcelList Desensitize whitelist data list
     * @param isUpdateSupport Whether to update support, if already exists, update the data
     * @param operName        Operator user
     * @return Result
     */
    String importDgDesensitizeWhitelist(List<DgDesensitizeWhitelistRespVO> importExcelList, boolean isUpdateSupport, String operName);

    // Query desensitize whitelist by category ID
    DgDesensitizeWhitelistDO getDgDesensitizeWhitelistByCategoryId(Long categoryId);
}
