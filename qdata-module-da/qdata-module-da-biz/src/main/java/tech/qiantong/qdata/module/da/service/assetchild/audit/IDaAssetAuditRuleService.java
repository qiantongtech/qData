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

package tech.qiantong.qdata.module.da.service.assetchild.audit;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRulePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditRuleDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Asset Quality Result Record Service Interface
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface IDaAssetAuditRuleService extends IService<DaAssetAuditRuleDO> {

    /**
     * Get data asset quality result record page list
     *
     * @param pageReqVO page request
     * @return data asset quality result record page list
     */
    PageResult<DaAssetAuditRuleDO> getDaAssetAuditRulePage(DaAssetAuditRulePageReqVO pageReqVO);

    /**
     * Create data asset quality result record
     *
     * @param createReqVO data asset quality result record info
     * @return data asset quality result record ID
     */
    Long createDaAssetAuditRule(DaAssetAuditRuleSaveReqVO createReqVO);

    /**
     * Update data asset quality result record
     *
     * @param updateReqVO data asset quality result record info
     */
    int updateDaAssetAuditRule(DaAssetAuditRuleSaveReqVO updateReqVO);

    /**
     * Delete data asset quality result record
     *
     * @param idList data asset quality result record ID list
     */
    int removeDaAssetAuditRule(Collection<Long> idList);

    /**
     * Get data asset quality result record details
     *
     * @param id data asset quality result record ID
     * @return data asset quality result record
     */
    DaAssetAuditRuleDO getDaAssetAuditRuleById(Long id);

    /**
     * Get all data asset quality result record list
     *
     * @return data asset quality result record list
     */
    List<DaAssetAuditRuleDO> getDaAssetAuditRuleList();

    /**
     * Get all data asset quality result record Map
     *
     * @return data asset quality result record Map
     */
    Map<Long, DaAssetAuditRuleDO> getDaAssetAuditRuleMap();


    /**
     * Import data asset quality result record data
     *
     * @param importExcelList data asset quality result record data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetAuditRule(List<DaAssetAuditRuleRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
