package tech.qiantong.qdata.module.da.api.service.asset;

import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.module.da.api.datasource.dto.DatasourceCreaTeTableListReqDTO;
import tech.qiantong.qdata.module.da.api.datasource.dto.DatasourceCreaTeTableReqDTO;

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

}
