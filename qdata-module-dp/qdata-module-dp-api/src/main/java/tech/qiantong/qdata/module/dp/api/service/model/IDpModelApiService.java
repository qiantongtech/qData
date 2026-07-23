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

package tech.qiantong.qdata.module.dp.api.service.model;

import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelReqDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelRespDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelColumnRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelRespDTO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Logical Model Service Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpModelApiService {

    /**
     * Get logical model info by logical model ID
     *
     * @param id
     * @return
     */
    DpModelRespDTO getDpModelByIdApi(Long id);

    /**
     * Get logical model column list by logical model ID
     *
     * @param modelId Logical Model ID
     * @return Logical Model Column List
     */
    List<DpModelColumnRespDTO> getDpModelColumnListByModelIdApi(Long modelId);

    /**
     * Query data element info by data element IDs
     *
     * @param ids
     * @return
     */
    List<DpDataElemRespDTO> getDpDataElemListByIdsApi(Set<Long> ids);

    /**
     * Get data element ID set by asset ID
     *
     * @param assetId
     * @return
     */
    Set<Long> getDpDataElemListByAssetIdApi(Long assetId);

    List<DpDataElemAssetRelRespDTO> getDpDataElemListByColumnIdInApi(Collection<Long> columnIds);

    /**
     * Get data element ID set by asset ID and column ID
     *
     * @param assetId
     * @return
     */
    Set<Long> getDpDataElemListByAssetIdAndColumnId(Long assetId, Long columnId);

    /**
     * Insert data element and asset relation data
     *
     * @param dpDataElemAssetRel
     * @return
     */
    boolean insertElementAssetRelation(List<DpDataElemAssetRelReqDTO> dpDataElemAssetRel);

    /**
     * Get count by category code
     *
     * @return
     */
    Long getCountByCatCode(String catCode);

    /**
     * Update data element and asset relation data
     *
     * @param dpDataElemAssetRel
     * @return
     */
    boolean updateElementAssetRelation(DpDataElemAssetRelReqDTO dpDataElemAssetRel);

    /**
     * Query data element info by asset ID and code table ID
     *
     * @param assetId
     * @param codeId
     * @return
     */
    List<DpDataElemRespDTO> getDpDataElemListByAssetId(Long assetId, Set<Long> codeId);

    /**
     * Query column list under model by model ID
     *
     * @param modelId Model ID
     */
    List<DpModelColumnRespDTO> getModelIdColumnList(Long modelId);

    /**
     * Batch update old CAT_CODE to new CAT_CODE
     *
     * @param oldCatCode Old Category Code
     * @param newCatCode New Category Code
     * @return Affected row count
     */
    int updateCatCode(String oldCatCode, String newCatCode);
}
