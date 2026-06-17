/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
