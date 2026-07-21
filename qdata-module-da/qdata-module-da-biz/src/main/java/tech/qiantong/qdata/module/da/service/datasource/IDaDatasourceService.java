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

package tech.qiantong.qdata.module.da.service.datasource;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectReqDTO;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectRespDTO;
import tech.qiantong.qdata.module.da.api.datasource.dto.DatasourceCreaTeTableReqDTO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourcePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceRespVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelColumnReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Datasource Service Interface
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaDatasourceService extends IService<DaDatasourceDO> {

    /**
     * Get datasource page list
     *
     * @param pageReqVO page request
     * @return datasource page list
     */
    PageResult<DaDatasourceDO> getDaDatasourcePage(DaDatasourcePageReqVO pageReqVO);

    /**
     * Query datasource list in data development
     *
     * @param daDatasource page request
     * @return datasource page list
     */
    PageResult<DaDatasourceDO> getDaDatasourceDppPage(DaDatasourcePageReqVO daDatasource);

    List<DaDatasourceDO> getDaDatasourceList(DaDatasourcePageReqVO pageReqVO);


    /**
     * Query datasource connection info for data asset
     *
     * @param daAsset
     * @return
     */
    List<DaDatasourceDO> getDataSourceByAsset(DaDatasourceRespVO daAsset);


    /**
     * Create datasource
     *
     * @param createReqVO datasource info
     * @return datasource ID
     */
    Long createDaDatasource(DaDatasourceSaveReqVO createReqVO);

    /**
     * Update datasource
     *
     * @param updateReqVO datasource info
     */
    int updateDaDatasource(DaDatasourceSaveReqVO updateReqVO);

    /**
     * Test connection information before creating a datasource.
     */
    boolean testDatasourceConnection(DaDatasourceSaveReqVO datasource);

    /**
     * Delete datasource
     *
     * @param idList datasource ID list
     */
    int removeDaDatasource(Collection<Long> idList);


    /**
     * Delete datasource with type check for data asset or data development
     * @param idList delete ID list
     * @param type 0: Data Asset, 1: Data Development
     * @return
     */
    int removeDaDatasourceDppOrDa(List<Long> idList, Long type);

    /**
     * Get datasource details
     *
     * @param id datasource ID
     * @return datasource
     */
    DaDatasourceDO getDaDatasourceById(Long id);
    DaDatasourceRespVO getDaDatasourceByIdSimple(Long id);

    /**
     * Get all datasource list
     *
     * @return datasource list
     */
    List<DaDatasourceDO> getDaDatasourceList();

    /**
     * Get all datasource Map
     *
     * @return datasource Map
     */
    Map<Long, DaDatasourceDO> getDaDatasourceMap();


    /**
     * Import datasource data
     *
     * @param importExcelList datasource data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName        operator user
     * @return result
     */
    String importDaDatasource(List<DaDatasourceRespVO> importExcelList, boolean isUpdateSupport, String operName);


    AjaxResult clientsTest(Long id);

    /**
     * Test a datasource connection using unsaved connection properties.
     */
    Boolean clientTest(DbQueryProperty dbQueryProperty);

    /**
     * Test a saved datasource connection.
     */
    Boolean clientTest(Long id);

    /**
     * Get database table info
     *
     * @param id datasource ID
     * @return
     */
    List<DbTable> getDbTables(Long id);

    /**
     * Get database table column info
     *
     * @param id        datasource ID
     * @param tableName table name
     * @return
     */
    List<DbColumn> getDbTableColumns(Long id, String tableName);

    /**
     * Get data columns in the data table
     *
     * @param jsonObject datasource ID and data table
     * @return
     */
    List<DpModelColumnReqDTO> getColumnsList(JSONObject jsonObject);

    List<DaAssetColumnDO> columnsAsAssetColumnList(JSONObject jsonObject);


    List<DaAssetColumnDO> columnsAsAssetColumnList(Long id, String tableName);


    /**
     * Table creation utility method
     *
     * @param datasourceCreaTeTableReqDTO single table
     * @return
     */
    boolean creaDatasourceTeTable(DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO);

    boolean creaDatasourceTeTable(DbQuery dbQuery, DbQueryProperty dbQueryProperty, DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO);


    /**
     * Query project list, make data added by development module unselectable
     *
     * @param pageReqVO
     * @return
     */
    PageResult<AttProjectRespDTO> getNoDppAddList(AttProjectReqDTO pageReqVO);

    /**
     * Exclude Kafka and list datasources for the current project in data integration
     * @param daDatasource
     * @return
     */
    List<DaDatasourceDO> getDaDatasourceDppNoKafka(DaDatasourcePageReqVO daDatasource);

    tech.qiantong.qdata.common.database.core.PageResult<Map<String, Object>> executeSqlQuery(DaDatasourcePageReqVO daDatasource);

    void exportSqlQueryResult(HttpServletResponse response, DaDatasourcePageReqVO daDatasource);

    List<DbColumn> sqlParse(String sourceId, String sqlText);

    /**
     * Modify datasource status
     * @param datasourceId
     * @param status
     * @return
     */
    Boolean editDatasourceStatus(Long datasourceId, Long status);

    /**
     * Scheduled task method
     *
     * @param id
     */
    void detectTableSchemaUpdates(Long id);
}
