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

package tech.qiantong.qdata.module.da.service.assetColumn;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Asset Column Service Interface
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaAssetColumnService extends IService<DaAssetColumnDO> {

    /**
     * Get data asset column page list
     *
     * @param pageReqVO page request
     * @return data asset column page list
     */
    PageResult<DaAssetColumnDO> getDaAssetColumnPage(DaAssetColumnPageReqVO pageReqVO);


    AjaxResult getColumnByAssetId(DaAssetColumnPageReqVO pageReqVO);

    List<DaAssetColumnDO> getDaAssetColumnList(DaAssetColumnPageReqVO pageReqVO);
    /**
     * Create data asset column
     *
     * @param createReqVO data asset column info
     * @return data asset column ID
     */
    Long createDaAssetColumn(DaAssetColumnSaveReqVO createReqVO);

    /**
     * Update data asset column
     *
     * @param updateReqVO data asset column info
     */
    int updateDaAssetColumn(DaAssetColumnSaveReqVO updateReqVO);

    /**
     * Delete data asset column
     *
     * @param idList data asset column ID list
     */
    int removeDaAssetColumn(Collection<Long> idList);

    /**
     * Get data asset column details
     *
     * @param id data asset column ID
     * @return data asset column
     */
    DaAssetColumnDO getDaAssetColumnById(Long id);

    /**
     * Get all data asset column list
     *
     * @return data asset column list
     */
    List<DaAssetColumnDO> getDaAssetColumnList();

    /**
     * Get all data asset column Map
     *
     * @return data asset column Map
     */
    Map<Long, DaAssetColumnDO> getDaAssetColumnMap();


    /**
     * Import data asset column data
     *
     * @param importExcelList data asset column data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetColumn(List<DaAssetColumnRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
