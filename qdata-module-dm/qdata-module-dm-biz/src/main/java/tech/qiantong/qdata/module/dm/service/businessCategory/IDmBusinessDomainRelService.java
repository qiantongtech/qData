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

package tech.qiantong.qdata.module.dm.service.businessCategory;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessDomainRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Business Category Data Domain Relation Service Interface
 *
 * @author qdata
 * @date 2026-04-12
 */
public interface IDmBusinessDomainRelService extends IService<DmBusinessDomainRelDO> {

    /**
     * Get business category data domain relation page list
     *
     * @param pageReqVO Page request
     * @return Business category data domain relation page list
     */
    PageResult<DmBusinessDomainRelDO> getDmBusinessDomainRelPage(DmBusinessDomainRelPageReqVO pageReqVO);

    /**
     * Create business category data domain relation
     *
     * @param createReqVO Business category data domain relation information
     * @return Business category data domain relation ID
     */
    Long createDmBusinessDomainRel(DmBusinessDomainRelSaveReqVO createReqVO);

    /**
     * Update business category data domain relation
     *
     * @param updateReqVO Business category data domain relation information
     */
    int updateDmBusinessDomainRel(DmBusinessDomainRelSaveReqVO updateReqVO);

    /**
     * Delete business category data domain relation
     *
     * @param idList Business category data domain relation IDs
     */
    int removeDmBusinessDomainRel(Collection<Long> idList);

    /**
     * Get business category data domain relation details
     *
     * @param id Business category data domain relation ID
     * @return Business category data domain relation
     */
    DmBusinessDomainRelDO getDmBusinessDomainRelById(Long id);

    /**
     * Get all business category data domain relations
     *
     * @return Business category data domain relation list
     */
    List<DmBusinessDomainRelDO> getDmBusinessDomainRelList();

    /**
     * Get all business category data domain relations as Map
     *
     * @return Business category data domain relation Map
     */
    Map<Long, DmBusinessDomainRelDO> getDmBusinessDomainRelMap();


    /**
     * Import business category data domain relation data
     *
     * @param importExcelList Business category data domain relation data list
     * @param isUpdateSupport Whether to support update, if exists, update the data
     * @param operName Operation user
     * @return Result
     */
    String importDmBusinessDomainRel(List<DmBusinessDomainRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    Integer removeDmBusinessDomainRelByDomainId(Long domainId, Long businessCategoryId);
}
