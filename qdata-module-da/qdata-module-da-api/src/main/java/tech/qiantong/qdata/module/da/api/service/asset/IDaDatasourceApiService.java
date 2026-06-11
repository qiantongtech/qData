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
