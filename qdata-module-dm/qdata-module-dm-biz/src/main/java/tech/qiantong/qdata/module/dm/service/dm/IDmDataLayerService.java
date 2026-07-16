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
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSaveReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerTreeRespVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerDO;

/**
 * Data Warehouse Layer Service Interface
 *
 * @author FXB
 * @date 2026-03-24
 */
public interface IDmDataLayerService extends IService<DmDataLayerDO> {

    /**
     * Get data warehouse layer page list
     *
     * @param pageReqVO Page request
     * @return Data warehouse layer page list
     */
    PageResult<DmDataLayerDO> getDmDataLayerPage(DmDataLayerPageReqVO pageReqVO);

    /**
     * Create data warehouse layer
     *
     * @param createReqVO Data warehouse layer information
     * @return Data warehouse layer ID
     */
    Long createDmDataLayer(DmDataLayerSaveReqVO createReqVO);

    /**
     * Update data warehouse layer
     *
     * @param updateReqVO Data warehouse layer information
     */
    int updateDmDataLayer(DmDataLayerSaveReqVO updateReqVO);

    /**
     * Delete data warehouse layer
     *
     * @param idList Data warehouse layer IDs
     */
    int removeDmDataLayer(Collection<Long> idList);

    /**
     * Get data warehouse layer details
     *
     * @param id Data warehouse layer ID
     * @return Data warehouse layer
     */
    DmDataLayerDO getDmDataLayerById(Long id);

    /**
     * Get all data warehouse layers
     *
     * @return Data warehouse layer list
     */
    List<DmDataLayerDO> getDmDataLayerList();

    /**
     * Get all data warehouse layers as Map
     *
     * @return Data warehouse layer Map
     */
    Map<Long, DmDataLayerDO> getDmDataLayerMap();


    /**
     * Import data warehouse layer data
     *
     * @param importExcelList Data warehouse layer data list
     * @param isUpdateSupport Whether to support update, if exists, update the data
     * @param operName        Operation user
     * @return Result
     */
    String importDmDataLayer(List<DmDataLayerRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Get data warehouse layer tree list
     *
     * @return Data warehouse layer tree list
     */
    List<DmDataLayerTreeRespVO> tree();
}
