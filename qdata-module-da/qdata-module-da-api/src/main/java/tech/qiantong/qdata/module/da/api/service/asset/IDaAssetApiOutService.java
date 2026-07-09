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

package tech.qiantong.qdata.module.da.api.service.asset;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetReqDTO;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetRespDTO;
import tech.qiantong.qdata.module.mc.api.column.dto.McColumnRespDTO;

import java.util.List;
import java.util.Map;

/**
 * Data Asset Service Interface
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaAssetApiOutService {

    public DaAssetRespDTO insertDaAsset(DaAssetReqDTO daAssetReqDTO);

    /**
     * Query count by category code
     *
     * @return
     */
    Long getCountByCatCode(String catCode);

    /**
     * Get paginated asset list
     */
    PageResult<DaAssetRespDTO> daAssetListPage(DaAssetReqDTO daAssetReqDTO);

    Map<String,Object> getDaAssetOverviewStatistics();


    /**
     * Batch update old CAT_CODE to new CAT_CODE
     *
     * @param oldCatCode Old Category Code
     * @param newCatCode New Category Code
     * @return Rows affected
     */
    int updateCatCode(String oldCatCode, String newCatCode);

    /**
     * Get MC table IDs that exist in assets from the given MC table ID list
     * @param mcTableIds
     * @return
     */
    List<Long> getMcTableInDaAsset(List<Long> mcTableIds);

    /**
     * Batch update MC columns to assets
     * @param columnMap
     */
    void mcTableColumnUpdateToDaAssetColumn(Map<Long, List<McColumnRespDTO>> columnMap);

    /**
     * Check if any asset uses the specified metadata table ID
     *
     * @param tableId Metadata Table ID
     * @return Whether any asset uses this table
     */
    boolean existsByTableId(Long tableId);
}
