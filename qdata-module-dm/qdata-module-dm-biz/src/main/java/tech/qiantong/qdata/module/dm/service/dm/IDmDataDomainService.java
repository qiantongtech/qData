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

package tech.qiantong.qdata.module.dm.service.dm;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Domain Service Interface
 *
 * @author FXB
 * @date 2026-03-24
 */
public interface IDmDataDomainService extends IService<DmDataDomainDO> {

    /**
     * Get data domain page list
     *
     * @param pageReqVO Page request
     * @return Data domain page list
     */
    PageResult<DmDataDomainDO> getDmDataDomainPage(DmDataDomainPageReqVO pageReqVO);

    /**
     * Create data domain
     *
     * @param createReqVO Data domain information
     * @return Data domain ID
     */
    Long createDmDataDomain(DmDataDomainSaveReqVO createReqVO);

    /**
     * Update data domain
     *
     * @param updateReqVO Data domain information
     */
    int updateDmDataDomain(DmDataDomainSaveReqVO updateReqVO);

    /**
     * Delete data domain
     *
     * @param idList Data domain IDs
     */
    int removeDmDataDomain(Collection<Long> idList);

    /**
     * Get data domain details
     *
     * @param id Data domain ID
     * @return Data domain
     */
    DmDataDomainDO getDmDataDomainById(Long id);

    /**
     * Get all data domains
     *
     * @return Data domain list
     */
    List<DmDataDomainDO> getDmDataDomainList();

    /**
     * Get all data domains as Map
     *
     * @return Data domain Map
     */
    Map<Long, DmDataDomainDO> getDmDataDomainMap();


    /**
     * Import data domain data
     *
     * @param importExcelList Data domain data list
     * @param isUpdateSupport Whether to support update, if exists, update the data
     * @param operName Operation user
     * @return Result
     */
    String importDmDataDomain(List<DmDataDomainRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Query data domain by business category ID
     *
     * @param dmDataDomain
     * @return
     */
    PageResult<DmDataDomainDO> getDmDataDomainByCategoryId(DmDataDomainPageReqVO dmDataDomain);
}
