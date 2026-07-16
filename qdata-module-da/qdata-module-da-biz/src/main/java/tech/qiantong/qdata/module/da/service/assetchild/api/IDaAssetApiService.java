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

package tech.qiantong.qdata.module.da.service.assetchild.api;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiDO;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Asset - External API Service Interface
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface IDaAssetApiService extends IService<DaAssetApiDO> {

    /**
     * Get data asset external API page list
     *
     * @param pageReqVO page request
     * @return data asset external API page list
     */
    PageResult<DaAssetApiDO> getDaAssetApiPage(DaAssetApiPageReqVO pageReqVO);

    DaAssetApiRespVO getDaAssetApiByAssetId(Long assetId);

    /**
     * Create data asset external API
     *
     * @param createReqVO data asset external API info
     * @return data asset external API ID
     */
    Long createDaAssetApi(DaAssetApiSaveReqVO createReqVO);

    /**
     * Update data asset external API
     *
     * @param updateReqVO data asset external API info
     */
    int updateDaAssetApi(DaAssetApiSaveReqVO updateReqVO);

    /**
     * Delete data asset external API
     *
     * @param idList data asset external API ID list
     */
    int removeDaAssetApi(Collection<Long> idList);

    /**
     * Get data asset external API details
     *
     * @param id data asset external API ID
     * @return data asset external API
     */
    DaAssetApiDO getDaAssetApiById(Long id);

    /**
     * Get all data asset external API list
     *
     * @return data asset external API list
     */
    List<DaAssetApiDO> getDaAssetApiList();

    /**
     * Get all data asset external API Map
     *
     * @return data asset external API Map
     */
    Map<Long, DaAssetApiDO> getDaAssetApiMap();


    /**
     * Import data asset external API data
     *
     * @param importExcelList data asset external API data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetApi(List<DaAssetApiRespVO> importExcelList, boolean isUpdateSupport, String operName);

    void queryServiceForwarding(HttpServletResponse response, DaAssetApiReqVO daAssetApi);
}
