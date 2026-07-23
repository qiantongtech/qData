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

import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbName;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.module.da.api.datasource.dto.DatasourceCreaTeTableListReqDTO;
import tech.qiantong.qdata.module.da.api.datasource.dto.DatasourceCreaTeTableReqDTO;

import java.util.List;

/**
 * Datasource Service Interface
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaDatasourceApiService {


    public DaDatasourceRespDTO getDatasourceById(Long id);
    public boolean creaDatasourceTeTableApi(DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO);
    public boolean creaDatasourceTeTableApi(DbQuery dbQuery, DbQueryProperty dbQueryProperty, DatasourceCreaTeTableReqDTO creaTeTableReqDTO);
    public boolean creaDatasourceTeTableListApi(DatasourceCreaTeTableListReqDTO datasourceCreaTeTableListReqDTO);

    /**
     * Get column list
     *
     * @param datasourceId Datasource ID
     * @param tableName    Table Name
     * @return Datasource List
     */
    List<DbColumn> getDbTableColumns(Long datasourceId, String tableName);


    /**
     * Get table info
     *
     * @param datasourceId Datasource ID
     * @param tableName    Table Name
     * @return Datasource List
     */
    DbTable getDbTable(Long datasourceId, String tableName);

    /**
     * TODO: Get database list
     *
     * @param id Datasource ID
     * @return Datasource List
     */
    public List<DbName> getDatabaseListByDatasourceId(Long id);

    /**
     * Get datasource list by IDs
     * @param ids
     * @return
     */
    List<DaDatasourceRespDTO> getDatabaseListByIds(List<Long> ids);
}
