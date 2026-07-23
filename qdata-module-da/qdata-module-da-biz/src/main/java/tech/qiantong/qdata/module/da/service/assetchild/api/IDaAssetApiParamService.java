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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiParamDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Asset - External API - Parameter Service Interface
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface IDaAssetApiParamService extends IService<DaAssetApiParamDO> {

    /**
     * Get data asset external API parameter page list
     *
     * @param pageReqVO page request
     * @return data asset external API parameter page list
     */
    PageResult<DaAssetApiParamDO> getDaAssetApiParamPage(DaAssetApiParamPageReqVO pageReqVO);

    /**
     * Create data asset external API parameter
     *
     * @param createReqVO data asset external API parameter info
     * @return data asset external API parameter ID
     */
    Long createDaAssetApiParam(DaAssetApiParamSaveReqVO createReqVO);

    void createDaAssetApiParamDeep(List<DaAssetApiParamSaveReqVO> daAssetApiParamList, Long daAssetApiId);

    /**
     * Update data asset external API parameter
     *
     * @param updateReqVO data asset external API parameter info
     */
    int updateDaAssetApiParam(DaAssetApiParamSaveReqVO updateReqVO);

    /**
     * Delete data asset external API parameter
     *
     * @param idList data asset external API parameter ID list
     */
    int removeDaAssetApiParam(Collection<Long> idList);
    int removeThemeRelByAssetApiId( Long assetApiId);

    /**
     * Get data asset external API parameter details
     *
     * @param id data asset external API parameter ID
     * @return data asset external API parameter
     */
    DaAssetApiParamDO getDaAssetApiParamById(Long id);

    /**
     * Get all data asset external API parameter list
     *
     * @return data asset external API parameter list
     */
    List<DaAssetApiParamDO> getDaAssetApiParamList();
    List<DaAssetApiParamRespVO> getDaAssetApiParamList(Long daAssetApiId);

    /**
     * Get all data asset external API parameter Map
     *
     * @return data asset external API parameter Map
     */
    Map<Long, DaAssetApiParamDO> getDaAssetApiParamMap();


    /**
     * Import data asset external API parameter data
     *
     * @param importExcelList data asset external API parameter data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetApiParam(List<DaAssetApiParamRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
