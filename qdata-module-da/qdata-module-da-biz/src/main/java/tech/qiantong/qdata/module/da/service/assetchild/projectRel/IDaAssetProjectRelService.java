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

package tech.qiantong.qdata.module.da.service.assetchild.projectRel;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.projectRel.DaAssetProjectRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Asset - Project Relation Service Interface
 *
 * @author qdata
 * @date 2025-04-18
 */
public interface IDaAssetProjectRelService extends IService<DaAssetProjectRelDO> {

    /**
     * Get data asset-project relation page list
     *
     * @param pageReqVO page request
     * @return data asset-project relation page list
     */
    PageResult<DaAssetProjectRelDO> getDaAssetProjectRelPage(DaAssetProjectRelPageReqVO pageReqVO);

    /**
     * Get all data asset-project relation list
     *
     * @return data asset-project relation list
     */
    List<DaAssetProjectRelDO> getDaAssetProjectRelList(DaAssetProjectRelPageReqVO pageReqVO);

    /**
     * Create data asset-project relation
     *
     * @param createReqVO data asset-project relation info
     * @return data asset-project relation ID
     */
    Long createDaAssetProjectRel(DaAssetProjectRelSaveReqVO createReqVO);

    /**
     * Update data asset-project relation
     *
     * @param updateReqVO data asset-project relation info
     */
    int updateDaAssetProjectRel(DaAssetProjectRelSaveReqVO updateReqVO);

    /**
     * Delete data asset-project relation
     *
     * @param idList data asset-project relation ID list
     */
    int removeDaAssetProjectRel(Collection<Long> idList);

    /**
     * Get data asset-project relation details
     *
     * @param id data asset-project relation ID
     * @return data asset-project relation
     */
    DaAssetProjectRelDO getDaAssetProjectRelById(Long id);

    /**
     * Get all data asset-project relation list
     *
     * @return data asset-project relation list
     */
    List<DaAssetProjectRelDO> getDaAssetProjectRelList();

    /**
     * Get all data asset-project relation Map
     *
     * @return data asset-project relation Map
     */
    Map<Long, DaAssetProjectRelDO> getDaAssetProjectRelMap();


    /**
     * Import data asset-project relation data
     *
     * @param importExcelList data asset-project relation data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetProjectRel(List<DaAssetProjectRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    int removeProjectRelByAssetId(Long assetId);
}
