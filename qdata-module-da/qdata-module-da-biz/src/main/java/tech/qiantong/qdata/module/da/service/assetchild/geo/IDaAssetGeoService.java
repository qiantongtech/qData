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

package tech.qiantong.qdata.module.da.service.assetchild.geo;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.geo.DaAssetGeoDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Asset - Vector Service Interface
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface IDaAssetGeoService extends IService<DaAssetGeoDO> {

    /**
     * Get data asset vector page list
     *
     * @param pageReqVO page request
     * @return data asset vector page list
     */
    PageResult<DaAssetGeoDO> getDaAssetGeoPage(DaAssetGeoPageReqVO pageReqVO);

    DaAssetGeoRespVO getDaAssetGeoByAssetId(Long assetId);

    /**
     * Create data asset vector
     *
     * @param createReqVO data asset vector info
     * @return data asset vector ID
     */
    Long createDaAssetGeo(DaAssetGeoSaveReqVO createReqVO);

    /**
     * Update data asset vector
     *
     * @param updateReqVO data asset vector info
     */
    int updateDaAssetGeo(DaAssetGeoSaveReqVO updateReqVO);

    /**
     * Delete data asset vector
     *
     * @param idList data asset vector ID list
     */
    int removeDaAssetGeo(Collection<Long> idList);

    /**
     * Get data asset vector details
     *
     * @param id data asset vector ID
     * @return data asset vector
     */
    DaAssetGeoDO getDaAssetGeoById(Long id);

    /**
     * Get all data asset vector list
     *
     * @return data asset vector list
     */
    List<DaAssetGeoDO> getDaAssetGeoList();

    /**
     * Get all data asset vector Map
     *
     * @return data asset vector Map
     */
    Map<Long, DaAssetGeoDO> getDaAssetGeoMap();


    /**
     * Import data asset vector data
     *
     * @param importExcelList data asset vector data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetGeo(List<DaAssetGeoRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
