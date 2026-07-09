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

package tech.qiantong.qdata.module.da.service.asset;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.TreeData;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetRespVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnRelRuleVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.asset.DaAssetDO;
import tech.qiantong.qdata.neo4j.dto.LineageDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Asset Service Interface
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaAssetService extends IService<DaAssetDO> {

    /**
     * Get data asset page list
     *
     * @param pageReqVO page request
     * @return data asset page list
     */
    PageResult<DaAssetDO> getDaAssetPage(DaAssetPageReqVO pageReqVO, String daAssetQueryType);

    List<DaAssetDO> getDaAssetList(DaAssetPageReqVO daAsset);

    List<DaAssetDO> getTablesByDataSourceId(DaAssetPageReqVO pageReqVO);

    DaAssetDO getDaAssetByDaAssetPageReqVO(DaAssetPageReqVO pageReqVO);


    /**
     * Create data asset
     *
     * @param createReqVO data asset info
     * @return data asset ID
     */
    Long createDaAsset(DaAssetSaveReqVO createReqVO);

    /**
     * Update data asset
     *
     * @param updateReqVO data asset info
     */
    int updateDaAsset(DaAssetSaveReqVO updateReqVO);

    /**
     * Delete data asset
     *
     * @param idList data asset ID list
     */
    int removeDaAsset(Collection<Long> idList);

    int removeDaAsset(Long id);

    /**
     * Get data asset details
     *
     * @param id data asset ID
     * @return data asset
     */
    DaAssetRespVO getDaAssetById(Long id);

    DaAssetRespVO getDaAssetByIdSimple(Long id);


    /**
     * Get all data asset list
     *
     * @return data asset list
     */
    List<DaAssetDO> getDaAssetList();

    /**
     * Get all data asset Map
     *
     * @return data asset Map
     */
    Map<Long, DaAssetDO> getDaAssetMap();


    /**
     * Import data asset data
     *
     * @param importExcelList data asset data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName        operator user
     * @return result
     */
    String importDaAsset(List<DaAssetRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Data asset preview with masking rules applied
     *
     * @param jsonObject primary key id and condition query content
     * @return
     */
    Map<String, Object> getColumnData(JSONObject jsonObject);

    /**
     * Mask data asset data
     *
     * @param id   data asset id
     * @param data data asset data
     * @return
     */
    List<Map<String, Object>> dataMasking(Long id, List<Map<String, Object>> data);

    void insertAssetByDiscoveryInfo(DaAssetPageReqVO daAssetPageReqVO, List<DaAssetColumnSaveReqVO> columnSaveReqVOList);

    void updateAssetByDiscoveryInfo(DaAssetPageReqVO daAssetPageReqVO);

    PageResult<DaAssetDO> getDppAssetPage(DaAssetPageReqVO daAsset);

    List<DaAssetDO> getDppAssetNoPageList(DaAssetPageReqVO daAsset);

    Long createDaAssetNew(DaAssetSaveReqVO daAsset);

    /**
     * Bind resources
     */
    Long createDaAssetBindResources(DaAssetSaveReqVO daAsset);

    int updateDaAssetNew(DaAssetSaveReqVO daAsset);

    AjaxResult startDaAssetDatasourceTask(Long id);

    void startDaAssetDatasourceTaskNull();

    PageResult<DaAssetDO> getDaAssetByIds(List<Long> ids);

    List<DaAssetColumnRelRuleVO> listRelRule(Long id, String type);

    List<DaAssetColumnRelRuleVO> listRelRule(Long datasourceId, String tableName, String type);

    /**
     * Query data lineage by asset id
     *
     * @param id
     * @return
     */
    LineageDTO dataLineage(Long id);

    List<DaAssetDO> getDaAssetListAll(DaAssetPageReqVO daAsset, String number);

    /**
     * Get tree category data (combined from multiple data sources)
     *
     * @return
     */
    List<TreeData> getTreeData();

    /**
     * Batch create data assets
     *
     * @param daAssetList
     * @return
     */
    List<Long> createDaAssetBatchNew(List<DaAssetSaveReqVO> daAssetList);

    List<Map<String, Object>> dataMaskings(Long id, List<Map<String, Object>> tableData, Long userId, String scene);

    List<DaAssetDO> getDaAssetByDataSourceId(Long dataSourceId, String tableName);
}
