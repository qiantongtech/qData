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

package tech.qiantong.qdata.quality.service.datasource;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.quality.controller.da.datasource.vo.DaDatasourcePageReqVO;
import tech.qiantong.qdata.quality.controller.da.datasource.vo.DaDatasourceRespVO;
import tech.qiantong.qdata.quality.controller.da.datasource.vo.DaDatasourceSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.datasource.DaDatasourceDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data source Service interface
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaDatasourceQualityService extends IService<DaDatasourceDO> {

    /**
     * Get a paginated list of data sources
     *
     * @param pageReqVO paging request
     * @return data source paginated list
     */
    PageResult<DaDatasourceDO> getDaDatasourcePage(DaDatasourcePageReqVO pageReqVO);

    List<DaDatasourceDO> getDaDatasourceList(DaDatasourcePageReqVO pageReqVO);

    DaDatasourceRespDTO getDatasourceById(Long id);

    /**
     * Query the data source connection information of data assets
     *
     * @param daAsset
     * @return
     */
    List<DaDatasourceDO> getDataSourceByAsset(DaDatasourceRespVO daAsset);


    /**
     * Create data source
     *
     * @param createReqVO data source information
     * @return data source number
     */
    Long createDaDatasource(DaDatasourceSaveReqVO createReqVO);

    /**
     * Delete data source
     *
     * @param idList data source number
     */
    int removeDaDatasource(Collection<Long> idList);



    /**
     * Get data source details
     *
     * @param id data source number
     * @return data source
     */
    DaDatasourceDO getDaDatasourceById(Long id);
    DaDatasourceRespVO getDaDatasourceByIdSimple(Long id);

    /**
     * Get a list of all data sources
     *
     * @return data source list
     */
    List<DaDatasourceDO> getDaDatasourceList();

    /**
     * Get all data sources Map
     *
     * @return data source Map
     */
    Map<Long, DaDatasourceDO> getDaDatasourceMap();


    /**
     * Import data source data
     *
     * @param importExcelList data source data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    String importDaDatasource(List<DaDatasourceRespVO> importExcelList, boolean isUpdateSupport, String operName);


    AjaxResult clientsTest(Long id);

    /**
     * Get database table information
     *
     * @param id data source id
     * @return
     */
    List<DbTable> getDbTables(Long id);

    /**
     * Get database
     * Table field information
     *
     * @param id data source id
     * @param tableName table name
     * @return
     */
    List<DbColumn> getDbTableColumns(Long id, String tableName);
}
