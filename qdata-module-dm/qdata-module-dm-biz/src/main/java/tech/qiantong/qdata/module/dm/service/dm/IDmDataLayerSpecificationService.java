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
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationSaveReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationPageReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerSpecificationDO;
/**
 * Data Warehouse Layer Specification Service Interface
 *
 * @author FXB
 * @date 2026-03-24
 */
public interface IDmDataLayerSpecificationService extends IService<DmDataLayerSpecificationDO> {

    /**
     * Get data warehouse layer specification page list
     *
     * @param pageReqVO Page request
     * @return Data warehouse layer specification page list
     */
    PageResult<DmDataLayerSpecificationDO> getDmDataLayerSpecificationPage(DmDataLayerSpecificationPageReqVO pageReqVO);

    /**
     * Create data warehouse layer specification
     *
     * @param createReqVO Data warehouse layer specification information
     * @return Data warehouse layer specification ID
     */
    Long createDmDataLayerSpecification(DmDataLayerSpecificationSaveReqVO createReqVO);

    /**
     * Update data warehouse layer specification
     *
     * @param updateReqVO Data warehouse layer specification information
     */
    int updateDmDataLayerSpecification(DmDataLayerSpecificationSaveReqVO updateReqVO);

    /**
     * Delete data warehouse layer specification
     *
     * @param idList Data warehouse layer specification IDs
     */
    int removeDmDataLayerSpecification(Collection<Long> idList);

    /**
     * Get data warehouse layer specification details
     *
     * @param id Data warehouse layer specification ID
     * @return Data warehouse layer specification
     */
    DmDataLayerSpecificationDO getDmDataLayerSpecificationById(Long id);

    /**
     * Get all data warehouse layer specifications
     *
     * @return Data warehouse layer specification list
     */
    List<DmDataLayerSpecificationDO> getDmDataLayerSpecificationPage();

    /**
     * Get all data warehouse layer specifications as Map
     *
     * @return Data warehouse layer specification Map
     */
    Map<Long, DmDataLayerSpecificationDO> getDmDataLayerSpecificationMap();


    /**
     * Import data warehouse layer specification data
     *
     * @param importExcelList Data warehouse layer specification data list
     * @param isUpdateSupport Whether to support update, if exists, update the data
     * @param operName Operation user
     * @return Result
     */
    String importDmDataLayerSpecification(List<DmDataLayerSpecificationRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
