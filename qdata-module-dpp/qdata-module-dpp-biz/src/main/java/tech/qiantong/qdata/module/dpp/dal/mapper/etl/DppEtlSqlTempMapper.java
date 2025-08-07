package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSqlTempDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据集成SQL模版Mapper接口
 *
 * @author FXB
 * @date 2025-06-25
 */
public interface DppEtlSqlTempMapper extends BaseMapperX<DppEtlSqlTempDO> {

    default PageResult<DppEtlSqlTempDO> selectPage(DppEtlSqlTempPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DppEtlSqlTempDO>()
                .likeIfPresent(DppEtlSqlTempDO::getName, reqVO.getName())
                .eqIfPresent(DppEtlSqlTempDO::getType, reqVO.getType())
                .eqIfPresent(DppEtlSqlTempDO::getContent, reqVO.getContent())
                .eqIfPresent(DppEtlSqlTempDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DppEtlSqlTempDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DppEtlSqlTempDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
