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
 * 数据资产Service接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaAssetApiOutService {

    public DaAssetRespDTO insertDaAsset(DaAssetReqDTO daAssetReqDTO);

    /**
     * 根据类目编码查询数量
     *
     * @return
     */
    Long getCountByCatCode(String catCode);

    /**
     * 获取资产集合分页
     */
    PageResult<DaAssetRespDTO> daAssetListPage(DaAssetReqDTO daAssetReqDTO);

    Map<String,Object> getDaAssetOverviewStatistics();


    /**
     * 将老的 CAT_CODE 批量更新成新的 CAT_CODE
     *
     * @param oldCatCode 旧分类编码
     * @param newCatCode 新分类编码
     * @return 受影响行数
     */
    int updateCatCode(String oldCatCode, String newCatCode);

    /**
     * 根据mc表id列表，获取在资产中存在的mc表id列表
     * @param mcTableIds
     * @return
     */
    List<Long> getMcTableInDaAsset(List<Long> mcTableIds);

    /**
     * 将mc字段批量更新到到资产中
     * @param columnMap
     */
    void mcTableColumnUpdateToDaAssetColumn(Map<Long, List<McColumnRespDTO>> columnMap);

    /**
     * 检查是否有资产使用了指定的元数据表ID
     *
     * @param tableId 元数据表ID
     * @return 是否存在使用该表的资产
     */
    boolean existsByTableId(Long tableId);
}
