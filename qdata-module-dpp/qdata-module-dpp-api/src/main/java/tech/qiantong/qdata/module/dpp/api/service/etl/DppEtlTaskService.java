package tech.qiantong.qdata.module.dpp.api.service.etl;

import java.util.List;

public interface DppEtlTaskService {

    /**
     * 查询数据源主键集合是否被引用
     * @param datasourceIdList 数据源id集合
     * @return 条数
     */
    int checkTaskIdInDatasource(List<Long> datasourceIdList,List<Long> projectIdList);

    /**
     * 查询资产主键集合是否被引用
     * @param assetIdList 资产主键集合
     * @return 条数
     */
    int checkTaskIdInAsset(List<Long> assetIdList);
}
