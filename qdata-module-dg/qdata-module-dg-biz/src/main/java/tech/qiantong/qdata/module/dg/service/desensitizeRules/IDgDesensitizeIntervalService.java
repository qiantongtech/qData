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

package tech.qiantong.qdata.module.dg.service.desensitizeRules;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeIntervalDO;
/**
 * Desensitize Interval Service Interface
 *
 * @author qdata
 * @date 2026-04-10
 */
public interface IDgDesensitizeIntervalService extends IService<DgDesensitizeIntervalDO> {

    /**
     * Get desensitize interval paginated list
     *
     * @param pageReqVO Pagination request
     * @return Desensitize interval paginated list
     */
    PageResult<DgDesensitizeIntervalDO> getDgDesensitizeIntervalPage(DgDesensitizeIntervalPageReqVO pageReqVO);

    /**
     * Create desensitize interval
     *
     * @param createReqVO Desensitize interval information
     * @return Desensitize interval ID
     */
    Long createDgDesensitizeInterval(DgDesensitizeIntervalSaveReqVO createReqVO);

    /**
     * Update desensitize interval
     *
     * @param updateReqVO Desensitize interval information
     */
    int updateDgDesensitizeInterval(DgDesensitizeIntervalSaveReqVO updateReqVO);

    /**
     * Delete desensitize interval
     *
     * @param idList Desensitize interval IDs
     */
    int removeDgDesensitizeInterval(Collection<Long> idList);

    /**
     * Get desensitize interval details
     *
     * @param id Desensitize interval ID
     * @return Desensitize interval
     */
    DgDesensitizeIntervalDO getDgDesensitizeIntervalById(Long id);

    /**
     * Get all desensitize interval list
     *
     * @return Desensitize interval list
     */
    List<DgDesensitizeIntervalDO> getDgDesensitizeIntervalList();

    /**
     * Get all desensitize interval Map
     *
     * @return Desensitize interval Map
     */
    Map<Long, DgDesensitizeIntervalDO> getDgDesensitizeIntervalMap();


    /**
     * Import desensitize interval data
     *
     * @param importExcelList Desensitize interval data list
     * @param isUpdateSupport Whether to update support, if already exists, update the data
     * @param operName        Operator user
     * @return Result
     */
    String importDgDesensitizeInterval(List<DgDesensitizeIntervalRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
