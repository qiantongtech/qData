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
 * 数据源Service接口
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
     * 获得字段列表
     *
     * @param datasourceId 数据源id
     * @param tableName    表名
     * @return 数据源列表
     */
    List<DbColumn> getDbTableColumns(Long datasourceId, String tableName);


    /**
     * 获得表信息
     *
     * @param datasourceId 数据源id
     * @param tableName    表名
     * @return 数据源列表
     */
    DbTable getDbTable(Long datasourceId, String tableName);

    /**
     * TODO:获得数据库列表
     *
     * @param id 数据源id
     * @return 数据源列表
     */
    public List<DbName> getDatabaseListByDatasourceId(Long id);

    /**
     * 根据ids获取数据源列表
     * @param ids
     * @return
     */
    List<DaDatasourceRespDTO> getDatabaseListByIds(List<Long> ids);
}
