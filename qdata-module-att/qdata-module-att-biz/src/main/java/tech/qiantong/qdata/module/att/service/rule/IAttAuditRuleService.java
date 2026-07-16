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
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRuleSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Audit Rule Service Interface
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IAttAuditRuleService extends IService<AttAuditRuleDO> {

    /**
     * Get audit rule paginated list
     *
     * @param pageReqVO Page request
     *  Audit rule paginated list
     */
    PageResult<AttAuditRuleDO> getAttAuditRulePage(AttAuditRulePageReqVO pageReqVO);

    /**
     * Create audit rule
     *
     * @param createReqVO Audit rule info
     *  Audit rule ID
     */
    Long createAttAuditRule(AttAuditRuleSaveReqVO createReqVO);

    /**
     * Update audit rule
     *
     * @param updateReqVO Audit rule info
     */
    int updateAttAuditRule(AttAuditRuleSaveReqVO updateReqVO);

    /**
     * Delete audit rule
     *
     * @param idList Audit rule ID list
     */
    int removeAttAuditRule(Collection<Long> idList);

    /**
     * Get audit rule details
     *
     * @param id Audit rule ID
     * @return Audit rule
     */
    AttAuditRuleDO getAttAuditRuleById(Long id);

    /**
     * Get all audit rule list
     *
     * @return Audit rule list
     */
    List<AttAuditRuleDO> getAttAuditRuleList();

    /**
     * Get all audit rule Map
     *
     * @return Audit rule Map
     */
    Map<Long, AttAuditRuleDO> getAttAuditRuleMap();

    /**
     * Import audit rule data
     *
     * @param importExcelList Audit rule data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importAttAuditRule(List<AttAuditRuleRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Get audit rule tree structure
     *
     * @param dataElemId Data element ID
     * @return Tree structure list
     */
    List<AttAuditRuleRespVO> getAttAuditRuleTree(Long dataElemId);
}
