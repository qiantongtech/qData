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

package tech.qiantong.qdata.module.att.service.rule;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttCleanCatDO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttCleanRuleDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Cleaning Rule Service Interface
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IAttCleanRuleService extends IService<AttCleanRuleDO> {

    /**
     * Get Cleaning Rule paginated list
     *
     * @param pageReqVO Page request
     * @return Cleaning Rule paginated list
     */
    PageResult<AttCleanRuleDO> getAttCleanRulePage(AttCleanRulePageReqVO pageReqVO);

    /**
     * Create Cleaning Rule
     *
     * @param createReqVO Cleaning Rule info
     * @return Cleaning Rule ID
     */
    Long createAttCleanRule(AttCleanRuleSaveReqVO createReqVO);

    /**
     * Update Cleaning Rule
     *
     * @param updateReqVO Cleaning Rule info
     */
    int updateAttCleanRule(AttCleanRuleSaveReqVO updateReqVO);

    /**
     * Delete Cleaning Rule
     *
     * @param idList Cleaning Rule ID list
     */
    int removeAttCleanRule(Collection<Long> idList);

    /**
     * Get Cleaning Rule details
     *
     * @param id Cleaning Rule ID
     * @return Cleaning Rule
     */
    AttCleanRuleDO getAttCleanRuleById(Long id);

    /**
     * Get all Cleaning Rule list
     *
     * @return Cleaning Rule list
     */
    List<AttCleanRuleDO> getAttCleanRuleList();
    List<AttCleanRuleRespVO> getAttCleanRuleList(AttCleanRulePageReqVO attCleanRule);

    /**
     * Get all Cleaning Rule Map
     *
     * @return Cleaning Rule Map
     */
    Map<Long, AttCleanRuleDO> getAttCleanRuleMap();

    /**
     * Import Cleaning Rule data
     *
     * @param importExcelList Cleaning Rule data list
     * @param isUpdateSupport Whether update is supported; if already exists, update the data
     * @param operName        Operator
     * @return Result
     */
    String importAttCleanRule(List<AttCleanRuleRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Get Cleaning Rule tree structure
     *
     * @return Tree structure list
     */
    List<AttCleanRuleRespVO> getAttCleanRuleTree(Long dataElemId);

    List<AttCleanRuleRespVO> getCleaningRuleTree(Long[] dataElemId);

    /**
     * @param catCode {@link AttCleanCatDO#code}
     */
    Long getCount(String catCode);

}
