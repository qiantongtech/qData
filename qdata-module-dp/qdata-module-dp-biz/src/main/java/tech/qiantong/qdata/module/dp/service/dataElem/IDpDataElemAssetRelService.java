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

package tech.qiantong.qdata.module.dp.service.dataElem;


import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemAssetRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Element Asset Relation Information Service Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpDataElemAssetRelService extends IService<DpDataElemAssetRelDO> {

    /**
     * Get data element asset relation information paginated list
     *
     * @param pageReqVO Pagination request
     * @return Paginated list of data element asset relation information
     */
    PageResult<DpDataElemAssetRelDO> getDpDataElemAssetRelPage(DpDataElemAssetRelPageReqVO pageReqVO);

    /**
     * Create data element asset relation information
     *
     * @param createReqVO Data element asset relation information
     * @return Data element asset relation information ID
     */
    Long createDpDataElemAssetRel(DpDataElemAssetRelSaveReqVO createReqVO);

    /**
     * Update data element asset relation information
     *
     * @param updateReqVO Data element asset relation information
     */
    int updateDpDataElemAssetRel(DpDataElemAssetRelSaveReqVO updateReqVO);

    /**
     * Delete data element asset relation information
     *
     * @param idList Data element asset relation information ID list
     */
    int removeDpDataElemAssetRel(Collection<Long> idList);

    /**
     * Get data element asset relation information details
     *
     * @param id Data element asset relation information ID
     * @return Data element asset relation information
     */
    DpDataElemAssetRelDO getDpDataElemAssetRelById(Long id);

    /**
     * Get all data element asset relation information list
     *
     * @return Data element asset relation information list
     */
    List<DpDataElemAssetRelDO> getDpDataElemAssetRelList();

    /**
     * Get all data element asset relation information Map
     *
     * @return Data element asset relation information Map
     */
    Map<Long, DpDataElemAssetRelDO> getDpDataElemAssetRelMap();


    /**
     * Import data element asset relation information data
     *
     * @param importExcelList Data element asset relation information data list
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName Operator
     * @return Result
     */
    String importDpDataElemAssetRel(List<DpDataElemAssetRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
