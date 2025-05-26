package tech.qiantong.qdata.module.da.dal.mapper.datasource;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceProjectRelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据源与项目关联关系Mapper接口
 *
 * @author qdata
 * @date 2025-03-13
 */
public interface DaDatasourceProjectRelMapper extends BaseMapperX<DaDatasourceProjectRelDO> {

    default PageResult<DaDatasourceProjectRelDO> selectPage(DaDatasourceProjectRelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaDatasourceProjectRelDO>()
                .eqIfPresent(DaDatasourceProjectRelDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(DaDatasourceProjectRelDO::getProjectCode, reqVO.getProjectCode())
                .eqIfPresent(DaDatasourceProjectRelDO::getDatasourceId, reqVO.getDatasourceId())
                .eqIfPresent(DaDatasourceProjectRelDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DaDatasourceProjectRelDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaDatasourceProjectRelDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
