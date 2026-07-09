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
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRuleRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRuleSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRulePageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeRuleDO;
/**
 * Desensitize Rule Service Interface
 *
 * @author qdata
 * @date 2026-04-10
 */
public interface IDgDesensitizeRuleService extends IService<DgDesensitizeRuleDO> {

    /**
     * Get desensitize rule paginated list
     *
     * @param pageReqVO Pagination request
     * @return Desensitize rule paginated list
     */
    PageResult<DgDesensitizeRuleDO> getDgDesensitizeRulePage(DgDesensitizeRulePageReqVO pageReqVO);

    /**
     * Create desensitize rule
     *
     * @param createReqVO Desensitize rule information
     * @return Desensitize rule ID
     */
    Long createDgDesensitizeRule(DgDesensitizeRuleSaveReqVO createReqVO);

    /**
     * Update desensitize rule
     *
     * @param updateReqVO Desensitize rule information
     */
    int updateDgDesensitizeRule(DgDesensitizeRuleSaveReqVO updateReqVO);

    /**
     * Delete desensitize rule
     *
     * @param idList Desensitize rule IDs
     */
    int removeDgDesensitizeRule(Collection<Long> idList);

    /**
     * Get desensitize rule details
     *
     * @param id Desensitize rule ID
     * @return Desensitize rule
     */
    DgDesensitizeRuleDO getDgDesensitizeRuleById(Long id);

    /**
     * Get all desensitize rule list
     *
     * @return Desensitize rule list
     */
    List<DgDesensitizeRuleDO> getDgDesensitizeRuleList();

    /**
     * Get all desensitize rule Map
     *
     * @return Desensitize rule Map
     */
    Map<Long, DgDesensitizeRuleDO> getDgDesensitizeRuleMap();


    /**
     * Import desensitize rule data
     *
     * @param importExcelList Desensitize rule data list
     * @param isUpdateSupport Whether to update support, if already exists, update the data
     * @param operName        Operator user
     * @return Result
     */
    String importDgDesensitizeRule(List<DgDesensitizeRuleRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Get rule count by category IDs
     *
     * @param idList Category ID array
     * @return Rule count
     */
    Long getCountByCategoryIds(Collection<Long> idList);
    DgDesensitizeRuleDO getDgDesensitizeRuleByDataCategoryId(Long dataCategoryId);
}
