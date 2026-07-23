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

import java.util.List;
import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainPageReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmThemeDomainDO;

/**
 * Theme Domain Service Interface
 *
 * @author FXB
 * @date 2026-03-24
 */
public interface IDmThemeDomainService extends IService<DmThemeDomainDO> {

    /**
     * Get theme domain page list
     *
     * @param pageReqVO Page request
     * @return Theme domain page list
     */
    PageResult<DmThemeDomainDO> getDmThemeDomainPage(DmThemeDomainPageReqVO pageReqVO);

    /**
     * Create theme domain
     *
     * @param createReqVO Theme domain information
     * @return Theme domain ID
     */
    Long createDmThemeDomain(DmThemeDomainSaveReqVO createReqVO);

    /**
     * Update theme domain
     *
     * @param updateReqVO Theme domain information
     */
    int updateDmThemeDomain(DmThemeDomainSaveReqVO updateReqVO);

    /**
     * Delete theme domain
     *
     * @param idList Theme domain IDs
     */
    int removeDmThemeDomain(Collection<Long> idList);

    /**
     * Get theme domain details
     *
     * @param id Theme domain ID
     * @return Theme domain
     */
    DmThemeDomainDO getDmThemeDomainById(Long id);

    /**
     * Get all theme domains
     *
     * @return Theme domain list
     */
    List<DmThemeDomainDO> getDmThemeDomainList();

    /**
     * Get all theme domains as Map
     *
     * @return Theme domain Map
     */
    Map<Long, DmThemeDomainDO> getDmThemeDomainMap();


    /**
     * Import theme domain data
     *
     * @param importExcelList Theme domain data list
     * @param isUpdateSupport Whether to support update, if exists, update the data
     * @param operName        Operation user
     * @return Result
     */
    String importDmThemeDomain(List<DmThemeDomainRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Get theme domain list
     *
     * @param reqVO
     * @return
     */
    List<DmThemeDomainDO> getDmThemeDomainList(DmThemeDomainPageReqVO reqVO);

    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

    /**
     * Update all codes under the specified parent ID
     *
     * @param pid
     */
    void changeCodeByPid(Long pid, String parentCode);
}
