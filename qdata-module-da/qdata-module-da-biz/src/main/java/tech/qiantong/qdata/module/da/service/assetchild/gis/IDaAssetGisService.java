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

package tech.qiantong.qdata.module.da.service.assetchild.gis;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.gis.DaAssetGisDO;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Asset - Geospatial Service Interface
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface IDaAssetGisService extends IService<DaAssetGisDO> {

    /**
     * Get data asset geospatial service page list
     *
     * @param pageReqVO page request
     * @return data asset geospatial service page list
     */
    PageResult<DaAssetGisDO> getDaAssetGisPage(DaAssetGisPageReqVO pageReqVO);

    DaAssetGisRespVO getDaAssetGisByAssetId(Long assetId);

    /**
     * Create data asset geospatial service
     *
     * @param createReqVO data asset geospatial service info
     * @return data asset geospatial service ID
     */
    Long createDaAssetGis(DaAssetGisSaveReqVO createReqVO);

    /**
     * Update data asset geospatial service
     *
     * @param updateReqVO data asset geospatial service info
     */
    int updateDaAssetGis(DaAssetGisSaveReqVO updateReqVO);

    /**
     * Delete data asset geospatial service
     *
     * @param idList data asset geospatial service ID list
     */
    int removeDaAssetGis(Collection<Long> idList);

    /**
     * Get data asset geospatial service details
     *
     * @param id data asset geospatial service ID
     * @return data asset geospatial service
     */
    DaAssetGisDO getDaAssetGisById(Long id);

    /**
     * Get all data asset geospatial service list
     *
     * @return data asset geospatial service list
     */
    List<DaAssetGisDO> getDaAssetGisList();

    /**
     * Get all data asset geospatial service Map
     *
     * @return data asset geospatial service Map
     */
    Map<Long, DaAssetGisDO> getDaAssetGisMap();


    /**
     * Import data asset geospatial service data
     *
     * @param importExcelList data asset geospatial service data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetGis(List<DaAssetGisRespVO> importExcelList, boolean isUpdateSupport, String operName);

    void queryServiceForwarding(HttpServletResponse response, DaAssetGisReqVO daAssetGisReqVO);
}
