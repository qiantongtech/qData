package tech.qiantong.qdata.module.dp.dal.mapper.model;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedPageReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelMaterializedDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 物化模型记录Mapper接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DpModelMaterializedMapper extends BaseMapperX<DpModelMaterializedDO> {

    default PageResult<DpModelMaterializedDO> selectPage(DpModelMaterializedPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DpModelMaterializedDO>()
                .likeIfPresent(DpModelMaterializedDO::getModelName, reqVO.getModelName())
                .eqIfPresent(DpModelMaterializedDO::getModelAlias, reqVO.getModelAlias())
                .eqIfPresent(DpModelMaterializedDO::getModelId, reqVO.getModelId())
                .eqIfPresent(DpModelMaterializedDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DpModelMaterializedDO::getMessage, reqVO.getMessage())
                .eqIfPresent(DpModelMaterializedDO::getSqlCommand, reqVO.getSqlCommand())
                .eqIfPresent(DpModelMaterializedDO::getDatasourceId, reqVO.getDatasourceId())
                .eqIfPresent(DpModelMaterializedDO::getDatasourceType, reqVO.getDatasourceType())
                .likeIfPresent(DpModelMaterializedDO::getDatasourceName, reqVO.getDatasourceName())
                .eqIfPresent(DpModelMaterializedDO::getAssetId, reqVO.getAssetId())
                .eqIfPresent(DpModelMaterializedDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DpModelMaterializedDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
