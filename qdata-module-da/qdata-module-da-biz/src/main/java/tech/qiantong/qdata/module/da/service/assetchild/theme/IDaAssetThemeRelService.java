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

package tech.qiantong.qdata.module.da.service.assetchild.theme;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.theme.DaAssetThemeRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Asset - Theme Relation Service Interface
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface IDaAssetThemeRelService extends IService<DaAssetThemeRelDO> {

    /**
     * Get data asset theme relation page list
     *
     * @param pageReqVO page request
     * @return data asset theme relation page list
     */
    PageResult<DaAssetThemeRelDO> getDaAssetThemeRelPage(DaAssetThemeRelPageReqVO pageReqVO);

    /**
     * Get all data asset theme relation list
     *
     * @return data asset theme relation list
     */
    List<DaAssetThemeRelRespVO> getDaAssetThemeRelList(DaAssetThemeRelPageReqVO pageReqVO);
    List<Long> getDaAssetIdList(List<Long> themeIdList);

    /**
     * Create data asset theme relation
     *
     * @param createReqVO data asset theme relation info
     * @return data asset theme relation ID
     */
    Long createDaAssetThemeRel(DaAssetThemeRelSaveReqVO createReqVO);

    void createDaAssetThemeRelList(List<String> themeIdList, Long id);

    /**
     * Update data asset theme relation
     *
     * @param updateReqVO data asset theme relation info
     */
    int updateDaAssetThemeRel(DaAssetThemeRelSaveReqVO updateReqVO);

    /**
     * Delete data asset theme relation
     *
     * @param idList data asset theme relation ID list
     */
    int removeDaAssetThemeRel(Collection<Long> idList);
    int removeThemeRelByAssetId( Long assetId);

    /**
     * Get data asset theme relation details
     *
     * @param id data asset theme relation ID
     * @return data asset theme relation
     */
    DaAssetThemeRelDO getDaAssetThemeRelById(Long id);

    /**
     * Get all data asset theme relation list
     *
     * @return data asset theme relation list
     */
    List<DaAssetThemeRelDO> getDaAssetThemeRelList();

    /**
     * Get all data asset theme relation Map
     *
     * @return data asset theme relation Map
     */
    Map<Long, DaAssetThemeRelDO> getDaAssetThemeRelMap();


    /**
     * Import data asset theme relation data
     *
     * @param importExcelList data asset theme relation data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetThemeRel(List<DaAssetThemeRelRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
